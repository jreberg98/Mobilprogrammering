package com.example.odsen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class ActiveRoom extends AppCompatActivity {

    // Views
    private TextView title;
    private LinearLayout challengesHolder;
    private LinearLayout leaderboardHolder;


    // Data
    private Room room;
    private Player player;

    // Eksternt
    private FirebaseFirestore storage;
    private FirebaseUser user;
    String document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_room);

        storage = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        document = getIntent().getStringExtra(OpenRoomsList.ROOM_KEY);

        loadPlayer();
        loadRoom();

        title = findViewById(R.id.ACTIVE_ROOM_title);
        challengesHolder = findViewById(R.id.ACTIVE_ROOM_challenges_holder);
        leaderboardHolder = findViewById(R.id.ACTIVE_ROOM_leaderboard_holder);
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
        storage.collection(IDB.ROOMS)
                .document(document)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            room = task.getResult().toObject(Room.class);
                            // TODO: Slett logtag, bare for debug
                            Log.i(LogTags.LOADING_DATA, "ActiveRoom: lasta inn rom " + document);

                            updateUI();
                        }
                    }
                });
    }

    private void updateUI() {
        // Tittel
        String titleText = getString(R.string.ACTIVE_ROOM_title, room.getName());
        title.setText(titleText);

        // Utfordringer
        for (Challenge challenge : room.getChallenges()) {
            TextView textView = new TextView(getApplicationContext());
            textView.setText(challenge.getText());
            textView.setGravity(ViewGroup.TEXT_ALIGNMENT_GRAVITY);

            // Røde utfordringer er de som gjennstår
            // TODO: bruke andre custom farger
            if (challenge.getCompletedBy() == null) {
                // textView.setTextColor(R.color.challenge_done); // Gir en blå ish farge, skal være rød
                textView.setTextColor(Color.RED);
                setCompleteChallenge(textView);
            } else {
                textView.setTextColor(Color.BLACK);
            }

            challengesHolder.addView(textView);
        }
    }

    private void setCompleteChallenge (View view){
        // TODO: implementer meg

        TextView textView = (TextView) view;
        String challengeText = (String) textView.getText();

        // int index = room.indexOfChallenge(challengeText);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Challenge temp = new Challenge();
                temp.setText(challengeText);
                storage.collection(IDB.ROOMS).document(document).update("challenges", FieldValue.arrayRemove(temp));

                Challenge challenge = room.complete(challengeText, player.getIdentifier());

                storage.collection(IDB.ROOMS).document(document).update("challenges", FieldValue.arrayUnion(challenge));
            }
        });
    }
}