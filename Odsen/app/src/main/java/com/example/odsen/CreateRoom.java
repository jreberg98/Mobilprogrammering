package com.example.odsen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
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
    private Date endDate;
    private int endChallenges;

    // UI element som oppdateres for brukeren
    private LinearLayout friendHolder;
    private LinearLayout challengeHolder;
    private TextView winCondition;

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
        winCondition = findViewById(R.id.CREATE_win_condition);

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

                promptNumber();
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
                tempChallengeView.setGravity(ViewGroup.TEXT_ALIGNMENT_GRAVITY);

                challengeHolder.addView(tempChallengeView);

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

        Log.i(LogTags.ANY_INPUT, "CreateRoom: prompter dato");

        DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Date date = new Date(year, month, day);

                endDate = date;

                String winText = getString(R.string.CREATE_end_room_by_date, day, month, year);
                winCondition.setText(winText);
            }
        },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePicker.show();
    }

    // https://stackoverflow.com/questions/17944061/how-to-use-number-picker-with-dialog - 10/11-21
    private void promptNumber() {

        Log.i(LogTags.ANY_INPUT, "CreateRoom: prompter tall");

        NumberPicker picker = new NumberPicker(getBaseContext());
        picker.setMinValue(1);
        // TODO: Er detta en reel verdi eller bør den endres?
        picker.setMaxValue(100);
        picker.setValue(25);


        FrameLayout layout = new FrameLayout(getBaseContext());
        layout.addView(picker, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
        ));

        new AlertDialog.Builder(this)
                .setView(layout)
                .setPositiveButton(R.string.GENERAL_submit_dialog, (dialogInterface, i) -> {
                    endChallenges = picker.getValue();

                    String winText = getString(R.string.CREATE_end_room_by_challenges, endChallenges);
                    winCondition.setText(winText);
        })
                .setNegativeButton(R.string.GENERAL_cancel_dialog, null)
                .show();
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