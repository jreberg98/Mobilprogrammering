package com.example.odsen;


// Eget interface for komunikasjon med DB
// Blir da lettere å eventuelt bytte DB seinere
public interface IDB {

    public void createRoom(Room room);

}
