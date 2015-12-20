package com.civilien.app;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sehail Fillali on 5/12/15.
 */
public class Incident extends JSONObject{

    public Incident() throws JSONException{}

    public Incident setIncID(String value)throws JSONException{ return (Incident) this.put("IncID" , value);}
    public Incident setPostDate(String value)throws JSONException{ return (Incident) this.put("PostDate" , value);}
    public Incident setCategory(String value)throws JSONException{ return (Incident) this.put("Category" , value);}
    public Incident setType(String value)throws JSONException{ return (Incident) this.put("Type" , value);}
    public Incident setUser(String value)throws JSONException{ return (Incident) this.put("User" , value);}
    public Incident setTitle(String value)throws JSONException{ return (Incident) this.put("Title" , value);}
    public Incident setGPSLat(String value)throws JSONException{ return (Incident) this.put("GPSLat" , value);}
    public Incident setGPSLon(String value)throws JSONException{ return (Incident) this.put("GPSLon" , value);}
    public Incident setRelevance(String value)throws JSONException{ return (Incident) this.put("Relevance" , value);}

    public Object getIncID()throws JSONException{ return this.getString("IncID");}
    public Object getPostDate()throws JSONException{ return this.getString("PostDate");}
    public Object getCategory()throws JSONException{ return this.getString("Category");}
    public Object getType()throws JSONException{ return this.getString("Type");}
    public Object getUser()throws JSONException{ return this.getString("User");}
    public Object getTitle()throws JSONException{ return this.getString("Title");}
    public Object getGPSLat()throws JSONException{ return this.getDouble("GPSLat");}
    public Object getGPSLon()throws JSONException{ return super.getDouble("GPSLon");}
    public Object getRelevance()throws JSONException{ return super.getDouble("Relevance");}

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

    public Incident (String Category, String Type,  String User, String Title, String GPSLat, String GPSLon, String Relevance)
            throws JSONException {
        this.setCategory(Category);
        this.setType(Type);
        this.setUser(User);
        this.setTitle(Title);
        this.setGPSLat(GPSLat);
        this.setGPSLon(GPSLon);
        this.setRelevance(Relevance);
    }

}

