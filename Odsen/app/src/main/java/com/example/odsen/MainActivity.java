package com.example.odsen;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public final String NAVIGATION = "NAVIGATING";

    private Button MAIN_open_rooms;
    private Button MAIN_find_friends;
    private Button MAIN_create_room;
    private Button MAIN_finished_rooms;
    private TextView loggedInn;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Navigering til de andre skjermene
        MAIN_open_rooms = findViewById(R.id.MAIN_open_rooms);
        MAIN_open_rooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openRoomsIntent = new Intent(view.getContext(), OpenRoomsList.class);

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

        firebaseAuth = FirebaseAuth.getInstance();

        createAuthStateListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loggedInn = findViewById(R.id.MAIN_logged_inn_user);
        String userText = getString(R.string.MAIN_logged_inn_as, "dust");
        loggedInn.setText(userText);
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    private void createAuthStateListener() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                            new AuthUI.IdpConfig.EmailBuilder().build()
                    );

                    signInLauncher.launch(AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build()
                    );
                }
            }
        };
    }

    // See: https://developer.android.com/training/basics/intents/result
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );


    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            String username = user.getDisplayName();
            String userText = "";

            Log.i(LogTags.USER_LOGGING, "Logget inn som " + user.getEmail());

            if (username != null) {
                userText = getString(R.string.MAIN_logged_inn_as, username);
            } else {
                userText = getString(R.string.MAIN_logged_inn_as, "en navnløs bruker");
            }

            loggedInn.setText(userText);
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...

            // Logger og avslutter appen
            loggedInn.setText("VIRKER IKKE");
            Log.e(LogTags.USER_LOGGING, "Innlogging feilet");
            this.finishAffinity();

        }
    }

}