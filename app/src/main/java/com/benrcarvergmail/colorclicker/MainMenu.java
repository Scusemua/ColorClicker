package com.benrcarvergmail.colorclicker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Benjamin on 4/10/2016.
 */
public class MainMenu extends AppCompatActivity {

    private Button mButtonPlay;          // Reference to the play button
    private Button mButtonSettings;      // Reference to the settings button
    private Button mButtonScores;        // Reference to the high scores button
    private Button mButtonStats;         // Button for accessing achievements
    private String mUniqueUserId;        // Unique user ID for submitting high scores
    private String mUserNickname;        // Nickname for the user
    private boolean mSoundEnabled;       // Indicates whether sounds are enabled/disabled
    private boolean mForceFirstTimeSetup = false;         // Forces the first time setup method to run
    private boolean mForceInitialLaunchSetup = false;     // Forces the initial launch setup method to run

    // SharedPreferences object for the app
    public static SharedPreferences sSharedPref;

    // Array of the local high scores
    public static ArrayList<HighScore> sLocalHighScores = new ArrayList<>();
    // Array of the global high scores
    public static ArrayList<HighScore> sGlobalHighScores = new ArrayList<>();

    private final String TAG = "ColorClickerMainMenu";  // TAG String


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        // Instantiate references to necessary views
        mButtonPlay = (Button) findViewById(R.id.button_play);
        mButtonSettings = (Button) findViewById(R.id.button_settings);
        mButtonScores = (Button) findViewById(R.id.button_scores);
        mButtonStats = (Button) findViewById(R.id.button_stats);

        // Instantiate the shared preferences object.
        sSharedPref = this.getPreferences(Context.MODE_PRIVATE);
        // Determine if it is the first launch ever and if it is the initial launch for this session
        boolean isItFirst = sSharedPref.getBoolean(getString(R.string.sharedPreferences_firstTime), true);
        boolean isInitialLaunch = sSharedPref.getBoolean(getString(R.string.sharedPreferences_initialLaunch), true);

        // Load in the user's saved nickname
        mUserNickname = sSharedPref.getString(getString(R.string.sharedPreferences_nickname), "I NEED TO SET A NICKNAME");

        // Change the parameters for the background image. This makes it scroll more slowly.
        KenBurnsView backgroundKBV = (KenBurnsView)findViewById(R.id.background_kenburnsview);
        RandomTransitionGenerator generator = new RandomTransitionGenerator(20000, new AccelerateDecelerateInterpolator());
        backgroundKBV.setTransitionGenerator(generator);

        // Load in the saved local high scores
        retrieveLocalScores();

        // Check if we need to run the firstTimeSetup() or initialLaunchSetup() methods
        if (isItFirst || mForceFirstTimeSetup) {
            firstTimeSetup();
        }
        if (isInitialLaunch || mForceInitialLaunchSetup) {
            initialLaunchSetup();
        }

