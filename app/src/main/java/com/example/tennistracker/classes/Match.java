package com.example.tennistracker.classes;

public class Match {

    public String id;
    public String player1;
    public String player2;
    public String player1_score;
    public String player2_score;

    public String locations;

    public Match(String id, String player1, String player2, String player1_score, String player2_score, String locations) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
        this.player1_score = player1_score;
        this.player2_score = player2_score;
        this.locations = locations;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public String getPlayer1_score() {
        return player1_score;
    }

    public String getPlayer2_score() {
        return player2_score;
    }

    public String getLocations() {
        return locations;
    }

}