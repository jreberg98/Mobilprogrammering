package com.example.odsen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.odsen.Model.Room;
import com.example.odsen.Tags.DBTags;
import com.example.odsen.Tags.LogTags;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class OpenRoomsList extends AppCompatActivity {

    public static final String ROOM_KEY = "ROOM_KEY";

    // Views
    private TextView titleView;
    private LinearLayout roomHolder;
    private EditText filterByNumberOfUsers;
    private EditText filterByPlayer;
    private EditText filterByRemainingChallenges;

    // Data
    private ArrayList<Room> rooms = new ArrayList<>();
    private FirebaseFirestore storage;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_rooms_list);

        storage = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Toast toast = Toast.makeText(this, "Kunne ikke holde deg innlogget :(", Toast.LENGTH_LONG);
        }

        loadRooms(user.getEmail());

        titleView = findViewById(R.id.OPEN_ROOMS_title);
        roomHolder = findViewById(R.id.OPEN_ROOMS_room);
        filterByNumberOfUsers = findViewById(R.id.OPEN_ROOMS_filter_by_number_of_players);
        filterByPlayer = findViewById(R.id.OPEN_ROOMS_filter_by_user);
        filterByRemainingChallenges = findViewById(R.id.OPEN_ROOMS_filter_remaining_challenges);
        }



    private void loadRooms(String userID) {

        storage.collection(DBTags.ROOM)
                .whereArrayContains("players", userID)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Room room = document.toObject(Room.class);

                        String documentID = document.getId();

                        rooms.add(room);
                        addRoomToUI(room.getName(), documentID);
                    }
                    Log.i(LogTags.LOADING_DATA, "Du har " + rooms.size() + " aktive rom");

                    String title = getString(R.string.OPEN_ROOMS_title, rooms.size());
                    titleView.setText(title);
                }
            }
        });

    }


    private void addRoomToUI(String roomName, String documentID) {
        TextView textView = new TextView(roomHolder.getContext());

        textView.setText(roomName);
        textView.setGravity(ViewGroup.TEXT_ALIGNMENT_GRAVITY);
        textView.setTag(documentID);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = intentToRoom(view);

                startActivity(intent);
            }
        });

        roomHolder.addView(textView);
    }

    private Intent intentToRoom(View view) {
        Intent intent = new Intent(getBaseContext(), ActiveRoom.class);
        TextView textView = (TextView) view;

        String tag = (String) view.getTag();

        intent.putExtra(ROOM_KEY, tag);
        Log.i(LogTags.NAVIGATION, "OpenRooms: Lager intent til " + textView.getText() + " med ID " + tag);

        return intent;
    }
}