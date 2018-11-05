package com.cis350.spotifyplaylists;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;

import java.util.Scanner;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class UserSpotifyPlaylistBuilder {

    private static final String clientId = "6ed14ff492bf439a840705e0b54e63d1";
    private static final String clientSecret = "00245d2afffd436eab7a311317eaffe3";

    private static final URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:5000/redirect");
    private static final String code = "";

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .setRedirectUri(redirectUri)
            .build();
//
//    private final AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code)
//            .build();


//    private static final GetListOfCurrentUsersPlaylistsRequest getListOfCurrentUsersPlaylistsRequest = spotifyApi
//            .getListOfCurrentUsersPlaylists()
//            .limit(10)
//            .offset(0)
//            .build();


    public static void main(String[] args) throws URISyntaxException {

        AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                .scope("user-read-birthdate, user-read-email")
                .show_dialog(true)
                .build();


        URI uri = authorizationCodeUriRequest.execute();

        System.out.println("URI: " + uri.toString());

        Scanner userInput = new Scanner(System.in);
        String code = userInput.nextLine();
        userInput.close();
        System.out.println(code);


        // authorizes the user
        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code)
                .build();

        try {
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

            // Set access and refresh token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

            System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // get list of current user's playlists
        GetListOfCurrentUsersPlaylistsRequest getListOfCurrentUsersPlaylistsRequest = spotifyApi
                .getListOfCurrentUsersPlaylists()
                .limit(10)
                .offset(0)
                .build();

        try {
            final Paging<PlaylistSimplified> playlistSimplifiedPaging = getListOfCurrentUsersPlaylistsRequest.execute();

            System.out.println("Total: " + playlistSimplifiedPaging.getTotal());
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }
}
