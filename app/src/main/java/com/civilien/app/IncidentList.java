package com.civilien.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class IncidentList extends JSONArray implements Serializable {

    public ArrayList getArrayList () {
        ArrayList<String> listData = new ArrayList<>();
        for (int i = 0 ; i < this.length() ; i++){
            try {
                listData.add(this.get(i).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return listData;
    }

    public boolean isEmpty () { return this.length() == 0;}

    @Override
    public JSONObject getJSONObject(int index) throws JSONException {
        return super.getJSONObject(index);
    }

    public IncidentList() {
        super();
    }

    public IncidentList(Collection copyFrom) {
        super(copyFrom);
    }

    public IncidentList(JSONTokener readFrom) throws JSONException {
        super(readFrom);
    }

    public IncidentList(String json) throws JSONException {
        super(json);
    }

    public IncidentList(Object array) throws JSONException {
        super(array);
    }

    @Override
    public int length() {
        return super.length();
    }

    @Override
    public JSONArray put(boolean value) {
        return super.put(value);
    }

    @Override
    public JSONArray put(double value) throws JSONException {
        return super.put(value);
    }

    @Override
    public JSONArray put(int value) {
        return super.put(value);
    }

    @Override
    public JSONArray put(long value) {
        return super.put(value);
    }

    @Override
    public JSONArray put(Object value) {
        return super.put(value);
    }

    @Override
    public JSONArray put(int index, boolean value) throws JSONException {
        return super.put(index, value);
    }

    @Override
    public JSONArray put(int index, double value) throws JSONException {
        return super.put(index, value);
    }

    @Override
    public JSONArray put(int index, int value) throws JSONException {
        return super.put(index, value);
    }

    @Override
    public JSONArray put(int index, long value) throws JSONException {
        return super.put(index, value);
    }

    @Override
    public JSONArray put(int index, Object value) throws JSONException {
        return super.put(index, value);
    }

    @Override
    public boolean isNull(int index) {
        return super.isNull(index);
    }

    @Override
    public Object get(int index) throws JSONException {
        return super.get(index);
    }

    @Override
    public Object opt(int index) {
        return super.opt(index);
    }

    @Override
    public Object remove(int index) {
        return super.remove(index);
    }

    @Override
    public boolean getBoolean(int index) throws JSONException {
        return super.getBoolean(index);
    }

    @Override
    public boolean optBoolean(int index) {
        return super.optBoolean(index);
    }

    @Override
    public boolean optBoolean(int index, boolean fallback) {
        return super.optBoolean(index, fallback);
    }

    @Override
    public double getDouble(int index) throws JSONException {
        return super.getDouble(index);
    }

    @Override
    public double optDouble(int index) {
        return super.optDouble(index);
    }

    @Override
    public double optDouble(int index, double fallback) {
        return super.optDouble(index, fallback);
    }

    @Override
    public int getInt(int index) throws JSONException {
        return super.getInt(index);
    }

    @Override
    public int optInt(int index) {
        return super.optInt(index);
    }

    @Override
    public int optInt(int index, int fallback) {
        return super.optInt(index, fallback);
    }

    @Override
    public long getLong(int index) throws JSONException {
        return super.getLong(index);
    }

    @Override
    public long optLong(int index) {
        return super.optLong(index);
    }

    @Override
    public long optLong(int index, long fallback) {
        return super.optLong(index, fallback);
    }

    @Override
    public String getString(int index) throws JSONException {
        return super.getString(index);
    }

    @Override
    public String optString(int index) {
        return super.optString(index);
    }

    @Override
    public String optString(int index, String fallback) {
        return super.optString(index, fallback);
    }

    @Override
    public JSONArray getJSONArray(int index) throws JSONException {
        return super.getJSONArray(index);
    }

    @Override
    public JSONArray optJSONArray(int index) {
        return super.optJSONArray(index);
    }

    @Override
    public JSONObject optJSONObject(int index) {
        return super.optJSONObject(index);
    }

    @Override
    public JSONObject toJSONObject(JSONArray names) throws JSONException {
        return super.toJSONObject(names);
    }

    @Override
    public String join(String separator) throws JSONException {
        return super.join(separator);
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
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
