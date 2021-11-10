package com.example.odsen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.odsen.Model.Challenge;
import com.example.odsen.Model.Player;
import com.example.odsen.Model.Room;
import com.example.odsen.Tags.DBTags;
import com.example.odsen.Tags.LogTags;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
    private GregorianCalendar endDate;
    private int endChallenges;

    // UI element som oppdateres for brukeren
    private LinearLayout friendHolder;
    private LinearLayout challengeHolder;

    // Eksterne vektøy
    private FirebaseFirestore storage;
    private FirebaseUser user;
    private Player player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);

        storage = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        loadPlayer();

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

                promptDate();
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

                Log.i(LogTags.ANY_INPUT, "CreateRoom: submitknapp trykket");

                // TODO: Input validering på andre felt på skjermen

                createRoom();
            }
        });

        // Håndtering av å legge til venner
        addFriends.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                // Triges når teksten fjernes
                if (textView.getText().toString().equals("")){
                    return false;
                }

                // TODO: Input validering, sjekke om vennen finnes

                String friend = textView.getText().toString();

                // Sjekker om man er venner
                if (!player.isFriendsWith(friend)){
                    addFriends.setError("Dere er ikke venner");
                    return false;
                }

                // Legger vennen til i lista
                friends.add(friend);


                // Lager en venn
                TextView tempFriend = new TextView(friendHolder.getContext());

                tempFriend.setText(friend);
                tempFriend.setId(friends.size());

                friendHolder.addView(tempFriend);

                addFriends.getText().clear();

                return true;

            }
        });

        // Hådterer å legge til utfordring
        addChallenge.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                // Triges når teksten fjernes
                if (textView.getText().toString().equals("")){
                    return false;
                }

                // TODO: Input validering
                String challenge = textView.getText().toString();

                // Legger til i lista
                Challenge tempChallenge = new Challenge(challenge);
                challenges.add(tempChallenge);

                TextView tempChallengeView = new TextView(challengeHolder.getContext());

                tempChallengeView.setText(challenge);
                tempChallengeView.setId(challenges.size());

                addChallenge.getText().clear();

                return true;
            }
        });
    }

    // Sjekker verdien på radioknapp, og oppdaterer variablene
    private void radioButtonClick(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        // TODO: Velge en verdi avhengig av valget
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
                Log.e(LogTags.ILLEGAL_INPUT, "CreateRoom: Radio input får feil verdi");
        }
        Log.v(LogTags.ANY_INPUT, "CreateRoom: win condition = " + String.valueOf(winAfterTime));
    }

    // Lager rommet, og sender til DB
    private void createRoom(){
        Room room = null;
        if (winAfterTime) {
            room = new Room(name.getText().toString(), friends, endDate, challenges);
        } else {
            room = new Room(name.getText().toString(), friends, endChallenges, challenges);
        }

        room.getPlayers().add(player.getIdentifier());

        storage.collection(DBTags.ROOM).add(room);

        this.finish();
    }

    private void promptDate() {
        DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month++; // Måneder er 0 indeksert

                GregorianCalendar calendar = new GregorianCalendar();

                calendar.set(year, month, day);

                endDate = calendar;
            }
        },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePicker.show();
    }


    private void loadPlayer() {
        storage.collection(DBTags.USER)
                .document(user.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            player = task.getResult().toObject(Player.class);
                        } else {
                            Log.e(LogTags.LOADING_DATA, "CreateRoom: loadPlayer " + task.getException());
                        }
                    }
                });
    }
}