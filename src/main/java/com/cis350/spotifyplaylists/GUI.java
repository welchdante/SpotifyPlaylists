package com.cis350.spotifyplaylists;

import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;

public class GUI {

    /*declare JFrame*/
    private JFrame top;

    /*declare JPanel*/
    private JPanel panel;

    /*declare JButtons*/
    private JButton newTrip;
    private JButton currTrip;
    private JButton makePlaylist;

    /*declare labels*/
    private JLabel directions;
    private JLabel logo;
    private JLabel start;
    private JLabel destination;

    /*declare text field*/
    private JTextField typeStart;
    private JTextField typeDestin;

    /*creates Spotify object*/
    private UserSpotifyPlaylistBuilder playlist;

    public GUI() {

        /*instantiate playlist*/
        playlist = new UserSpotifyPlaylistBuilder();

        /*instantiate JFrame*/
        top = new JFrame("Spotify Roadtrip Playlist Generator");

        /*instantiate JPanel*/
        panel = new JPanel();

        /*instantiate buttons*/
        newTrip = new JButton("New Trip");
        newTrip.setPreferredSize(new DimensionUIResource(300,100));
        currTrip = new JButton("Current Trip");
        currTrip.setPreferredSize(new DimensionUIResource(300,100));
        makePlaylist = new JButton("Make My Playlist");
        makePlaylist.setPreferredSize(new DimensionUIResource(300,100));

        /*instantiate labels*/
        //CHANGE THIS
        directions = new JLabel("Enter your locations in the form of ");
        logo = new JLabel();
        start = new JLabel("Starting Point");
        destination = new JLabel("Destination");

        /*instantiate text fields*/
        typeStart = new JTextField();
        typeDestin = new JTextField();

        /*button actions listeners*/
        /*newTrip.addActionListener(this);
        currTrip.addActionListener(this);
        makePlaylist.addActionListener(this);*/

        /*adding items*/
        panel.add(logo);
        panel.add(newTrip);
        panel.add(currTrip);

        /*finishing*/
        top.getContentPane().add(panel);
        top.setSize(700,500);
        top.setVisible(true);
        top.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newTrip) {

        }
        if (e.getSource() == currTrip) {

        }
        if (e.getSource() == makePlaylist) {

        }
    }

    public static void main(String[] args) {
        new GUI();
    }

}
