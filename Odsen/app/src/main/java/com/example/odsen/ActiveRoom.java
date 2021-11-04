package com.example.odsen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ActiveRoom extends AppCompatActivity {

    // Views
    private TextView title;

    // Data
    private Room room;

    // Eksternt
    private FirebaseFirestore storage;
    private FirebaseUser user;
    private Player player;
    String document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_room);

        storage = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        loadPlayer();
        loadRoom();

        title = findViewById(R.id.ACTIVE_ROOM_title);
    }



    private void loadPlayer() {
        storage.collection(IDB.USERS)
                .document(user.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            player = task.getResult().toObject(Player.class);
                        } else {
                            Log.e(LogTags.LOADING_DATA, "ActiveRoom: loadPlayer " + task.getException());
                        }
                    }
                });
    }

    private void loadRoom() {
        String key = getIntent().getStringExtra(OpenRoomsList.ROOM_KEY);
        storage.collection(IDB.ROOMS).document(key).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    room = task.getResult().toObject(Room.class);
                    // TODO: Slett logtag, bare for debug
                    Log.i(LogTags.LOADING_DATA, "ActiveRoom: lasta inn rom " + key);

                    updateUI();
                }
            }
        });
    }

    private void updateUI() {
        String titleText = getString(R.string.ACTIVE_ROOM_title, room.getName());
        title.setText(titleText);
    }
}