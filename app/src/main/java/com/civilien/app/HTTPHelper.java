package com.civilien.app;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * Created by Sehail Fillali on 22/12/15.
 */
public class HTTPHelper {
    public HTTPHelper() {
    }

    JSONObject json;

    public JSONObject JSON_POST_Request(String location, Incident json_toSend){
        JSONObject response = new JSONObject();
        HttpURLConnection conn = null;
        try {

            // format json_toSend.data into request parameters
            StringBuilder request_params = new StringBuilder();
            int i = 0;
            Iterator<String> keys = json_toSend.keys();
            while (keys.hasNext()){
                String key = keys.next();
                if(i != 0){
                    request_params.append("&");
                }
                request_params.append(key).append("=").append(URLEncoder.encode((String) json_toSend.get(key), "UTF-8"));
                i++;
            }
            // Check log cat for request parameters
            Log.d("*", "*************************************************************************************************");
            Log.d("___REQUEST_PARAMS___", request_params.toString());


            // create connection and send request
            URL url = new URL(CONSTANTS.URL_CREATE_INCIDENT);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=utf-8");
            conn.setRequestProperty("Content-Length","" + Integer.toString(request_params.toString().getBytes().length));
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setUseCaches(false);
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);

            DataOutputStream writer = new DataOutputStream(conn.getOutputStream());
            writer.writeBytes(request_params.toString());
            writer.flush();
            writer.close();

            conn.connect();

            InputStream InpS = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    InpS, "iso-8859-1"), 8);


            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                line = line + "\n";
                sb.append(line);
            }
            InpS.close();
            // Check log cat for built string
            Log.d("___RESPONSE STRING___", sb.toString());
            if (sb.length() > 0) {
                response = new JSONObject(sb.toString());
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        // Check log cat for JSON response
//            Log.d("___RESPONSE___", response.toString());
        return response;
    }
    public JSONObject JSON_GET_Request(String location){

        HttpURLConnection conn = null;

        try {
            URL url = new URL(location);
            conn = (HttpURLConnection) url.openConnection();



            InputStream InpS = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    InpS, "iso-8859-1"), 8);


            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                line = line + "\n";
                sb.append(line);
            }
            InpS.close();
            Log.d("___RESPONSE STRING___", sb.toString());

            json = new JSONObject(sb.toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        JSONObject jsonObject = json;
        if (json == null) {
            try {
                jsonObject = new JSONObject("{" + TAGS.SUCCESS + ":0,\"message\":\"Failed to retrieve data\"}");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Check log cat for JSON response
        Log.d("JSON DATA", jsonObject.toString());

        return jsonObject;
    }


}
