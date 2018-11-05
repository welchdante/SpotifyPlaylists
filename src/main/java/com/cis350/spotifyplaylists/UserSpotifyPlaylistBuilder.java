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

    private static final String clientId = "6ed14ff492bf439a840705e0b54e63d1";
    private static final String clientSecret = "00245d2afffd436eab7a311317eaffe3";
    private static final URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:5000/redirect");
    private static final String code = "";

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .setRedirectUri(redirectUri)
            .build();

    private static final AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code)
            .build();


    public static void main(String[] args) throws URISyntaxException {

        AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                .scope("user-read-birthdate,user-read-email")
                .show_dialog(true)
                .build();


        URI uri = authorizationCodeUriRequest.execute();

        System.out.println("URI: " + uri.toString());

        Scanner userInput = new Scanner(System.in);
        String code = userInput.nextLine();
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

//    public String getAuthorizationURL() {
//        System.out.println("Getting Authorize URL");
//        Api api = getApiBuilder().build();
//
//        // Set the necessary scopes that the application will need from the user
//        String scopes = env.getProperty("spotify.oauth.scope");
//        List<String> scopeList = Arrays.asList(scopes.split(","));
//
//        // Set a state. This is used to prevent cross site request forgeries.
//        String state = env.getProperty("spotify.oauth.state");
//
//        return api.createAuthorizeURL(scopeList, state);
//    }
}
