package com.benrcarvergmail.colorclicker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Benjamin on 4/10/2016.
 */
public class SettingsMenu extends AppCompatActivity {

    private TextView mTextViewCurrentNickname;   // Reference to the current nickname text view
    private TextView mWarningText;               // Reference to text view of the warning paragraph
    private EditText mEditTextNickname;          // Reference to the new nickname edit text
    private Button mButtonConfirmNickname;       // Reference to the new nickname confirm button
    private Button mButtonDeleteContent;         // Reference to the button used to delete scores

    private String mCurrentNickName;             // The user's current nickname
    private SharedPreferences mSharedPref;       // Shared preference object

    private boolean areYouSure;                  // Used when deleting content

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        // Assign the current nickname text view the value of the saved nick name
        mTextViewCurrentNickname.setText("Current nickname: " + mCurrentNickName);

        // Save the color of the text now, in case the user triggers the warning
        // button which will turn the text white and therefore change the color
        final int oldColor = mWarningText.getCurrentTextColor();

        // onClickListener for the delete high scores button
        mButtonDeleteContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Warn the user first by changing the button's text to "Are you sure?" and
                // by making the warning paragraph's text stand out more by making it white.
                if (!areYouSure) {
                    areYouSure = true;
                    mButtonDeleteContent.setText(R.string.are_you_sure);
                    mWarningText.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
                } else {
                    // Once the user is warned and clicks the button again, reset the button's text,
                    // reset the color of the warning paragraph, and create a Toast message that
                    // lets the user know that the high scores have been cleared.
                    mButtonDeleteContent.setText(R.string.button_deleteContent);
                    areYouSure = false;
                    mWarningText.setTextColor(oldColor);
                    // Delete the high scores
                    SharedPreferences.Editor editor = mSharedPref.edit();
                    editor.putInt(getString(R.string.sharedPreferences_highscoreOne), -1);
                    editor.putInt(getString(R.string.sharedPreferences_highscoreTwo), -1);
                    editor.putInt(getString(R.string.sharedPreferences_highscoreThree), -1);
                    editor.putInt(getString(R.string.sharedPreferences_highscoreFour), -1);
                    editor.putInt(getString(R.string.sharedPreferences_highscoreFive), -1);
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
                final String oldNick = mCurrentNickName;
                mCurrentNickName = mEditTextNickname.getText().toString();
                mTextViewCurrentNickname.setText("Current nickname: " + mCurrentNickName);

                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString(getString(R.string.sharedPreferences_nickname), mCurrentNickName);
                editor.apply();

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
     * Load necessary data from the SharedPreferences
     */
    private void loadFromPreferences() {

    }
}
