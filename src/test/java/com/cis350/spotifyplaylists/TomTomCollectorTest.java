package com.cis350.spotifyplaylists;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TomTomCollectorTest {
    TomTomCollector testTomTomCollector = new TomTomCollector();

    @Test
    void getTripDistance() {

        JSONObject testSummary = new JSONObject();
        testSummary.put("travelTimeInSeconds", 100);

        JSONObject testFirstRoute = new JSONObject();
        testFirstRoute.put("summary", testSummary);

        JSONArray testRoutes = new JSONArray();
        testRoutes.put(testFirstRoute);

        JSONObject testResponse = new JSONObject();
        testResponse.put("routes", testRoutes);

        int actual = testTomTomCollector.getTripDistance(testResponse);
        int expected = 100;
        assertEquals(expected, actual, "Result should be 100");
    }

    @Test
    void buildRequestUrl() {

        String startingLatitude = "52";
        String startingLongitude = "12";
        String endingLatitude = "53";
        String endingLongitude = "13";

        String actual = testTomTomCollector.buildRequestUrl(startingLatitude,
                                                            startingLongitude,
                                                            endingLatitude,
                                                            endingLongitude);

        String expected = testTomTomCollector.baseUrl + startingLatitude + ","
                          + startingLongitude + ":"
                          + endingLatitude + "," + endingLongitude
                          + "/json?routeType=fastest&avoid=tollRoads&key="
                          + testTomTomCollector.key;

        assertEquals(expected, actual, "The strings should be the same");
    }

}