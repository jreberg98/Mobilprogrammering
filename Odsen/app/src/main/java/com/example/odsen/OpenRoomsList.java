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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class OpenRoomsList extends AppCompatActivity {

    public final String ROOM_KEY = "ROOM_KEY";

    // Views
    private TextView titleView;
    private LinearLayout roomHolder;
    private EditText filterByNumberOfUsers;
    private EditText filterByPlayer;
    private EditText filterByRemainingChallenges;

    // Data
    private IDB db;
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

        storage.collection(IDB.ROOMS)
                .whereArrayContains("players", userID)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Room room = document.toObject(Room.class);
                        rooms.add(room);
                    }
                    Log.i(LogTags.LOADING_DATA, "Du har " + rooms.size() + " aktive rom");

                    String title = getString(R.string.OPEN_ROOMS_title, rooms.size());
                    titleView.setText(title);
                }
                updateRoomListUI();
            }
        });

    }

    private void updateRoomListUI() {
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            TextView textView = new TextView(roomHolder.getContext());

            textView.setText(room.getName());
            textView.setId(i);
            textView.setGravity(ViewGroup.TEXT_ALIGNMENT_GRAVITY);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = intentToRoom(view);

                    Log.i(LogTags.NAVIGATION, "Open rooms: Går til " + room.getName());
                    startActivity(intent);
                }
            });

            roomHolder.addView(textView);

        }
    }

    private Intent intentToRoom(View view) {
        Intent intent = new Intent(getBaseContext(), ActiveRoom.class);
        TextView textView = (TextView) view;

        intent.putExtra(ROOM_KEY, textView.getText());
        Log.i(LogTags.NAVIGATION, "OpenRooms: Lager intent til " + textView.getText());

        return intent;
    }
}