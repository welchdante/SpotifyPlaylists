package com.cis350.spotifyplaylists;

import org.json.JSONArray;
import org.json.JSONObject;

public class TomTomCollector {

    String baseUrl = "https://api.tomtom.com/routing/1/calculateRoute/";
    String key = "4yIZHrtDC65WvrfagUUmXAQQiDVAqqre";

    public int getTripDistance(JSONObject jsonObject) {
        JSONArray routes = (JSONArray)jsonObject.get("routes");
        JSONObject firstRoute = (JSONObject)routes.get(0);
        JSONObject summary = (JSONObject)firstRoute.get("summary");
        int travelTimeInSeconds = (int)summary.get("travelTimeInSeconds");
        return travelTimeInSeconds;
    }

    public String buildRequestUrl(String startingLatitude, String startingLongitude, String endingLatitude, String endingLongitude) {
        String url = baseUrl + startingLatitude + "," + startingLongitude + ":"
                             + endingLatitude + "," + endingLongitude + "/json?routeType=fastest&avoid=tollRoads&key=" + key;
        return url;
    }

}
