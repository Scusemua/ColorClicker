package com.benrcarvergmail.colorclicker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Benjamin on 4/10/2016.
 */
public class MainMenu extends AppCompatActivity {

    private Button mPlayButton;          // Reference to the play button
    private Button mSettingsButton;      // Reference to the settings button
    private Button mScoresButton;        // Reference to the high scores button
    private String mUniqueUserId;        // Unique user ID for submitting high scores
    private boolean forceFirstTimeSetup = false;          // Forces the first time setup method to run
    private boolean forceInitialLaunchSetup = false;     // Forces the initial launch setup method to run

    public static SharedPreferences sSharedPref;        // SharedPreferences object for the app
    public static String sUserNickname;                 // Nickname for the user
    public static String[] sLocalHighScores;            // Array of the local high scores
    public static String[] sGlobalHighScores;           // Array of the global high scores
    // Array of the local high scores with ONLY numbers, no usernames
    public static Integer[] sLocalHighScoresNums;
    // Array of the global high scores with ONLY numbers, no usernames
    public static Integer[] sGlobalHighScoresNums;

    private final String TAG = "ColorClickerMainMenu";  // TAG String


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        // Instantiate references to necessary views
        mPlayButton = (Button) findViewById(R.id.button_play);
        mSettingsButton = (Button) findViewById(R.id.button_settings);
        mScoresButton = (Button) findViewById(R.id.button_scores);

        // Instantiate the shared preferences object.
        sSharedPref = this.getPreferences(Context.MODE_PRIVATE);
        // Determine if it is the first launch ever and if it is the initial launch for this session
        boolean isItFirst = sSharedPref.getBoolean(getString(R.string.sharedPreferences_firstTime), true);
        boolean isInitialLaunch = sSharedPref.getBoolean(getString(R.string.sharedPreferences_initialLaunch), true);

        // Load in the user's saved nickname
        sUserNickname = sSharedPref.getString(getString(R.string.sharedPreferences_nickname), "I NEED TO SET A NICKNAME");

        // Load in the saved local high scores
        retrieveLocalScores();

        // Check if we need to run the firstTimeSetup() or initialLaunchSetup() methods
        if (isItFirst || forceFirstTimeSetup) {
            firstTimeSetup();
        }
        if (isInitialLaunch || forceInitialLaunchSetup) {
            initialLaunchSetup();
        }

        // onClickListener() for the "Play" button
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the game activity by creating an intent to start it
                Intent myIntent = new Intent(MainMenu.this, GameActivity.class);
                startActivity(myIntent);
            }
        });

        // onClickListener() for the "Settings" button
        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the settings menu activity by creating an intent to start it
                Intent myIntent = new Intent(MainMenu.this, SettingsMenu.class);
                startActivity(myIntent);
            }
        });

        // onClickListener() for the "High Scores" button
        mScoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the high scores menu activity by creating an intent to start it
                Intent myIntent = new Intent(MainMenu.this, ScoresMenu.class);
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
    }

    /**
     * Generates necessary information if its the user's first time opening the application
     */
    private void firstTimeSetup() {
        Log.i(TAG, "First time data created.");
        mUniqueUserId = UUID.randomUUID().toString();
        SharedPreferences.Editor editor = sSharedPref.edit();
        editor.putString(getString(R.string.sharedPreferences_uniqueId), mUniqueUserId);
        editor.putBoolean(getString(R.string.sharedPreferences_firstTime), false);
        editor.putInt(getString(R.string.sharedPreferences_highscoreOne), 0);
        editor.putInt(getString(R.string.sharedPreferences_highscoreTwo), 0);
        editor.putInt(getString(R.string.sharedPreferences_highscoreThree), 0);
        editor.putInt(getString(R.string.sharedPreferences_highscoreFour), 0);
        editor.putInt(getString(R.string.sharedPreferences_highscoreFive), 0);
        editor.apply();
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
        SharedPreferences.Editor editor = sSharedPref.edit();
        editor.putBoolean(getString(R.string.sharedPreferences_initialLaunch), false);
        editor.apply();
    }

    /**
     * Retrieves the local high scores
     */
    public void retrieveLocalScores() {

        Log.i(TAG, "Local scores were just retrieved");

        Integer[] toSort = new Integer[] {
                sSharedPref.getInt(getString(R.string.sharedPreferences_highscoreOne), -5),
                sSharedPref.getInt(getString(R.string.sharedPreferences_highscoreTwo), -5),
                sSharedPref.getInt(getString(R.string.sharedPreferences_highscoreThree), -5),
                sSharedPref.getInt(getString(R.string.sharedPreferences_highscoreFour), -5),
                sSharedPref.getInt(getString(R.string.sharedPreferences_highscoreFive), -5)
        };

        // Sort the Array...
        Arrays.sort(toSort);
        Collections.reverse(Arrays.asList(toSort));

        for (Integer i: toSort) {
            Log.i(TAG, "toSort value: " + i);
        }

        // Have sLocalHighScoreNums now be the sorted array
        MainMenu.sLocalHighScoresNums = toSort;

        for (Integer i: MainMenu.sLocalHighScoresNums) {
            Log.i(TAG, "From Retrieve: localHighScoreNums value: " + i);
        }

        // Load in each of the five saved high score values
        sLocalHighScores = new String[] {
                MainMenu.sUserNickname + "                                            " +
                        toSort[0],
                MainMenu.sUserNickname + "                                            " +
                        toSort[1],
                MainMenu.sUserNickname + "                                            " +
                        toSort[2],
                MainMenu.sUserNickname + "                                            " +
                        toSort[3],
                MainMenu.sUserNickname + "                                            " +
                        toSort[4] };
    }

    // Connects to remote server to grab the global scores
    class RetrieveGlobalHighScores extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            return null;
        }
    }
}
