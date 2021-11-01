package com.example.odsen;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

// Klasse for å kommunisere med Firebase DB
public class DBFirebase implements IDB{
    private FirebaseFirestore storage;
    private CollectionReference collection;

    // Konstruktør, velger collection som skal brukes av instansen
    public DBFirebase(String collection) {
        storage = FirebaseFirestore.getInstance();
        this.collection = storage.collection(collection);

        Log.i(LogTags.STARTING_DB, "Instans av Firebase DB, collection " + this.collection.getId());
    }


    @Override
    public void createRoom(Room room) {
        HashMap<String, String> map = new HashMap<String, String>();

        map.put("key", "value");

        collection.add(room);
    }
}
