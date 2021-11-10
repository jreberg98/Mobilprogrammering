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

    // Sjekker om en brukeren er venn med en annen bruker
    public boolean isFriendsWith(String friendsName) {
        for (String friend : friends) {
            if (friend.equals(friendsName)){
                return true;
            }
        }

        return false;
    }
    // Sjekker om en bruker har et forhold til andre burkere
    public boolean hasRelationWith(String user) {
        for (String friend : friends) {
            if (friend.equals(user)) {
                return true;
            }
        }
        for (String friend : pendingRequests) {
            if (friend.equals(user)) {
                return true;
            }
        }
        for (String friend : friendRequests) {
            if (friend.equals(user)) {
                return true;
            }
        }
        // TODO: Legge til blokerte brukere
        // TODO: exception for å vite hvaslags forhold man har?
        return false;
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