        // onClickListener() for the "Stats" (Achievements) button
        mButtonStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainMenu.this, AchievementsMenu.class);
                myIntent.putExtra("nickname", mUserNickname);
                startActivity(myIntent);
            }
        });

        // onClickListener() for the "Play" button
        mButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the game activity by creating an intent to start it
                Intent myIntent = new Intent(MainMenu.this, GamemodeSelector.class);
                myIntent.putExtra("nickname", mUserNickname);
                myIntent.putExtra("uniqueUserId", mUniqueUserId);
                startActivity(myIntent);
            }
        });

        // onClickListener() for the "Settings" button
        mButtonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the settings menu activity by creating an intent to start it
                Intent myIntent = new Intent(MainMenu.this, SettingsMenu.class);
                myIntent.putExtra("uniqueUserId", mUniqueUserId);
                startActivity(myIntent);
            }
        });

        // onClickListener() for the "High Scores" button
        mButtonScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the high scores menu activity by creating an intent to start it
                Intent myIntent = new Intent(MainMenu.this, ScoresMenu.class);
                myIntent.putExtra("nickname", mUserNickname);
                myIntent.putExtra("uniqueUserId", mUniqueUserId);
                startActivity(myIntent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Since we're basically either closing the app or pausing it for a while, reset
        // the initial launch variable so we run the initialLaunchSetup() when the app resumes
        SharedPreferences.Editor editor = sSharedPref.edit();
        editor.putBoolean(getString(R.string.sharedPreferences_initialLaunch), true);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check if we need to run it (we do probably I think) and if we do, run the initial launch setup method
        boolean runInitialLaunchSetup = sSharedPref.getBoolean(getString(R.string.sharedPreferences_initialLaunch), true);
        if (runInitialLaunchSetup) {
            initialLaunchSetup();
        }
        // Update the local scores since they've quite possibly changed
        retrieveLocalScores();
        mUserNickname = sSharedPref.getString(getString(R.string.sharedPreferences_nickname), "NO NICKNAME SET");
    }

    /**
     * Generates necessary information if its the user's first time opening the application
     */
    private void firstTimeSetup() {
        Log.i(TAG, "First time data created.");
        mUniqueUserId = UUID.randomUUID().toString();           // Generate a unique user id code
        SharedPreferences.Editor editor = sSharedPref.edit();   // Create an editor for SharedPref
        editor.putString(getString(R.string.sharedPreferences_uniqueId), mUniqueUserId);            // Save the unique user id
        // Establish that it is no longer the first time
        editor.putBoolean(getString(R.string.sharedPreferences_firstTime), false);
        editor.putBoolean("soundEnabled", true);                // Enable sound
        // Create a default high score object to create the high scores
        HighScore defaultScore = new HighScore(0, mUserNickname, mUniqueUserId);
        if (sLocalHighScores == null) { // Instantiate the high scores list
            sLocalHighScores = new ArrayList<>();
        }
        sLocalHighScores.clear();       // Populate the high scores list. We clear it first in
        for(int i = 0; i < 5; i++) {    // case we're debugging and force the first time to run.
                                        // If we didn't clear it, the list would have these added
                                        // to the end of an already populated list of scores.
            sLocalHighScores.add(defaultScore);
        }
        saveLocalHighScores();  // Save the high scores
        // Apply the edits
        editor.apply(); // Apply the edits

        // Display a Toast message, notifying the user to set their nickname
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Make sure to set your nickname in the SETTINGS menu!", Toast.LENGTH_SHORT).show();
            }
        });

        // Debugging log information
        Log.i(TAG, "Unique User ID Generated: " + mUniqueUserId);
    }

    /**
     * Returns the unique user id
     * @return the user's unique id
     */
    public String getUniqueUserId() {
        return mUniqueUserId;
    }

    /**
     * Executes when the user opens the application for the first time
     */
    private void initialLaunchSetup() {
        Log.i(TAG, "Initial launch data retrieval and recording complete.");
        sSharedPref = this.getPreferences(Context.MODE_PRIVATE);
        mUniqueUserId = sSharedPref.getString(getString(R.string.sharedPreferences_uniqueId), "UNKNOWN_USER_ID");
        mSoundEnabled = sSharedPref.getBoolean("soundEnabled", true);
        SharedPreferences.Editor editor = sSharedPref.edit();
        editor.putBoolean(getString(R.string.sharedPreferences_initialLaunch), false);
        editor.apply();
    }

    /**
     * Retrieves the local high scores
     */
    public void retrieveLocalScores() {
        Gson gson = new Gson();
        String json = sSharedPref.getString("sLocalHighScores", "");
        Type type = new TypeToken<List<HighScore>>(){}.getType();
        sLocalHighScores = gson.fromJson(json, type);
    }

    /**
     * Writes whatever is in localHighScores to memory
     */
    public static void saveLocalHighScores() {
        SharedPreferences.Editor editor = sSharedPref.edit();
        Gson gson = new Gson();
        Type type = new TypeToken<List<HighScore>>(){}.getType();
        String json = gson.toJson(sLocalHighScores, type);
        editor.putString("sLocalHighScores", json);
        editor.apply();
    }

    // Connects to remote server to grab the global scores
    class RetrieveGlobalHighScores extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            return null;
        }
    }
}
