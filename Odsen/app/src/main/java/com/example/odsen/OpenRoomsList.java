package com.example.odsen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private ArrayList<Room> rooms = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_rooms_list);
        
        
        titleView = findViewById(R.id.OPEN_ROOMS_title);
        roomHolder = findViewById(R.id.OPEN_ROOMS_room);
        filterByNumberOfUsers = findViewById(R.id.OPEN_ROOMS_filter_by_number_of_players);
        filterByPlayer = findViewById(R.id.OPEN_ROOMS_filter_by_user);
        filterByRemainingChallenges = findViewById(R.id.OPEN_ROOMS_filter_remaining_challenges);
        
        
        
        // TODO: Fylle rooms med rom fra DB
        
        if (((int) (Math.random() * 10) % 2) == 0){
            rooms.add(new Room("Dusterom", new ArrayList<String>(), 0, new ArrayList<Challenge>()));
        }
        
        if (rooms.isEmpty()) {
            // TODO: gjør noe når du ikke har noen rom
            Log.i("OPEN ROOMS","Ingen rom");
        } else {
            // Lister opp alle rom
            Log.i("OPEN ROOMS", "Du har " + rooms.size() + " aktive rom");

            String title = getString(R.string.OPEN_ROOMS_title, rooms.size());
            titleView.setText(title);


            // Oppdaterer lista med rom
            for(int i = 0; i < rooms.size(); i++) {
                Room room = rooms.get(i);
                TextView textView = new TextView(roomHolder.getContext());

                textView.setText(room.getName());
                textView.setId(i);
                textView.setGravity(ViewGroup.TEXT_ALIGNMENT_GRAVITY);

                // Kan trykke på TextViewet for å gå til rommet
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
    }

    private Intent intentToRoom(View view) {
        Intent intent = new Intent(getBaseContext(), ActiveRoom.class);

        TextView textView = (TextView) view;

        intent.putExtra(ROOM_KEY, textView.getText());

        Log.i(LogTags.NAVIGATION, "OpenRooms: Lager intent til " + textView.getText());

        return intent;
    }
}