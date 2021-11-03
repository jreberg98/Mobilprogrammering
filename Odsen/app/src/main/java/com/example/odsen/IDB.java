package com.example.odsen;


import java.util.ArrayList;

// Eget interface for komunikasjon med DB
// Blir da lettere å eventuelt bytte DB seinere
public interface IDB {

    String ROOMS = "ROOMS";
    String USERS = "USERS";

    // Insetting av data
    void createRoom(Room room);

    // Henting av data
    ArrayList<Room> getRooms(String username);

}
