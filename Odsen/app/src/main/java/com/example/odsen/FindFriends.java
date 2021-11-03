package com.example.odsen;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.Document;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FindFriends extends AppCompatActivity {

    // Views
    private EditText usernameInput;
    private ImageView qrCodePlaceholder;
    private Button qrCodeInput;
    private TextView infoNewFriendRequests;
    private LinearLayout friendList;
    private LinearLayout friendRequest;
    private TextView infoSendFriendRequest;

    // Data
    // TODO: oppdatere begge listene fra DB
    private ArrayList<String> incomingFriendRequests;
    private ArrayList<String> outFriendRequests = new ArrayList<>();

    // Eksternt
    private FirebaseUser user;
    private FirebaseFirestore storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        storage = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();



        usernameInput = findViewById(R.id.FRIENDS_add_with_user_name);
        qrCodePlaceholder = findViewById(R.id.FRIENDS_qr_code_placeholder);
        qrCodeInput = findViewById(R.id.FRIENDS_add_with_qr_code);
        infoNewFriendRequests = findViewById(R.id.FRIENDS_new_friend_requests);
        friendList = findViewById(R.id.FRIENDS_friends_list);
        friendRequest = findViewById(R.id.FRIENDS_friend_request_list);
        infoSendFriendRequest = findViewById(R.id.FRIENDS_sent_friend_requests_text);


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


                outFriendRequests.add(username);

                TextView tempRequest = new TextView(friendRequest.getContext());

                tempRequest.setText(username);
                tempRequest.setId(outFriendRequests.size());
                tempRequest.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                tempRequest.setGravity(ViewGroup.TEXT_ALIGNMENT_GRAVITY);

                friendRequest.addView(tempRequest);

                checkTitle();

                Log.d(LogTags.ANY_INPUT, "FindFriends: Sendt vennefroespørsel til " + username);
                // TODO: Sende venneforespørsel til DB

                addFriend(username);

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
        Log.i(LogTags.LOADING_DATA, "FindFriends: Laster inn venner");
        return null;
    }

    private void checkTitle() {
        if (!outFriendRequests.isEmpty()) {
            infoSendFriendRequest.setText("Sendte forespørsler");
        }
    }

    private void addFriend(String displayName) {
        // TODO: Legge til godkjenning på venneforespørsler
        storage.collection(IDB.USERS).document(displayName).update("friends", FieldValue.arrayUnion(user.getEmail()));
        storage.collection(IDB.USERS).document(user.getEmail()).update("friends", FieldValue.arrayUnion(displayName));
    }
}