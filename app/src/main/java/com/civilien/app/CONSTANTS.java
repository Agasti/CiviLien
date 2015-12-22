package com.civilien.app;

/**
 * Created by Sehail Fillali on 20/12/15.
 */
public class CONSTANTS {
    final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    static long UPDATE_INTERVAL = 10 * 1000; // 10 Seconds
    static long FASTEST_INTERVAL = 2000; // 2 Seconds

    static final String IP_ADDRESS = "160.177.100.39";
    static final String USERNAME = "test";


    // URLs
    static final String URL_GET_INCIDENTS = "http://"+CONSTANTS.IP_ADDRESS +"/get_all_incidents.php";
    static final String URL_CREATE_INCIDENT = "http://"+CONSTANTS.IP_ADDRESS +"/create_incident.php";

    public CONSTANTS() {
    }
}
