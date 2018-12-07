package com.cis350.spotifyplaylists;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Collects data from the TomTom Maps API.
 *
 * @author Dante Welch
 * @version 1.0
 */
class TomTomCollector {

    /**
     * The base URL to send to the TomTom API.
     */
    String baseUrl = "https://api.tomtom.com/routing/1/calculateRoute/";

    /**
     * The API key for authentication with the TomTom API
     */
    String key = "4yIZHrtDC65WvrfagUUmXAQQiDVAqqre";


    /**
     * Gets the distance of a specified trip.
     *
     * @param jsonObject A JSON object that was retrieved from the TomTom API
     * @return Integer specifying the travel time of a trip in seconds.
     */
    public int getTripDistance(JSONObject jsonObject) {
        JSONArray routes = (JSONArray) jsonObject.get("routes");
        JSONObject firstRoute = (JSONObject) routes.get(0);
        JSONObject summary = (JSONObject) firstRoute.get("summary");
        return (int) summary.get("travelTimeInSeconds");
    }


    /**
     * Builds the request URL for use with the TomTom API to create a road trip from one set of coordinates to another.
     *
     * @param startingLatitude  The startingLatitude of the trip.
     * @param startingLongitude The startingLongitude of the trip.
     * @param endingLatitude    The endingLatitude of the trip.
     * @param endingLongitude   The endingLongitude of the trip.
     * @return The url for use with the specified trip to be sent to the TomTom API.
     */
    public String buildRequestUrl(String startingLatitude, String startingLongitude, String endingLatitude, String endingLongitude) {
        return baseUrl + startingLatitude + "," + startingLongitude + ":"
                + endingLatitude + "," + endingLongitude
                + "/json?routeType=fastest&avoid=tollRoads&key=" + key;
    }

}
