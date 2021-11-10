package com.example.odsen.Model;

import android.util.Log;

import com.example.odsen.Tags.LogTags;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

public class Room {
    private String name;
    private ArrayList<String> players;
    private GregorianCalendar endDate;
    private int endNumberOfChallenges;
    private ArrayList<Challenge> challenges;


    // To konstruktører, en for når rom slutter på tid og en for antall utfordringer
    public Room(String name, ArrayList<String> players, GregorianCalendar endDate, ArrayList<Challenge> challenges) {
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
    public GregorianCalendar getEndDate() {
        return endDate;
    }
    public int getEndNumberOfChallenges() {
        return endNumberOfChallenges;
    }
    public ArrayList<Challenge> getChallenges() {
        return challenges;
    }

    public Room() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlayers(ArrayList<String> players) {
        this.players = players;
    }

    public void setEndDate(GregorianCalendar endDate) {
        this.endDate = endDate;
    }

    public void setEndNumberOfChallenges(int endNumberOfChallenges) {
        this.endNumberOfChallenges = endNumberOfChallenges;
    }

    public void setChallenges(ArrayList<Challenge> challenges) {
        this.challenges = challenges;
    }

    public Challenge complete(String challengeText, String user){
        int index = indexOfChallenge(challengeText);

        challenges.get(index).complete(user);

        return challenges.get(index);
    }

    private int indexOfChallenge(String challengeText){
        for (int i = 0; i < challenges.size(); i++) {
            if (challenges.get(i).getText().equals(challengeText)){
                return i;
            }
        }
        // Finner ikke elementet
        Log.e(LogTags.ILLEGAL_INPUT, "Room: IndexOfChallenge: challenge ikke i rommet");
        return -1;
    }
}
