package com.example.odsen.Model;

import java.util.ArrayList;

public class Player {
    private String UID;
    private String Identifier;
    private String displayName;
    private ArrayList<String> friends = new ArrayList<>();
    private ArrayList<String> pendingRequests = new ArrayList<>();
    private ArrayList<String> friendRequests = new ArrayList<>();

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
    public ArrayList<String> getPendingRequests() {
        return pendingRequests;
    }
    public void setPendingRequests(ArrayList<String> pendingRequests) {
        this.pendingRequests = pendingRequests;
    }
    public ArrayList<String> getFriendRequests() {
        return friendRequests;
    }
    public void setFriendRequests(ArrayList<String> friendRequests) {
        this.friendRequests = friendRequests;
    }
}
