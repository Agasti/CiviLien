package com.civilien.app;

import org.json.JSONException;

/**
 * Created by SehailFillali on 6/12/15.
 */
public class IncidentExamples {
        Incident one1;
        Incident two2;
        Incident three3;

        public IncidentExamples() throws JSONException {
                one1 = new Incident("1","date1","cat1", "type1", "user1","title1","34.1","-6.91","1");
                two2 = new Incident("2", "date2","cat2", "type2", "user2","title2","34.2","-6.92","1");
                three3 = new Incident("3","date3", "cat3", "type3", "user3","title3","34.3","-6.93","1");
        }
}
