package com.civilien.app;

/**
 * Created by Sehail Fillali on 5/12/15.
 */
public class Incident {

    String Category, Type, User, PostTitle;
    int GpsLat, GpsLon;

    public Incident(){}

    public void setCategory(String value){
        this.Category = value;
    }

    public void setType(String value){
        this.Type = value;
    }

    public void setUser(String value){
        this.User = value;
    }

    public void setPostTitle(String value){
        this.PostTitle = value;
    }

    public void setGpsLat(int value){
        this.GpsLat = value;
    }

    public void setGpsLon(int value){
        this.GpsLon = value;
    }

    public Incident (String Cat, String Typ,  String Use, String PoTi, int GpsLt, int GpsLn){
        setCategory(Cat);
        setType(Typ);
        setUser(Use);
        setPostTitle(PoTi);
        setGpsLat(GpsLt);
        setGpsLon(GpsLn);

    }

    public String printOut() {
        return this.Category+this.Type+this.User+this.PostTitle+this.GpsLat+this.GpsLon;
    }
}

