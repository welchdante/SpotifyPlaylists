package com.cis350.spotifyplaylists;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.AlbumSimplified;
import com.wrapper.spotify.model_objects.specification.User;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;


public class RoadTripPlaylistBuilder {

    public static void main(String[] args) {
        PromptHelpers.greeting();

        //create new object
        UserSpotifyPlaylistBuilder userSpotifyPlaylistBuilder = new UserSpotifyPlaylistBuilder();
        String uri = userSpotifyPlaylistBuilder.getAuthenticationURI();
        System.out.println("URI: " + uri);
        String code = userSpotifyPlaylistBuilder.getUserAuthenticationCode();
        SpotifyApi spotifyApi = userSpotifyPlaylistBuilder.authenticateAccount(code);


        HashMap coordinates = PromptHelpers.promptUserForCoordinates();

        //build URL for the request
        TomTomCollector tomTomCollector = new TomTomCollector();
        String url = tomTomCollector.buildRequestUrl((String) coordinates.get("startingLatitude"),
                (String) coordinates.get("startingLongitude"),
                (String) coordinates.get("endingLatitude"),
                (String) coordinates.get("endingLongitude"));

        //make request object and make request
        EasyHTTPRequest easyHTTPRequest = new EasyHTTPRequest();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = easyHTTPRequest.sendGet(url);
            System.out.println(jsonObject);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        int travelTimeInSeconds = tomTomCollector.getTripDistance(jsonObject);
        System.out.println("Your trip will take: " + travelTimeInSeconds + " seconds.");

        //build the playlist
        SpotifySongsCollector songCollector = new SpotifySongsCollector();
        songCollector.authenticateCredentials(songCollector.spotifyApi);
        Set<AlbumSimplified> songs = songCollector.getAllSongs();
        Set<AlbumSimplified> playlist = songCollector.buildPlaylist(songs, 0, travelTimeInSeconds);
        System.out.println("Your playlist is:");
        for (AlbumSimplified p : playlist) {
            System.out.println(p.getName());
        }

        System.out.println("Let's add that playlist to your user account!");
        String playlistName = PromptHelpers.prompUserForPlaylistName();
        String displayName = userSpotifyPlaylistBuilder.getUsername();
        userSpotifyPlaylistBuilder.createPlaylist(displayName, playlistName);

    }

}