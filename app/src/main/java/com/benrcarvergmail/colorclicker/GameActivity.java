package com.benrcarvergmail.colorclicker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private TextView mTitleText;       // Reference to the title text
    private TextView mLeftText;        // Reference to the left color TextView
    private TextView mRightText;       // Reference to the right color TextView
    private TextView mPointsCounter;   // Reference to the points counter
    private TextView mTimerText;       // Reference to the timer TextView
    private MyColor[] mColors;         // Array full of colors
    private MyColor mLeftColor;        // Reference to the color of the left TextView
    private MyColor mRightColor;       // Reference to the color of the right TextView
    private MediaPlayer mPlayer;       // SoundPool object to play sound effects
    private TimerController mTimer;    // Responsible for handling the timer
    private int mPoints;               // Variable for the number of points the user has
    private boolean mInNeedOfReset;    // Used to identify whether or not its time to reset
    private boolean mLeftIsCorrect;    // Used to specify which TextView was supposed to be clicked

    private boolean mFirstClick = true;      // Used to identify whether or not its the first click

    private final String TAG = "ColorClickerGame";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_game);

        // Create references to the various TextView objects
        mTitleText = (TextView) findViewById(R.id.textview_titleText);
        mLeftText = (TextView) findViewById(R.id.textview_left);
        mRightText = (TextView) findViewById(R.id.textview_right);
        mPointsCounter = (TextView) findViewById(R.id.textview_counter);
        mTimerText = (TextView) findViewById(R.id.textview_timer);

        // Assign the points counter's text to the points variable
        mPoints = 0;
        mPointsCounter.setText(String.valueOf(mPoints));

        // Populate the color array
        fillColors();

        // Pick a random color for the left TextView and save the color in leftColor
        mLeftColor = pickFirstColor();
        // Set mLeftText's background to be the generated color
        mLeftText.setBackgroundResource(mLeftColor.getColorId());

        // Pick a random color for the right TextView and save the color in rightColor
        mRightColor = pickFirstColor();
        mRightText.setBackgroundResource(mRightColor.getColorId());

        // Start time for the timer in milliseconds
        final long startTime = 1150;
        // Update interval for the timer
        final long interval = 5;
        // Create the TimerController object to handle the timer
        mTimer = new TimerController(startTime, interval);

        Log.i(TAG, "Before onClickListener() is called mFirstClick: " + mFirstClick);

        mLeftText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(TAG, "mFirstClick: " + mFirstClick);

                // Determine if the user is clicking for the first time. If the user is,
                // start the timer now. Otherwise, the timer will be handled in other methods.
                if(mFirstClick) {
                    Log.i(TAG, "mFirstClick true for left text");
                    mTimer.resetTimer();         // Start the timer
                    mFirstClick = false;    // It is no longer the first click
                    updateTextViews();
                    return;
                }

                // Determine whether or not we need to reset the game...
                if (!mFirstClick && mInNeedOfReset) {
                    resetGame();    // Reset the game
                }

                if (!mFirstClick) {
                    if (mLeftIsCorrect) {
                        // Call updatePoints(true), incrementing the points
                        updatePoints();
                        // We need the timer to restart since the user did not lose
                        mTimer.setNeedReset(true);
                        Log.i(TAG, "LEFT was clicked.");
                        Log.i(TAG, "Value of leftIsCorrect: " + mLeftIsCorrect);
                        updateTextViews();
                    } else {
                        // The user lost the game so just stop the timer for now
                        mTimer.cancel();
                        loseGame(R.string.game_lost_because_wrong);
                        Log.i(TAG, "LEFT was clicked.");
                        Log.i(TAG, "Value of leftIsCorrect: " + mLeftIsCorrect);
                    }
                }
            }
        });

        mRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(TAG, "mFirstClick: " + mFirstClick);

                // Determine if the user is clicking for the first time. If the user is,
                // start the timer now. Otherwise, the timer will be handled in other methods.
                if(mFirstClick) {
                    Log.i(TAG, "mFirstClick true for right text");
                    mTimer.resetTimer();         // Start the timer
                    mFirstClick = false;    // It is no longer the first click
                    updateTextViews();
                    return;
                }

                // Determine whether or not we need to reset the game (won't be on first click)
                if (!mFirstClick && mInNeedOfReset) {
                    resetGame();    // Reset the game
                }

                if (!mFirstClick) {
                    if (mLeftIsCorrect) {
                        // The user lost the game so just stop the timer for now
                        mTimer.cancel();
                        // Call updatePoints(false), which will handle the user losing the game
                        loseGame(R.string.game_lost_because_wrong);
                        Log.i(TAG, "RIGHT was clicked.");
                        Log.i(TAG, "Value of leftIsCorrect: " + mLeftIsCorrect);
                    } else {
                        // Call updatePoints(true), incrementing the points
                        updatePoints();
                        // We need the timer to restart since the user did not lose
                        mTimer.setNeedReset(true);
                        Log.i(TAG, "RIGHT was clicked.");
                        Log.i(TAG, "Value of leftIsCorrect: " + mLeftIsCorrect);
                        updateTextViews();
                    }
                }
            }
        });
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
    private void updateTextViews() {
        // Pick new colors and assign them to the variable
        mLeftColor = pickRandomColor();
        mRightColor = pickRandomColor();

        // Update the TextViews' background colors
        mLeftText.setBackgroundResource(mLeftColor.getColorId());
        mRightText.setBackgroundResource(mRightColor.getColorId());

        Random RNG = new Random();      // Create a new Random Number Generator
        int rand = RNG.nextInt(2) + 1;  // Generate a number, either 1 or 2 (I like 1 and 2)

        // Pick a random color. This will differ from the left and right colors due to how
        // pickRandomColor() works, in that it ensures a different color within the method itself.
        MyColor tempColor = pickRandomColor();

        // If rand is 1, the LEFT TextView will be improperly labeled.
        // If rand is 2, the RIGHT TextView will be improperly labeled.
        if (rand == 1) {
            mLeftText.setText(tempColor.getColorName());
            mRightText.setText(mRightColor.getColorName());
            mLeftIsCorrect = false;
        } else {
            mLeftText.setText(mLeftColor.getColorName());
            mRightText.setText(tempColor.getColorName());
            mLeftIsCorrect = true;
        }
    }

    private void updatePoints() {
        mPoints++;                                          // Increment the player's points
        mPointsCounter.setText(String.valueOf(mPoints));    // Update the point counter
    }

    /**
     * Called when the user loses the game
     */
    private void loseGame(int mTitleTextValue) {
        // Update the title text to say that the player lost
        mTitleText.setText(mTitleTextValue);
        // Ensure the mTimerText displays zero. This is because that, even though it may
        // reach zero, it will not always display zero at the end because of how it updates.
        mTimerText.setText("0ms");
        // Tell the game that it needs to reset everything since the player lost
        mLeftText.setText(R.string.left_pregame);
        mRightText.setText(R.string.right_pregame);
        mInNeedOfReset = true;

        Integer score = Integer.parseInt(mPointsCounter.getText().toString());

        Log.i(TAG, "Score: " + score);

        // Will convert this to a nicer looking loop later. I just needed something working and this works, somehow.
        // TODO: Fix this god-awful mess
        for(int i = 0; i < MainMenu.sLocalHighScoresNums.length; i++) {
            Log.i(TAG, "i: " + i + ", sLocalHighScoresNums value: " + MainMenu.sLocalHighScoresNums[i]);
            if (MainMenu.sLocalHighScoresNums[i] < score) {
                Log.i(TAG, "Score #" + i + " with value of " + MainMenu.sLocalHighScoresNums[i]
                        + " is less than " + score);
                SharedPreferences.Editor editor = MainMenu.sSharedPref.edit();
                int temp = MainMenu.sLocalHighScoresNums[i];
                MainMenu.sLocalHighScoresNums[i] = score;
                String toEdit = "highScore" + (i + 1);
                Log.i(TAG, "toEdit: " + toEdit);
                editor.putInt(toEdit, score);
                if (i + 1 < 5) {
                    if (MainMenu.sLocalHighScoresNums[i+1] < temp) {
                        Log.i(TAG, "Score #" + (i + 1) + " with value of " + MainMenu.sLocalHighScoresNums[i + 1]
                                + " is less than " + temp);
                        Log.i(TAG, "Value of temp (temp0): " + temp);
                        int temp2 = MainMenu.sLocalHighScoresNums[i+1];
                        MainMenu.sLocalHighScoresNums[i+1] = temp;
                        Log.i(TAG, "Value of temp0: " + temp + ", value of [i+1] or [" + (i + 1) + "]:"  + MainMenu.sLocalHighScoresNums[i+1]);
                        if (i + 2 < 5) {
                            if (MainMenu.sLocalHighScoresNums[i+2] < temp2) {
                                Log.i(TAG, "Score #" + (i + 2) + " with value of " + MainMenu.sLocalHighScoresNums[i + 2]
                                        + " is less than " + temp2);
                                int temp3 = MainMenu.sLocalHighScoresNums[i+2];
                                MainMenu.sLocalHighScoresNums[i+2] = temp2;
                                if(i + 3 < 5) {
                                    if (MainMenu.sLocalHighScoresNums[i+3] < temp3) {
                                        Log.i(TAG, "Score #" + (i + 3) + " with value of " + MainMenu.sLocalHighScoresNums[i + 3]
                                                + " is less than " + temp3);
                                        int temp4 = MainMenu.sLocalHighScoresNums[i+3];
                                        MainMenu.sLocalHighScoresNums[i+3] = temp3;
                                        if(i + 4 < 5) {
                                            if(MainMenu.sLocalHighScoresNums[i+4] < temp4) {
                                                Log.i(TAG, "Score #" + (i + 4) + " with value of " + MainMenu.sLocalHighScoresNums[i + 4]
                                                        + " is less than " + temp4);
                                                int temp5 = MainMenu.sLocalHighScoresNums[i+4];
                                                MainMenu.sLocalHighScoresNums[i+4] = temp4;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                editor.apply();
                return;
            }
        }
    }

    /**
     * Resets everything needed to be reset to its default value
     */
    private void resetGame() {
        // Reset points to zero
        mPoints = 0;
        // Update the point counter
        mPointsCounter.setText(String.valueOf(mPoints));
        // Reset the title text's text and text color
        mTitleText.setText(R.string.app_name);
        // Reset the title text's color
        mTitleText.setTextColor(ContextCompat.getColor(this, android.R.color.primary_text_light));
        // Reset the point counter's text color
        mPointsCounter.setTextColor(ContextCompat.getColor(this, R.color.gray));
        // Set this back to false since we just reset the game
        mInNeedOfReset = false;
        // Force the game to think it's the first click again
        mFirstClick = true;

        // updateTextViews(); // Update the text views for new colors and all

        mLeftText.setText(R.string.press_to_start);
        mRightText.setText(R.string.press_to_start);
    }

    /**
     * Updates the points value field
     * @param value value to set it as
     */
    private void updatePointsTextView(int value) {
        mPointsCounter.setText(String.valueOf(value));
        if (value < 20) {
            mPointsCounter.setTextColor(ContextCompat.getColor(this, R.color.gray));
        }
        if (value == 20) {
            mPointsCounter.setTextColor(ContextCompat.getColor(this, R.color.pink));
            mPlayer = MediaPlayer.create(this, R.raw.sound_wow);
            mPlayer.start();
        } else if (value == 30) {
            mPointsCounter.setTextColor(ContextCompat.getColor(this, R.color.orange));
            mPlayer.start();
        } else if (value == 40) {
            mPointsCounter.setTextColor(ContextCompat.getColor(this, R.color.yellow));
            mPlayer.start();
        } else if (value == 50) {
            mPointsCounter.setTextColor(ContextCompat.getColor(this, R.color.red));
            mPlayer.start();
        }
    }

    /**
     * Populate the color array
     */
    private void fillColors() {
        final MyColor red = new MyColor("Red", R.color.red);
        final MyColor blue = new MyColor("Blue", R.color.blue);
        final MyColor yellow = new MyColor("Yellow", R.color.yellow);
        final MyColor orange = new MyColor("Orange", R.color.orange);
        final MyColor pink = new MyColor("Pink", R.color.pink);
        final MyColor green = new MyColor("Green", R.color.green);
        final MyColor purple = new MyColor("Purple", R.color.purple);
        final MyColor white = new MyColor("White", R.color.white);

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
    private MyColor pickRandomColor() {
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
    private MyColor pickFirstColor() {
        Random RNG = new Random();
        int index = RNG.nextInt(8);
        return mColors[index];
    }

    public static class MyColor {
        private String colorName;
        private int colorId;

        // Constructor
        MyColor(String name, int color) {
            colorName = name;
            colorId = color;
        }

        // Return the name of the color
        String getColorName() {
            return colorName;
        }

        // Return the colorId
        int getColorId() {
            return colorId;
        }
    }

    public class TimerController extends CountDownTimer {
        private boolean mNeedReset;     // Boolean that indicates whether the timer should be reset or not

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
        private void resetTimer() {
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
