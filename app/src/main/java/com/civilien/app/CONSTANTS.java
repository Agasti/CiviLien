package com.civilien.app;

/**
 * Created by Sehail Fillali on 20/12/15.
 */
public class CONSTANTS {
    final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    static final String IP_ADDRESS = "41.248.185.242";
    static long UPDATE_INTERVAL = 10 * 1000; // 10 Seconds
    static long FASTEST_INTERVAL = 2000; // 2 Seconds
    static final String USERNAME = "test";


    // URLs
    static final String URL_GET_INCIDENTS = "http://"+CONSTANTS.IP_ADDRESS +"/get_all_incidents.php";
    static final String URL_CREATE_INCIDENT = "http://"+CONSTANTS.IP_ADDRESS +"/create_incident.php";
    static final String URL_LOGIN = "http://"+CONSTANTS.IP_ADDRESS+"/app_login.php";
    static final String URL_REGISTER = "http://"+CONSTANTS.IP_ADDRESS+"/app_register.php";
    static final String URL_VOTE = "http://"+CONSTANTS.IP_ADDRESS+"/app_vote.php";

    public CONSTANTS() {
    }
}
