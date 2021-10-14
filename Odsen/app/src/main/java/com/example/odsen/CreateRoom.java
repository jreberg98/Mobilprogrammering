package com.example.odsen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class CreateRoom extends AppCompatActivity {

    // Input felt
    private EditText name;
    private EditText addFriends;
    private EditText addChallenge;
    private RadioButton radioChallenge;
    private RadioButton radioTime;
    private Button submit;

    // Data som skal til database
    private boolean winAfterTime = true;
    private ArrayList<String> friends = new ArrayList<>();
    private ArrayList<Challenge> challenges = new ArrayList<>();
    private Date endDate;
    private int endChallenges;

    // UI element som oppdateres for brukeren
    private LinearLayout friendHolder;
    private LinearLayout challengeHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);

        name = findViewById(R.id.CREATE_name_of_room);
        addFriends = findViewById(R.id.CREATE_add_friends);
        addChallenge = findViewById(R.id.CREATE_add_challenge);
        friendHolder = findViewById(R.id.CREATE_friends_holder);
        challengeHolder = findViewById(R.id.CREATE_challenge_holder);
        radioChallenge = findViewById(R.id.CREATE_radio_challenges);
        radioTime = findViewById(R.id.CREATE_radio_time);
        submit = findViewById(R.id.CREATE_submit_room);

        // Hånterer radio valgene
        radioTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioButtonClick(view);
            }
        });
        radioChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioButtonClick(view);
            }
        });

        // Sjekker innput, lager rommet med createRoom()
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO: Input validering på andre felt på skjermen

                createRoom();
            }
        });

        // Håndtering av å legge til venner
        addFriends.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                // TODO: Input validering, sjekke om vennen finnes

                String friend = textView.getText().toString();

                // Legger vennen til i lista
                friends.add(friend);


                // Lager en venn
                TextView tempFriend = new TextView(friendHolder.getContext());

                tempFriend.setText(friend);
                tempFriend.setId(friends.size());

                friendHolder.addView(tempFriend);

                return true;

            }
        });

        // Hådterer å legge til utfordring
        addChallenge.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                // TODO: Input validering
                String challenge = textView.getText().toString();

                // Legger til i lista
                Challenge tempChallenge = new Challenge(challenge);
                challenges.add(tempChallenge);

                TextView tempChallengeView = new TextView(challengeHolder.getContext());

                tempChallengeView.setText(challenge);
                tempChallengeView.setId(challenges.size());

                return true;
            }
        });

    }

    // Sjekker verdien på radioknapp, og oppdaterer variablene
    private void radioButtonClick(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        Log.d("TAG", "radioButtonClick: ");
        switch (view.getId()) {
            case R.id.CREATE_radio_challenges:
                if (checked)
                    winAfterTime = false;
                    break;
            case R.id.CREATE_radio_time:
                if (checked)
                    winAfterTime = true;
                    break;
            default:
                Log.d("CREATE ROOM", "Unexpected value on radio button, create rom");
        }
        Log.d("T", String.valueOf(winAfterTime));
    }

    // Lager rommet, og sender til DB
    private boolean createRoom(){
        Room room = null;
        if (winAfterTime) {
            room = new Room(name.getText().toString(), friends, endDate, challenges);
        } else {
            room = new Room(name.getText().toString(), friends, endChallenges, challenges);
        }

        // TODO: Sende rommet til DB

        return false;
    }
}