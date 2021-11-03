package com.example.odsen;

import java.util.ArrayList;

public class Player {
    private String UID;
    private String Identifier;
    private String displayName;
    private ArrayList<String> friends;

    public Player(){

    }

    public Player(String UID, String identifier, String displayName) {
        this.UID = UID;
        Identifier = identifier;
        this.displayName = displayName;
    }

    public String getUID() {
        return UID;
    }
    public void setUID(String UID) {
        this.UID = UID;
    }
    public String getIdentifier() {
        return Identifier;
    }
    public void setIdentifier(String identifier) {
        Identifier = identifier;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public ArrayList<String> getFriends() {
        return friends;
    }
    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }
}
