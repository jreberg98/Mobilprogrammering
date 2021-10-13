package com.example.odsen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public final String NAVIGATION = "NAVIGATING";

    private Button MAIN_open_rooms;
    private Button MAIN_find_friends;
    private Button MAIN_create_room;
    private Button MAIN_finished_rooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MAIN_open_rooms = findViewById(R.id.MAIN_open_rooms);
        MAIN_open_rooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openRoomsIntent = new Intent(view.getContext(), OpenRooms.class);

                Log.i(NAVIGATION, "Going to 'OpenRooms'");

                startActivity(openRoomsIntent);
            }
        });

        MAIN_find_friends = findViewById(R.id.MAIN_find_friends);
        MAIN_find_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent findFriendsIntent = new Intent(view.getContext(), FindFriends.class);

                Log.i(NAVIGATION, "Going to 'FindFriends'");

                startActivity(findFriendsIntent);
            }
        });

        MAIN_create_room = findViewById(R.id.MAIN_create_room);
        MAIN_create_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createRoomIntent = new Intent(view.getContext(), CreateRoom.class);

                Log.i(NAVIGATION, "Going to 'CreateRoom'");

                startActivity(createRoomIntent);
            }
        });

        MAIN_finished_rooms = findViewById(R.id.MAIN_finished_rooms);
        MAIN_finished_rooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent finishedRoomsIntent = new Intent(view.getContext(), FinishedRooms.class);

                Log.i(NAVIGATION, "Going to 'FinishedRooms'");

                startActivity(finishedRoomsIntent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        TextView loggedInn = findViewById(R.id.MAIN_logged_inn_user);
        String userText = getString(R.string.MAIN_logged_inn_as, "dust");
        loggedInn.setText(userText);
    }


}