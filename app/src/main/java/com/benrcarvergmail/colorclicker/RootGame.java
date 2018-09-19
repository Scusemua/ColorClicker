package com.benrcarvergmail.colorclicker;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Benjamin on 4/20/2016.
 */
public class RootGame extends Activity {
    protected TextView mTitleText;       // Reference to the title text
    protected TextView mLeftText;        // Reference to the left color TextView
    protected TextView mRightText;       // Reference to the right color TextView
    protected TextView mPointsCounter;   // Reference to the points counter
    protected TextView mTimerText;       // Reference to the timer TextView
    protected TextView mCenterTextView;      // Reference to the center TextView
    protected MyColor[] mColors;         // Array full of colors
    protected MyColor mLeftColor;        // Reference to the color of the left TextView
    protected MyColor mRightColor;       // Reference to the color of the right TextView
    protected MediaPlayer mPlayer;       // SoundPool object to play sound effects
    protected TimerController mTimer;    // Responsible for handling the timer
    protected int mPoints;               // Variable for the number of points the user has
    protected String mNickname;          // The user's nickname
    protected String mUniqueUserId;      // The user's unique id
    protected boolean mInNeedOfReset;    // Used to identify whether or not its time to reset
    protected boolean mLeftIsCorrect;    // Used to specify which TextView was supposed to be clicked
    protected boolean mSoundEnabled;     // Indicates whether or not sound is enabled
    protected boolean mVibrationEnabled; // Indicates whether or not vibration is enabled
    protected FrameLayout mLeftFrameLayout;    // Reference to the left FrameLayout
    protected FrameLayout mRightFrameLayout;   // Reference to the right FrameLayout

    protected GameButton mLeftButton;
    protected GameButton mRightButton;

    protected boolean mFirstClick = true;      // Used to identify whether or not its the first click

    // Establish a reference to the system's vibration controller
    protected Vibrator mVibrator;

    protected final String TAG = "ColorClickerRootGame";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mNickname = intent.getStringExtra("nickname");
        mUniqueUserId = intent.getStringExtra("uniqueUserId");
        setContentView(R.layout.activity_gamestandard);

        // Create references to the various TextView objects
        mTitleText = (TextView) findViewById(R.id.textview_titleText);
        // mLeftText = (TextView) findViewById(R.id.textview_left);
        // mRightText = (TextView) findViewById(R.id.textview_right);
        mPointsCounter = (TextView) findViewById(R.id.textview_counter);
        mTimerText = (TextView) findViewById(R.id.textview_timer);
        mCenterTextView = (TextView) findViewById(R.id.centerTextView);

        mLeftButton = (GameButton) findViewById(R.id.leftButton);
        mRightButton = (GameButton) findViewById(R.id.rightButton);

        mVibrator = (Vibrator) getApplicationContext().getSystemService(VIBRATOR_SERVICE);

        // Assign the points counter's text to the points variable
        mPoints = 0;
        mPointsCounter.setText(String.valueOf(mPoints));

        // Populate the color array
        fillColors();

        // Pick a random color for the left TextView and save the color in leftColor
        mLeftColor = pickFirstColor();
        // Set mLeftText's background to be the generated color
        mLeftButton.setBackgroundResource(mLeftColor.getDrawableId());

        // Pick a random color for the right TextView and save the color in rightColor
        mRightColor = pickFirstColor();
        mRightButton.setBackgroundResource(mRightColor.getDrawableId());

        // Load the SharedPreference data for whether or not sounds and vibrations are enabled
        mSoundEnabled = MainMenu.sSharedPref.getBoolean("soundEnabled", true);
        mVibrationEnabled = MainMenu.sSharedPref.getBoolean("vibrationEnabled", true);

        // Start time for the timer in milliseconds
        final long startTime = 1500;
        // Update interval for the timer
        final long interval = 5;
        // Create the TimerController object to handle the timer
        mTimer = new TimerController(startTime, interval);

        Log.i(TAG, "Before onClickListener() is called mFirstClick: " + mFirstClick);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Load the SharedPreference data for whether or not sounds and vibrations are enabled
        mSoundEnabled = MainMenu.sSharedPref.getBoolean("soundEnabled", true);
        mVibrationEnabled = MainMenu.sSharedPref.getBoolean("vibrationEnabled", true);
        // Load in the set nickname in case the user has since changed it
        mNickname = MainMenu.sSharedPref.getString(getString(R.string.sharedPreferences_nickname), "NO NICKNAME SET");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTimer.cancel();
        resetGame();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mTimer.cancel();
        resetGame();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    /**
     * Updates the TextViews
     */
    protected void updateTextViews() {
        // Pick new colors and assign them to the variable
        mLeftColor = pickRandomColor();
        mRightColor = pickRandomColor();

        // Update the TextViews' background colors
        mLeftButton.setBackgroundResource(mLeftColor.getDrawableId());
        mRightButton.setBackgroundResource(mRightColor.getDrawableId());

        Random RNG = new Random();      // Create a new Random Number Generator
        int rand = RNG.nextInt(2) + 1;  // Generate a number, either 1 or 2 (I like 1 and 2)

        // Pick a random color. This will differ from the left and right colors due to how
        // pickRandomColor() works, in that it ensures a different color within the method itself.
        MyColor tempColor = pickRandomColor();

        // If rand is 1, the LEFT TextView will be improperly labeled.
        // If rand is 2, the RIGHT TextView will be improperly labeled.
        if (rand == 1) {
            mLeftButton.setText(tempColor.getColorName());
            mRightButton.setText(mRightColor.getColorName());

            mLeftIsCorrect = false;
        } else {
            mLeftButton.setText(mLeftColor.getColorName());
            mRightButton.setText(tempColor.getColorName());

            mLeftIsCorrect = true;
        }
    }

