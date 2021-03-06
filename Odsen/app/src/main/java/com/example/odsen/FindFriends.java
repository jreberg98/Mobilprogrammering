package com.example.odsen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.example.odsen.Model.Player;
import com.example.odsen.Tags.DBTags;
import com.example.odsen.Tags.LogTags;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.security.auth.login.LoginException;

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
    private Player player;
    private ArrayList<String> allUsers = new ArrayList<>();

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
        loadAllUsers();

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

                if (username.equals("")) {
                    return true;
                }

                EditText editText = (EditText) textView;

                if (!allUsers.contains(username)) {
                    usernameInput.setError("Brukeren finnes ikke");
                    return false;
                }

                // TODO: sjekke om bruker finnes
                if (player.hasRelationWith(username)) {
                    usernameInput.setError("Kan ikke legges til som venn, dere har allerede et forhold");
                    return false;
                }

                player.getPendingRequests().add(username);

                outFriendRequests.add(username);

                TextView tempRequest = new TextView(friendRequest.getContext());

                tempRequest.setText(username);
                tempRequest.setId(outFriendRequests.size());
                tempRequest.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                tempRequest.setGravity(ViewGroup.TEXT_ALIGNMENT_GRAVITY);
                tempRequest.setTextSize(30);
                tempRequest.setPadding(5,5,5,10);

                friendRequest.addView(tempRequest);

                checkTitle();

                Log.d(LogTags.ANY_INPUT, "FindFriends: Sendt vennefroesp??rsel til " + username);

                addFriend(username);

                editText.getText().clear();

                return true;
            }
        });

    }

    private void checkTitle() {
        if (!outFriendRequests.isEmpty()) {
            infoSendFriendRequest.setText("Sendte foresp??rsler");
        }
    }

    private void addFriend(String displayName) {
        // TODO: Legge til godkjenning p?? venneforesp??rsler
        storage.collection(DBTags.USER).document(displayName).update(DBTags.USER_FRIEND_REQUESTS, FieldValue.arrayUnion(user.getEmail()));
        storage.collection(DBTags.USER).document(user.getEmail()).update(DBTags.USER_FRIEND_REQUESTS, FieldValue.arrayUnion(displayName));
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
                    loadNewFriendRequests();
                } else {
                    Log.e(LogTags.LOADING_DATA, "FindFriends: loadPlayer " + task.getException());
                }
            }
        });

        storage.collection(DBTags.USER)
                .document(user.getEmail())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                player = documentSnapshot.toObject(Player.class);
                loadNewFriendRequests();
            }
        });
    }

    private void loadAllUsers(){
        // TODO: Er detta trygt? Egentlig ikke, vell -.-
        storage.collection(DBTags.USER)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    Log.i(LogTags.LOADING_DATA, "FindFriends: loadAlllUsers: fant " + task.getResult().size() + " spillere totalt");
                    for (DocumentSnapshot document : task.getResult()){
                        allUsers.add(document.getId());
                    }
                } else {
                    Log.e(LogTags.LOADING_DATA, "FindFriends: loadAllUsers: " + task.getException());
                }
            }
        });
    }

    private void loadNewFriendRequests() {
        friendList.removeAllViewsInLayout();

        for (int i = 0; i < player.getFriendRequests().size(); i++){
            String friend = player.getFriendRequests().get(i);
            TextView textView = new TextView(this.getBaseContext());

            textView.setText(friend);
            textView.setId(i);
            textView.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            textView.setGravity(ViewGroup.TEXT_ALIGNMENT_GRAVITY);
            // Litt lettere ?? trykke p??
            // Alle har p??lsefingre i forhold til datamusa
            textView.setTextSize(20);
            textView.setPadding(5,5,5,10);

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
        // Flytter n??v??rende bruker sin venneforesp??rsel fra friendRequests til friends i DB
        storage.collection(DBTags.USER)
                .document(player.getIdentifier())
                .update("friendRequests", FieldValue.arrayRemove(friend),
                "friends", FieldValue.arrayUnion(friend));

        // Tilsvarende som over, bare for den som sendte foresp??rselen
        storage.collection(DBTags.USER)
                .document(friend)
                .update("pendingRequests", FieldValue.arrayRemove(player.getIdentifier()),
                        "friends", FieldValue.arrayUnion(player.getIdentifier()));


        // Oppdaterer brukeren i minnet i appen
        player.getFriendRequests().remove(friend);
        // Oppdaterer UI
        loadNewFriendRequests();
    }
}