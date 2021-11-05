package com.example.odsen;

public class PlayerInRoom extends Player implements Comparable<PlayerInRoom> {
    private int challengesCompleted = 0;

    public PlayerInRoom() {
        super();
    }

    public int getChallengesCompleted(){
        return challengesCompleted;
    }

    public void addChallenge(){
        challengesCompleted++;
    }

    @Override
    public int compareTo(PlayerInRoom playerInRoom) {
        if (this.challengesCompleted == playerInRoom.getChallengesCompleted()) {
            return 0;
        } else {
            return this.challengesCompleted - playerInRoom.challengesCompleted;
        }
    }
}
