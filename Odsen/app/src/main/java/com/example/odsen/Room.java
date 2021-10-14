package com.example.odsen;

import java.util.ArrayList;
import java.util.Date;

public class Room {
    private String name;
    private ArrayList<String> players;
    private Date endDate;
    private int endNumberOfChallenges;
    private ArrayList<Challenge> challenges;


    // To konstruktører, en for når rom slutter på tid og en for antall utfordringer
    public Room(String name, ArrayList<String> players, Date endDate, ArrayList<Challenge> challenges) {
        this.name = name;
        this.players = players;
        this.endDate = endDate;
        this.challenges = challenges;
    }
    public Room(String name, ArrayList<String> players, int endNumberOfChallenges, ArrayList<Challenge> challenges) {
        this.name = name;
        this.players = players;
        this.endNumberOfChallenges = endNumberOfChallenges;
        this.challenges = challenges;
    }



    public String getName() {
        return name;
    }
    public ArrayList<String> getPlayers() {
        return players;
    }
    public Date getEndDate() {
        return endDate;
    }
    public int getEndNumberOfChallenges() {
        return endNumberOfChallenges;
    }
    public ArrayList<Challenge> getChallenges() {
        return challenges;
    }
}
