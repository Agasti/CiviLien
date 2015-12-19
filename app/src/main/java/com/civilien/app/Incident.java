package com.civilien.app;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sehail Fillali on 5/12/15.
 */
public class Incident extends JSONObject{

    public Incident(){}

    public Incident setIncID(String value)throws JSONException{ return (Incident) this.put("IncID" , value);}
    public Incident setPostDate(String value)throws JSONException{ return (Incident) this.put("PostDate" , value);}
    public Incident setCategory(String value)throws JSONException{ return (Incident) this.put("Category" , value);}
    public Incident setType(String value)throws JSONException{ return (Incident) this.put("Type" , value);}
    public Incident setUser(String value)throws JSONException{ return (Incident) this.put("User" , value);}
    public Incident setTitle(String value)throws JSONException{ return (Incident) this.put("Title" , value);}
    public Incident setGpsLat(double value)throws JSONException{ return (Incident) this.put("GpsLat" , value);}
    public Incident setGpsLon(double value)throws JSONException{ return (Incident) this.put("GpsLon" , value);}
    public Incident setRelevance(double value)throws JSONException{ return (Incident) this.put("Relevance" , value);}

    public Object getIncID()throws JSONException{ return this.getString("IncID");}
    public Object getPostDate()throws JSONException{ return this.getString("PostDate");}
    public Object getCategory()throws JSONException{ return this.getString("Category");}
    public Object getType()throws JSONException{ return this.getString("Type");}
    public Object getUser()throws JSONException{ return this.getString("User");}
    public Object getTitle()throws JSONException{ return this.getString("Title");}
    public Object getGpsLat()throws JSONException{ return this.getDouble("GpsLat");}
    public Object getGpsLon()throws JSONException{ return super.getDouble("GpsLon");}
    public Object getRelevance()throws JSONException{ return super.getDouble("Relevance");}

    public Incident (String IncID, String PostDate, String Category, String Type,  String User, String Title, double GpsLat, double GpsLon, double Relevance)
    throws JSONException {
        this.setIncID(IncID);
        this.setPostDate(PostDate);
        this.setCategory(Category);
        this.setType(Type);
        this.setUser(User);
        this.setTitle(Title);
        this.setGpsLat(GpsLat);
        this.setGpsLon(GpsLon);
        this.setRelevance(Relevance);
    }
}

