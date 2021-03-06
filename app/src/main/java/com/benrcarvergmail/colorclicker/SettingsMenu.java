package com.benrcarvergmail.colorclicker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;

/**
 * Created by Benjamin on 4/10/2016.
 */
public class SettingsMenu extends AppCompatActivity {

    private TextView mTextViewCurrentNickname;   // Reference to the current nickname text view
    private TextView mWarningText;               // Reference to text view of the warning paragraph
    private EditText mEditTextNickname;          // Reference to the new nickname edit text
    private Button mButtonConfirmNickname;       // Reference to the new nickname confirm button
    private Button mButtonDeleteContent;         // Reference to the button used to delete scores
    private Button mButtonPickColors;            // Reference to the button used to pick colors
    private Button mHelpButton;                  // Reference to the button that displays help
    private ToggleButton mToggleButtonVibrate;         // Reference to the toggle vibration button
    private ToggleButton mToggleSoundButton;     // Reference to the toggle sound button

    private String mCurrentNickName;             // The user's current nickname
    private String mUniqueUserId;                // The user's unique user id
    private SharedPreferences mSharedPref;       // Shared preference object

    private boolean mAreYouSure;                 // Used when deleting content

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent myIntent = getIntent();
        mUniqueUserId = myIntent.getStringExtra("uniqueUserId");
        setContentView(R.layout.activity_settingsmenu);

        // Instantiate the sSharedPref object to the one created in MainMenu so they're the same
        mSharedPref = MainMenu.sSharedPref;
        // Load in the saved nickname. If there isn't a nickname saved, return "NO NICKNAME SET"
        mCurrentNickName = mSharedPref.getString(getString(R.string.sharedPreferences_nickname), "NO NICKNAME SET");

        // Get references to all the views and whatnot
        mTextViewCurrentNickname = (TextView) findViewById(R.id.textview_currentNickname);
        mWarningText = (TextView) findViewById(R.id.textview_deleteContentWarning);
        mEditTextNickname = (EditText) findViewById(R.id.edittext_nickname);
        mButtonConfirmNickname = (Button) findViewById(R.id.button_confirmNickname);
        mButtonDeleteContent = (Button) findViewById(R.id.button_delete_local_content);
        mButtonPickColors = (Button) findViewById(R.id.button_pickerColors);
        mHelpButton = (Button) findViewById(R.id.button_settingsHelp);
        mToggleSoundButton = (ToggleButton) findViewById(R.id.togglebutton_sound);
        mToggleButtonVibrate = (ToggleButton) findViewById(R.id.togglebutton_vibrate);

        // Assign the current nickname text view the value of the saved nick name
        mTextViewCurrentNickname.setText("Current nickname: " + mCurrentNickName);

        // Change the parameters for the background image. This makes it scroll more slowly.
        KenBurnsView backgroundKBV = (KenBurnsView)findViewById(R.id.background_kenburnsview);
        RandomTransitionGenerator generator = new RandomTransitionGenerator(20000, new AccelerateDecelerateInterpolator());
        backgroundKBV.setTransitionGenerator(generator);

        // Save the color of the text now, in case the user triggers the warning
        // button which will turn the text white and therefore change the color
        final int oldColor = mWarningText.getCurrentTextColor();

        mToggleButtonVibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Save the SharedPreferences parameter for vibration
                SharedPreferences.Editor editor = mSharedPref.edit();
                if (isChecked) {
                    editor.putBoolean("vibrationEnabled", true);
                } else {
                    editor.putBoolean("vibrationEnabled", false);
                }
                editor.apply();
            }
        });

        mToggleSoundButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = mSharedPref.edit();
                if (isChecked) {
                    editor.putBoolean("soundEnabled", true);
                    final MediaPlayer mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.blop);
                    mPlayer.start();
                    mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
                        public void onCompletion(MediaPlayer player) {
                            mPlayer.release();
                        }
                    });
                } else {
                    editor.putBoolean("soundEnabled", false);
                }
                editor.apply();
            }
        });

        mButtonPickColors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(SettingsMenu.this, ColorChooserActivity.class);
                startActivity(myIntent);
            }
        });

        // onClickListener for the delete high scores button
        mButtonDeleteContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Warn the user first by changing the button's text to "Are you sure?" and
                // by making the warning paragraph's text stand out more by making it white.
                if (!mAreYouSure) {
                    mAreYouSure = true;
                    mButtonDeleteContent.setText(R.string.are_you_sure);
                    mWarningText.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
                } else {
                    // Once the user is warned and clicks the button again, reset the button's text,
                    // reset the color of the warning paragraph, and create a Toast message that
                    // lets the user know that the high scores have been cleared.
                    mButtonDeleteContent.setText(R.string.button_deleteContent);
                    mAreYouSure = false;
                    mWarningText.setTextColor(oldColor);
                    // Delete the high scores
                    SharedPreferences.Editor editor = mSharedPref.edit();

                    // Reset the scores
                    resetLocalScores();

                    // Create a Toast message notifying the user that the scores were deleted

                    editor.apply();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "High scores deleted", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        mButtonConfirmNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the old nickname temporarily
                final String oldNick = mCurrentNickName;
                // Get the new nickname and save it in the proper variable
                mCurrentNickName = mEditTextNickname.getText().toString();
                // Change the text view that displays the current nickname
                mTextViewCurrentNickname.setText("Current nickname: " + mCurrentNickName);

                // Save the new nickname to SystemPreferences
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString(getString(R.string.sharedPreferences_nickname), mCurrentNickName);
                editor.apply();

                // Create a Toast message notifying the user that their nickname was changed
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Nickname changed from " + oldNick + " to " + mCurrentNickName + ".", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    /**
     * Resets the local high score data
     */
    private void resetLocalScores() {
        // Create a default high score to use
        HighScore defaultScore = new HighScore(0, mCurrentNickName, mUniqueUserId);
        // Clear the local high score ArrayList
        MainMenu.sLocalHighScores.clear();
        // Add the default high score for each value
        for(int i = 0; i < 5; i++) {
            MainMenu.sLocalHighScores.add(defaultScore);
        }
        // Save the changes
        MainMenu.saveLocalHighScores();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Determine whether or not the various toggle buttons should be toggled or not
        boolean soundEnabled = mSharedPref.getBoolean("soundEnabled", true);
        boolean vibrationEnabled = mSharedPref.getBoolean("vibrationEnabled", true);
        mToggleSoundButton.setChecked(soundEnabled);
        mToggleButtonVibrate.setChecked(vibrationEnabled);
    }
}