    /**
     * Updates the players points, plays the sound for pressing the right color (if enabled),
     * and updates the total correctly clicked colors statistic and the current points the user has.
     */
    protected void updatePoints() {
        mPoints++;                                          // Increment the player's points
        mPointsCounter.setText(String.valueOf(mPoints));    // Update the point counter

        // Ensure sound is enabled before playing sounds
        if (mSoundEnabled) {
            mPlayer = MediaPlayer.create(this, R.raw.ding);
            mPlayer.start();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
                public void onCompletion(MediaPlayer player) {
                    mPlayer.release();
                }
            });
        }
        // Update the statistical value for the total number of colors clicked as well
        // as the current points of the user by one. False indicates that we want to add to
        // the statistical value, not replace it with a value.
        Achievements.updateNumericalAchievement("achievementTotalClickedGood", 1, false);
        Achievements.updateNumericalAchievement("achievementCurrentPoints", 1, false);
    }

    /**
     * Called when the user loses the game
     */
    protected void loseGame(int mTitleTextValue) {
        // Update the title text to say that the player lost
        mTitleText.setText(mTitleTextValue);
        // Ensure the mTimerText displays zero. This is because that, even though it may
        // reach zero, it will not always display zero at the end because of how it updates.
        mTimerText.setText("0ms");
        mInNeedOfReset = true;

        // Update TextViews. The center text reads "press to restart" and the left and right read nothing.
        // mCenterTextView.setText(R.string.press_to_restart);

        // Ensure sound is enabled before playing sounds
        if (mSoundEnabled) {
            // Play the sound for clicking the wrong color
            mPlayer = MediaPlayer.create(this, R.raw.wrong);
            mPlayer.start();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
                public void onCompletion(MediaPlayer player) {
                    mPlayer.release();
                }
            });
        }

        // Ensure vibration is enabled before vibrating
        if (mVibrationEnabled) {
            mVibrator.vibrate(500);
        }

        // Reflect the incorrect click visually.
        if (mLeftIsCorrect) {
            mRightButton.setIncorrect(true);
        } else {
            mLeftButton.setIncorrect(true);
        }

        // Grab the value of the player's current score and store it in an int variable
        int score = Integer.parseInt(mPointsCounter.getText().toString());

        // Create a local reference to the list of local high scores for convenience's sake
        ArrayList<HighScore> localListOfHighScores = MainMenu.sLocalHighScores;

        // Update the LOCAL high scores arraylist
        for(int i = 0; i < localListOfHighScores.size(); i++) {
            Log.i(TAG, "Outer loop " + i + " out of " + localListOfHighScores.size());
            // Iterate through and see if the score the user JUST got it bigger than any score
            if (localListOfHighScores.get(i).getScore() < score) {
                // Save the old high score in case we need to bump scores down
                int oldHighScore = localListOfHighScores.get(i).getScore();
                // Save the old nick name in case we need to bump scores down
                String oldNickName = localListOfHighScores.get(i).getNickname();
                // Create a new high score using the proper data
                HighScore newHighScore = new HighScore(score, mNickname, mUniqueUserId);
                // Replace the old high score with the new high score
                localListOfHighScores.set(i, newHighScore);
                // Now, let's see if we need to bump the scores down. We do this by iterating
                // through the remaining scores and determining which need to be shifted down based
                // on whether or not they're smaller than the score that comes before it.
                for (int j = i; j < localListOfHighScores.size(); j++) {
                    Log.i(TAG, "Inner loop " + j + " out of " + localListOfHighScores.size());
                    // If we find a score that needs to be shifted...
                    if (oldHighScore > localListOfHighScores.get(j).getScore()) {
                        // We first create another high score to replace the score needing to be
                        // shifted. Basically, if we have scores 1, 2, 3, we replace score 1 with
                        // new score, then score 2 with score 1, then score 3 with score 2.
                        newHighScore = new HighScore(oldHighScore, oldNickName, mUniqueUserId);
                        oldHighScore = localListOfHighScores.get(j).getScore();
                        oldNickName = localListOfHighScores.get(j).getNickname();
                        localListOfHighScores.set(j, newHighScore);
                    }
                }
                // Save the high scores now that they've been modified
                MainMenu.saveLocalHighScores();
                // Return because we are now done with manipulating the saved high scores and
                // if we let the loops continue, scores will be modified in ways we don't want
                // (i.e. duplicates)
                return;
            }
        }
    }

    /**
     * Resets everything needed to be reset to its default value
     */
    protected void resetGame() {
        // Have the center text view display "press again to restart"
        mCenterTextView.setText(R.string.press_again_to_restart);
        // Reset the title text's text and text color
        mTitleText.setText(R.string.app_name);
        // Set this back to false since we just reset the game
        mInNeedOfReset = false;
        // Force the game to think it's the first click again
        mFirstClick = true;

        mLeftButton.setIncorrect(false);
        mRightButton.setIncorrect(false);

    }

    /**
     * Populate the color array
     */
    protected void fillColors() {
        final MyColor red = new MyColor("Red", R.color.red, R.drawable.gamebutton_red);
        final MyColor blue = new MyColor("Blue", R.color.blue, R.drawable.gamebutton_blue);
        final MyColor yellow = new MyColor("Yellow", R.color.yellow, R.drawable.gamebutton_yellow);
        final MyColor orange = new MyColor("Orange", R.color.orange, R.drawable.gamebutton_orange);
        final MyColor pink = new MyColor("Pink", R.color.pink, R.drawable.gamebutton_pink);
        final MyColor green = new MyColor("Green", R.color.green, R.drawable.gamebutton_green);
        final MyColor purple = new MyColor("Purple", R.color.purple, R.drawable.gamebutton_purple);
        final MyColor white = new MyColor("White", R.color.white, R.drawable.gamebutton_white);

        mColors = new MyColor[] {
                red,
                blue,
                yellow,
                orange,
                pink,
                green,
                purple,
                white
        };
    }

    /**
     * Picks a random color from the color array. It checks to ensure that the color
     * is not the same as either color already displayed, ensuring that there are no
     * repeats and additionally that the left and right TextViews always remain different colors.
     *
     * @return randomly chosen color
     */
    protected MyColor pickRandomColor() {
        if(mColors == null) {
            fillColors();
        }
        Random RNG = new Random();
        int index = RNG.nextInt(8);
        MyColor color = mColors[index];
        boolean done = false;
        while(!done) {
            if (color.getColorId() == mLeftColor.getColorId() || color.getColorId() == mRightColor.getColorId()) {
                index = RNG.nextInt(8);
                color = mColors[index];
            } else {
                done = true;
            }
        }
        return color;
    }

    /**
     * called to avoid NullPointerException due to leftColor not having a value
     * when .getColorID is called in pickRandomColor() initially
     *
     * @return a randomly picked color
     */
    protected MyColor pickFirstColor() {
        Random RNG = new Random();
        int index = RNG.nextInt(8);
        return mColors[index];
    }

    public static class MyColor {
        private String colorName;
        private int drawableId;
        private int colorId;

        // Constructor
        MyColor(String name, int color, int drawableId) {
            colorName = name;
            colorId = color;
            this.drawableId = drawableId;
        }

        int getDrawableId() { return drawableId; }

        // Return the name of the color
        String getColorName() {
            return colorName;
        }

        // Return the colorId
        int getColorId() {
            return colorId;
        }
    }

    protected class TimerController extends CountDownTimer {
        protected boolean mNeedReset;     // Boolean that indicates whether the timer should be reset or not

        /**
         * Constructor for the TimerController object
         * @param startTime the start time for the timer (what the timer should start at)
         * @param interval the interval at which the timer should update (I think)
         */
        public TimerController(long startTime, long interval) {
            super(startTime, interval);
        }

        /**
         * Returns the value of mNeedReset
         * @return boolean value of mNeedReset
         */
        public boolean getNeedReset() {
            return mNeedReset;
        }

        /**
         * Sets a new value for the mNeedReset boolean
         * @param b new boolean value for mNeedReset
         */
        public void setNeedReset(boolean b) {
            mNeedReset = b;
        }

        /**
         * Resets the timer to its initial values
         */
        protected void resetTimer() {
            mNeedReset = false;     // It no longer needs to be reset
            cancel();               // Stop the timer
            start();                // (Re)start the timer
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (mNeedReset) {
                resetTimer();   // Reset the timer
            } else {
                String s = "Time Remaining: " + String.valueOf(millisUntilFinished) + "ms";
                mTimerText.setText(s);
            }
        }

        @Override
        public void onFinish() {
            cancel();
            Log.i(TAG, "onFinish() of TimerController called!");
            loseGame(R.string.game_lost_because_time);
        }
    }
}
