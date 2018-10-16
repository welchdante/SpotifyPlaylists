package com.cis350.spotifyplaylists;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import java.util.Scanner;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class UserSpotifyPlaylistBuilder {

    public static void main(String[] args) throws URISyntaxException {

        String clientId = "6ed14ff492bf439a840705e0b54e63d1";
        String clientSecret = "00245d2afffd436eab7a311317eaffe3";
        URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:5000");

        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(redirectUri)
                .build();

        AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                .state("x4xkmn9pu3j6ukrs8n")
                .scope("user-read-birthdate,user-read-email")
                .show_dialog(true)
                .build();


        URI uri = authorizationCodeUriRequest.execute();

        System.out.println("URI: " + uri.toString());


        /****************************/

        Scanner userInput = new Scanner(System.in);
        String code = userInput.next();
        userInput.close();
        System.out.println(code);

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

    }
}
