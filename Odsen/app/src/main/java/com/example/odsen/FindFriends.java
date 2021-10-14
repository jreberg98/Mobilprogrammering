package com.example.odsen;

import static java.lang.Math.random;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class FindFriends extends AppCompatActivity {

    private EditText usernameInput;
    private ImageView qrCodePlaceholder;
    private Button qrCodeInput;
    private TextView infoNewFriendRequests;
    private LinearLayout friendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        usernameInput = findViewById(R.id.FRIENDS_add_with_user_name);
        qrCodePlaceholder = findViewById(R.id.FRIENDS_qr_code_placeholder);
        qrCodeInput = findViewById(R.id.FRIENDS_add_with_qr_code);
        infoNewFriendRequests = findViewById(R.id.FRIENDS_new_friend_requests);
        friendList = findViewById(R.id.FRIENDS_friends_list);


        // Sjekker etter nye venner
        ArrayList<String> friends = newFriendRequests();
        if (friends != null) {
            for (int i = 0; i < friends.size(); i++) {
                TextView tempFriend = new TextView(friendList.getContext());

                tempFriend.setText(friends.get(i));
                tempFriend.setId(i);
                tempFriend.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                tempFriend.setGravity(ViewGroup.TEXT_ALIGNMENT_GRAVITY);

                friendList.addView(tempFriend);
            }
        } else {
            infoNewFriendRequests.setText("");
        }



        usernameInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                String username = textView.getText().toString();

                // TODO: Sende venneforespørsel til DB

                return true;
            }
        });

    }

    private ArrayList<String> newFriendRequests() {
        // TODO: sjekk DB etter nye venner

        int random = (int) (Math.random() * 10);

        // Returnerer random en ArrayList ettersom DB ikke er satt opp
        if (random % 2 == 0) {
            Log.i("FRIENDS", "Har venner");
            ArrayList<String> temp = new ArrayList<String>();
            temp.add("dust");
            temp.add("gjøk");
            temp.add("tosk");
            temp.add("pute");
            temp.add("fisk");
            return temp;
        }
        Log.i("FRIENDS", "Ingen venner :(");
        return null;

    }
}