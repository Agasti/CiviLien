package com.civilien.app;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Sehail Fillali on 22/12/15.
 */
public class HttpJSONRequest {
    public HttpJSONRequest() {
    }

    JSONObject json;

    public JSONObject makeJSONRequest(String location){

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
