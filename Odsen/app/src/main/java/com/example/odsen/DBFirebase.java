package com.example.odsen;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.odsen.Model.Room;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

// Klasse for å kommunisere med Firebase DB
public class DBFirebase implements IDB{
    private FirebaseFirestore storage;
    private ArrayList<Room> rooms = new ArrayList<>();

    // Konstruktør, velger collection som skal brukes av instansen
    public DBFirebase(String user) {
        storage = FirebaseFirestore.getInstance();

        loadRooms(user);

        Log.i(LogTags.STARTING_DB, "Instans av Firebase DB, bruker " + user);
    }


    @Override
    public void createRoom(Room room) {
        HashMap<String, String> map = new HashMap<String, String>();

        storage.collection(IDB.ROOMS).add(room);
    }

    @Override
    public ArrayList<Room> getRooms(String username){
        return rooms;
    }


    private void loadRooms(String username) {

        storage.collection(IDB.ROOMS)
                .whereArrayContains("players", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.i(LogTags.LOADING_DATA, String.valueOf(task.isSuccessful()));

                if(task.isSuccessful()) {

                    Log.v(LogTags.LOADING_DATA, "Bruker med i " + String.valueOf(task.getResult().size()) + " antall rom");

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Room room = document.toObject(Room.class);

                        rooms.add(room);
                    }
                } else {
                    Log.e(LogTags.LOADING_DATA, "DBFirebase: Kan ikke hente fra DB: " + task.getException());
                }
            }

        });
        Log.v(LogTags.LOADING_DATA, String.valueOf(rooms.size()));

    }
}
