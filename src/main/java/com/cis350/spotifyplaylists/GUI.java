package com.cis350.spotifyplaylists;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.model_objects.specification.AlbumSimplified;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.Set;
import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;
import org.json.JSONObject;

//import static com.cis350.spotifyplaylists.SpotifySongsCollector.getAllSongs;

/*******************************************
 * GUI class that creates a user interface
 * to generate a new playlist in their
 * respective Spotify account.
 *
 * @version 11/25/18
 * @author Julia
 *******************************************/
public class GUI implements ActionListener {

    /*declare and instantiate JFrame*/
    private JFrame top = new JFrame("Spotify Roadtrip Playlist Generator");

    /*declare and instantiate JPanel*/
    private JPanel panel1 = new JPanel();
    private JPanel panel2 = new JPanel();
    private JPanel panel3top = new JPanel();
    private JPanel panel3bottom = new JPanel();
    private JPanel panel4 = new JPanel();

    /*declare and instantiate JButtons*/
    private JButton startList = new JButton("Start");
    private JButton enterPoints = new JButton("Next");
    private JButton makeList = new JButton("Make Playlist");

    /*declare and instantiate labels*/
    //CHANGE LATER welcome
    private JLabel welcomeMes = new JLabel("Welcome or logo");
    private JLabel directions2 = new JLabel("Copy and paste the URL below. "
            + "Log in to your Spotify account");
    private JLabel directions3 = new JLabel("Enter the latitude and longitude"
            +"of your starting point and destination.");
    //CHANGE LATER with better info
    private JLabel directions4 = new JLabel("Go to blah to see your playlist.");
    private JLabel URL = new JLabel();
    private JLabel startLat = new JLabel("Starting Latitude");
    private JLabel startLong = new JLabel("Starting Longitude");
    private JLabel destLat = new JLabel("Destination Latitude");
    private JLabel destLong = new JLabel("Destination Longitude");
    private JLabel travelTime = new JLabel();

    /*declare text field*/
    private JTextField typeStartLat = new JTextField();
    private JTextField typeStartLong = new JTextField();
    private JTextField typeDestinLat = new JTextField();
    private JTextField typeDestinLong = new JTextField();

    /*creates Spotify object*/
    //CHANGE LATER
    private RoadTripPlaylistBuilder playlist = new RoadTripPlaylistBuilder();

    /********************************
     Constructor that makes main page
     ********************************/
    public GUI() {

        /*change colors and size*/
        //buttons
        startList.setBackground(Color.white);
        startList.setPreferredSize(new DimensionUIResource(150, 50));
        enterPoints.setBackground(Color.white);
        enterPoints.setPreferredSize(new DimensionUIResource(150, 50));
        makeList.setBackground(Color.white);
        makeList.setPreferredSize(new DimensionUIResource(150, 50));
        //labels
        welcomeMes.setForeground(Color.white);
        directions2.setForeground(Color.white);
        directions3.setForeground(Color.white);
        directions4.setForeground(Color.white);
        travelTime.setForeground(Color.white);
        URL.setForeground(Color.white);
        startLat.setForeground(Color.white);
        startLong.setForeground(Color.white);
        destLat.setForeground(Color.white);
        destLong.setForeground(Color.white);
        //panels
        panel1.setBackground(Color.darkGray);
        panel2.setBackground(Color.darkGray);
        panel3top.setBackground(Color.darkGray);
        panel3bottom.setBackground(Color.darkGray);
        panel4.setBackground(Color.darkGray);
        //text fields
        typeStartLat.setPreferredSize(new Dimension(150, 20));
        typeStartLong.setPreferredSize(new Dimension(150, 20));
        typeDestinLat.setPreferredSize(new Dimension(150, 20));
        typeDestinLong.setPreferredSize(new Dimension(150, 20));

        /*button action listeners*/
        startList.addActionListener(this);
        enterPoints.addActionListener(this);
        makeList.addActionListener(this);

        /*getting the url for the user*/
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
        URL.setText(uri.toString());

        /*adding items for home panel*/
        panel1.add(welcomeMes, BorderLayout.NORTH);
        panel1.add(startList, BorderLayout.CENTER);

        /*finishing*/
        window1();

    }

