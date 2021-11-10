package com.example.odsen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.odsen.Model.Challenge;
import com.example.odsen.Model.Player;
import com.example.odsen.Model.PlayerInRoom;
import com.example.odsen.Model.Room;
import com.example.odsen.Tags.DBTags;
import com.example.odsen.Tags.LogTags;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;

public class ActiveRoom extends AppCompatActivity {

    // Views
    private TextView title;
    private LinearLayout challengesHolder;
    private LinearLayout leaderboardHolder;
    private Button endRoom;

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
        endRoom = findViewById(R.id.ACTIVE_ROOM_end_room);


        // TODO: Sjekke om spiller har laget rommet / har rettigheter til å avlsutte rommet
        endRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(LogTags.ANY_INPUT, "ActiveRoom: avslutter rommet " + room.getName() + " før det egentlig er ferdig");

                /*  Firestore har ikke noen funksjonalitet for å flytte dokumenter, og må
                 *  da eventuelt skrive det til ny lokasjon og deretter slette det orginale.
                 *  Da er det bedre å bare markere at rommet er ferdig tenker jeg.
                 */

                storage.collection(DBTags.ROOM).document(document).update("completed", true);
            }
        });
    }



    private void loadPlayer() {
        storage.collection(DBTags.USER)
            .document(user.getEmail())
            .get()
            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    Log.i(LogTags.USER_LOGGING, "ActiveRoom: loadPlayer: " + task.getResult().getId());
                    if (task.isSuccessful()) {
                        player = task.getResult().toObject(Player.class);
                    } else {
                        Log.e(LogTags.LOADING_DATA, "ActiveRoom: loadPlayer " + task.getException());
                    }
                }
            });
    }

    private void loadRoom() {
        storage.collection(DBTags.ROOM)
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
        challengesHolder.removeAllViewsInLayout();
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

            loadLeaderboard();
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
                // TODO: Funker ikke å fjerne ?
                storage.collection(DBTags.ROOM).document(document).update(DBTags.ROOM_CHALLENGES, FieldValue.arrayRemove(temp));

                Challenge challenge = room.complete(challengeText, player.getIdentifier());

                storage.collection(DBTags.ROOM).document(document).update(DBTags.ROOM_CHALLENGES, FieldValue.arrayUnion(challenge));

                // TODO: bare oppdatere challenges
                updateUI();
            }
        });
    }

    private void loadLeaderboard() {

        // Lager liste med alle spillerne i rommet
        ArrayList<PlayerInRoom> players = new ArrayList<>();
        for (String p : room.getPlayers()) {
            PlayerInRoom tempPlayer = new PlayerInRoom();

            tempPlayer.setIdentifier(p);
            // TODO: Hente rett display name
            tempPlayer.setDisplayName(p);

            players.add(tempPlayer);
        }

        // Går gjennom lista med alle challenges
        for (Challenge challenge : room.getChallenges()) {
            // Challenge er fullført
            if (challenge.getCompletedBy() != null) {
                // Sjekker alle brukere i rommet
                for (PlayerInRoom playerInRoom : players) {
                    // Rett bruker, oppdaterer da teller
                    if (playerInRoom.getIdentifier().equals(challenge.getCompletedBy())){
                        playerInRoom.addChallenge();
                    }
                }
            }
        }

        // Sorterer lista, den som leder øverst
        Collections.sort(players);
        Collections.reverse(players);

        // Skriver ut brukerne
        leaderboardHolder.removeAllViewsInLayout();
        for (int i = 0; i < players.size(); i++){

            TextView textView = new TextView(getApplicationContext());
            String text = players.get(i).getIdentifier() + " - " + players.get(i).getChallengesCompleted();
            textView.setText(text);
            textView.setId(i);
            textView.setGravity(ViewGroup.TEXT_ALIGNMENT_GRAVITY);

            leaderboardHolder.addView(textView);
        }
    }
}