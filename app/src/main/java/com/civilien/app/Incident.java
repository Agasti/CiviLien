package com.civilien.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Created by Sehail Fillali on 5/12/15.
 */
public class Incident extends JSONObject implements Serializable{

    public Incident() throws JSONException {}

    public Incident setIncID(String value)throws JSONException{ return (Incident) this.put(TAGS.INC_ID, value);}
    public Incident setPostDate(String value)throws JSONException{ return (Incident) this.put(TAGS.POSTDATE, value);}
    public Incident setCategory(String value)throws JSONException{ return (Incident) this.put(TAGS.CATEGORY, value);}
    public Incident setType(String value)throws JSONException{ return (Incident) this.put(TAGS.TYPE, value);}
    public Incident setUser(String value)throws JSONException{ return (Incident) this.put(TAGS.USER, value);}
    public Incident setTitle(String value)throws JSONException{ return (Incident) this.put(TAGS.TITLE, value);}
    public Incident setGPSLat(String value)throws JSONException{ return (Incident) this.put(TAGS.GPS_LAT, value);}
    public Incident setGPSLon(String value)throws JSONException{ return (Incident) this.put(TAGS.GPS_LON, value);}
    public Incident setRelevance(String value)throws JSONException{ return (Incident) this.put(TAGS.RELEVANCE, value);}

    public Object getIncID()throws JSONException{ return this.getString(TAGS.INC_ID);}
    public Object getPostDate()throws JSONException{ return this.getString(TAGS.POSTDATE);}
    public Object getCategory()throws JSONException{ return this.getString(TAGS.CATEGORY);}
    public Object getType()throws JSONException{ return this.getString(TAGS.TYPE);}
    public Object getUser()throws JSONException{ return this.getString(TAGS.USER);}
    public Object getTitle()throws JSONException{ return this.getString(TAGS.TITLE);}
    public Object getGPSLat()throws JSONException{ return this.getString(TAGS.GPS_LAT);}
    public Object getGPSLon()throws JSONException{ return super.getString(TAGS.GPS_LON);}
    public Object getRelevance()throws JSONException{ return super.getString(TAGS.RELEVANCE);}

    public Incident (String IncID, String PostDate, String Category, String Type,  String User, String Title, String GPSLat, String GPSLon, String Relevance)
    throws JSONException {
        this.setIncID(IncID);
        this.setPostDate(PostDate);
        this.setCategory(Category);
        this.setType(Type);
        this.setUser(User);
        this.setTitle(Title);
        this.setGPSLat(GPSLat);
        this.setGPSLon(GPSLon);
        this.setRelevance(Relevance);
    }

    public Incident (String Category, String Type,  String User, String Title, String GPSLat, String GPSLon)
            throws JSONException {
        this.setCategory(Category);
        this.setType(Type);
        this.setUser(User);
        this.setTitle(Title);
        this.setGPSLat(GPSLat);
        this.setGPSLon(GPSLon);
    }

    public Incident (JSONObject jsonObject)
            throws JSONException {
        this.setIncID(jsonObject.getString(TAGS.INC_ID));
        this.setPostDate(jsonObject.getString(TAGS.POSTDATE));
        this.setCategory(jsonObject.getString(TAGS.CATEGORY));
        this.setType(jsonObject.getString(TAGS.TYPE));
        this.setUser(jsonObject.getString(TAGS.USER));
        this.setTitle(jsonObject.getString(TAGS.TITLE));
        this.setGPSLat(jsonObject.getString(TAGS.GPS_LAT));
        this.setGPSLon(jsonObject.getString(TAGS.GPS_LON));
        this.setRelevance(jsonObject.getString(TAGS.RELEVANCE));
    }

    @Override
    public Iterator<String> keys() {
        return super.keys();
    }

    @Override
    public JSONArray names() {
        return super.names();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public String toString(int indentSpaces) throws JSONException {
        return super.toString(indentSpaces);
    }

    @Override
    public int length() {
        return super.length();
    }
}

