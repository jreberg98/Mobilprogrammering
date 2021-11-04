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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String USER_KEY = "USER_ID";

    private Button MAIN_open_rooms;
    private Button MAIN_find_friends;
    private Button MAIN_create_room;
    private Button MAIN_finished_rooms;
    private Button MAIN_logg_out;
    private TextView loggedInn;

    // Eksternt
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseFirestore storage;

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

                openRoomsIntent.putExtra(USER_KEY, user.getUid());

                Log.i(LogTags.NAVIGATION, "Going to 'OpenRooms'");

                startActivity(openRoomsIntent);
            }
        });

        MAIN_find_friends = findViewById(R.id.MAIN_find_friends);
        MAIN_find_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent findFriendsIntent = new Intent(view.getContext(), FindFriends.class);

                Log.i(LogTags.NAVIGATION, "Going to 'FindFriends'");

                startActivity(findFriendsIntent);
            }
        });

        MAIN_create_room = findViewById(R.id.MAIN_create_room);
        MAIN_create_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createRoomIntent = new Intent(view.getContext(), CreateRoom.class);

                Log.i(LogTags.NAVIGATION, "Going to 'CreateRoom'");

                startActivity(createRoomIntent);
            }
        });

        MAIN_finished_rooms = findViewById(R.id.MAIN_finished_rooms);
        MAIN_finished_rooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent finishedRoomsIntent = new Intent(view.getContext(), FinishedRooms.class);

                Log.i(LogTags.NAVIGATION, "Going to 'FinishedRooms'");

                startActivity(finishedRoomsIntent);
            }
        });

        MAIN_logg_out = findViewById(R.id.MAIN_logg_out);
        MAIN_logg_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthUI.getInstance().signOut(getApplicationContext())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.i(LogTags.USER_LOGGING, "Bruker logget ut");
                        }
                });
            }
        });

        loggedInn = findViewById(R.id.MAIN_logged_inn_user);

        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseFirestore.getInstance();
        user = firebaseAuth.getCurrentUser();

        createAuthStateListener();
    }
/*
    @Override
    protected void onStart() {
        super.onStart();
        loggedInn = findViewById(R.id.MAIN_logged_inn_user);
        String userText = getString(R.string.MAIN_logged_inn_as, "dust");
        loggedInn.setText(userText);
    }
*/
    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);

        if (user == null) {
            Log.i(LogTags.USER_LOGGING, "Main: du er logget av, logger deg på igjen -.-");

            user = FirebaseAuth.getInstance().getCurrentUser();

            if (user == null){
                Log.e(LogTags.USER_LOGGING, "Main: kunne ikke logge deg på -.-");
                Log.e(LogTags.ILLEGAL_INPUT, "Kommet inn på main uten å være logget inn");
            }
        }
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

    // Kopiert fra nettet, dokumentasjonen til enten Firebase eller Developer.Android (eller begge)
    // TODO: Finne link til koden
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
            user = FirebaseAuth.getInstance().getCurrentUser();

            String username = user.getDisplayName();
            String userText = "";

            Log.i(LogTags.USER_LOGGING, "Logget inn som " + user.getEmail());

            if (username != null) {
                userText = getString(R.string.MAIN_logged_inn_as, username);
            } else {
                userText = getString(R.string.MAIN_logged_inn_as, "en navnløs bruker");
            }

            addUserToDB();

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
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);

        }
    }

    // Legger bruker til i DB, så den kan brukes av flere
    private void addUserToDB(){
        Log.i(LogTags.USER_LOGGING, "Legger til i DB");
        storage.collection(IDB.USERS)
                .whereEqualTo("uid", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Bruker er ikke i DB, legger den da til
                    if (task.getResult().isEmpty()) {
                        Log.i(LogTags.USER_LOGGING, "Main: legger til brukerdata i \"vanlig\" db");
                        Player temp = new Player(user.getUid(), user.getEmail(), user.getDisplayName());

                        storage.collection(IDB.USERS)
                                .document(temp.getIdentifier()).set(temp);
                    }
                } else {
                    Log.e(LogTags.LOADING_DATA, "Main: kunne ikke laste data: " + task.getException());
                }
            }
        });
    }

}