    /************************************
     * Method that creates the opening
     * window of the GUI. Button goes
     * to next page of getting user info.
     ************************************/
    public void window1() {
        top.getContentPane().add(panel1);
        top.setSize(1000, 500);
        top.setVisible(true);
        top.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /************************************
     * Method that provides the URL and
     * directions. Button moves to next
     * page of getting user info. Clears
     * previous window.
     ************************************/
    public void window2() {

        /*clearing jframe and adding new components*/
        top.getContentPane().removeAll();
        top.getContentPane().repaint();
        top.validate();

        panel2.add(directions2, BorderLayout.NORTH);
        panel2.add(URL, BorderLayout.CENTER);
        panel2.add(enterPoints, BorderLayout.SOUTH);

        top.add(panel2);
        top.setSize(1600, 500);
        top.setVisible(true);
        top.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /************************************
     * Method gathers latitude and
     * longitude info from user. Button
     * creates playlist in user account
     * and moves to next window for a
     * confirmation. Clears previous
     * window.
     ************************************/
    public void window3() {

        /*clearing jframe and adding new components*/
        top.getContentPane().removeAll();
        top.getContentPane().repaint();
        top.validate();

        panel3top.add(directions3, BorderLayout.NORTH);
        panel3top.add(startLat, BorderLayout.CENTER);
        panel3top.add(typeStartLat, BorderLayout.EAST);
        panel3top.add(startLong, BorderLayout.CENTER);
        panel3top.add(typeStartLong, BorderLayout.EAST);
        panel3bottom.add(destLat, BorderLayout.CENTER);
        panel3bottom.add(typeDestinLat, BorderLayout.EAST);
        panel3bottom.add(destLong, BorderLayout.CENTER);
        panel3bottom.add(typeDestinLong, BorderLayout.EAST);
        panel3bottom.add(makeList, BorderLayout.SOUTH);

        top.add(panel3top, BorderLayout.NORTH);
        top.add(panel3bottom, BorderLayout.CENTER);
        top.setSize(1000, 500);
        top.setVisible(true);
        top.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /*************************************
     * Method clears window to act as a
     * confirmation that the playlist
     * has been created in user's account.
     *************************************/
    public void window4() {
        /*clearing jframe and adding new components*/
        top.getContentPane().removeAll();
        top.getContentPane().repaint();
        top.validate();

        //CHANGE LATER once we have info
        panel4.add(directions4, BorderLayout.NORTH);
        panel4.add(travelTime, BorderLayout.CENTER);

        top.add(panel4);
        top.setSize(1000, 500);
        top.setVisible(true);
        top.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /*******************************************
     Method makes action listeners for buttons.
     *******************************************/
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startList) {

            /*changes window*/
            window2();

        }
        if (e.getSource() == enterPoints) {

            /*changes window*/
            window3();

        }
        if (e.getSource() == makeList) {

            /*using lat and long from prev window*/
            builder();

            /*changes window*/
            window4();

        }
    }

    /**********************************
     * Method to take the lat and long
     * and find a route through tomtom.
     * Gives time of trip and finish
     * playlist.
     **********************************/
    public void builder() {

        //build URL for the request
        TomTomCollector tomTomCollector = new TomTomCollector();
        String url = tomTomCollector.buildRequestUrl(typeStartLat.getText(), typeStartLong.getText(),
                typeDestinLat.getText(), typeDestinLong.getText());

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
        travelTime.setText("Your trip will take " + travelTimeInSeconds + " seconds." +
                " Your playlist is the following...");

        //build the playlist
        SpotifySongsCollector songCollector = new SpotifySongsCollector();
        songCollector.authenticateCredentials(songCollector.spotifyApi);
        Set<AlbumSimplified> songs = songCollector.getAllSongs();
        Set<AlbumSimplified> playlist = songCollector.buildPlaylist(songs, 0, travelTimeInSeconds);
        System.out.println("Your playlist is:");
        for (AlbumSimplified p : playlist) {
            System.out.println(p.getName());
        }

    }

    public static void main(String[] args) {
        new GUI();
    }

}
