package com.example.odsen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
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
    Player player;

    // Eksternt
    private FirebaseUser user;
    private FirebaseFirestore storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        storage = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        loadPlayer();

        usernameInput = findViewById(R.id.FRIENDS_add_with_user_name);
        qrCodePlaceholder = findViewById(R.id.FRIENDS_qr_code_placeholder);
        qrCodeInput = findViewById(R.id.FRIENDS_add_with_qr_code);
        infoNewFriendRequests = findViewById(R.id.FRIENDS_new_friend_requests);
        friendList = findViewById(R.id.FRIENDS_friends_list);
        friendRequest = findViewById(R.id.FRIENDS_friend_request_list);
        infoSendFriendRequest = findViewById(R.id.FRIENDS_sent_friend_requests_text);




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

    private void checkTitle() {
        if (!outFriendRequests.isEmpty()) {
            infoSendFriendRequest.setText("Sendte forespørsler");
        }
    }

    private void addFriend(String displayName) {
        // TODO: Legge til godkjenning på venneforespørsler
        storage.collection(IDB.USERS).document(displayName).update("friendRequests", FieldValue.arrayUnion(user.getEmail()));
        storage.collection(IDB.USERS).document(user.getEmail()).update("pendingRequests", FieldValue.arrayUnion(displayName));
    }

    private void loadPlayer() {
        storage.collection(IDB.USERS)
                .document(user.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    player = task.getResult().toObject(Player.class);
                    loadNewFriendRequests();
                } else {
                    Log.e(LogTags.LOADING_DATA, "FindFriends: loadPlayer " + task.getException());
                }
            }
        });
    }

    private void loadNewFriendRequests() {
        // Tømet lista, kan dermed brukes flere ganger
        // TODO: erstatte med recycleView ?
        friendList.removeAllViewsInLayout();

        for (int i = 0; i < player.getFriendRequests().size(); i++){
            String friend = player.getFriendRequests().get(i);
            TextView textView = new TextView(this.getBaseContext());

            textView.setText(friend);
            textView.setId(i);
            textView.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            textView.setGravity(ViewGroup.TEXT_ALIGNMENT_GRAVITY);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    acceptFriend(friend);
                }
            });

            friendList.addView(textView);

        }
    }

    private void acceptFriend(String friend){
        // Flytter nåværende bruker sin venneforespørsel fra friendRequests til friends i DB
        storage.collection(IDB.USERS)
                .document(player.getIdentifier())
                .update("friendRequests", FieldValue.arrayRemove(friend),
                "friends", FieldValue.arrayUnion(friend));

        // Tilsvarende som over, bare for den som sendte forespørselen
        storage.collection(IDB.USERS)
                .document(friend)
                .update("pendingRequests", FieldValue.arrayRemove(player.getIdentifier()),
                        "friends", FieldValue.arrayUnion(player.getIdentifier()));


        // Oppdaterer brukeren i minnet i appen
        player.getFriendRequests().remove(friend);
        // Oppdaterer UI
        loadNewFriendRequests();
    }
}