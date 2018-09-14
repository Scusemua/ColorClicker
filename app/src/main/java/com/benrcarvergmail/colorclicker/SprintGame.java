package com.benrcarvergmail.colorclicker;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

/**
 * Created by Benjamin on 4/20/2016.
 */
public class SprintGame extends RootGame {

    private final static String TAG = "ColorClickerSprint";

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        // Recreate the mTimer object such that it lasts 30 seconds
        mTimer = new SprintTimerController(30000, 10);

        mLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(TAG, "mFirstClick: " + mFirstClick);

                // Determine if the user is clicking for the first time. If the user is,
                // start the timer now. Otherwise, the timer will be handled in other methods.
                if(mFirstClick) {
                    Log.i(TAG, "mFirstClick true for left text");
                    mTimer.resetTimer();         // Start the timer
                    mFirstClick = false;         // It is no longer the first click
                    mCenterTextView.setText("");     // Reset the text for the middle text view
                    // Reset points to zero
                    mPoints = 0;
                    // Update the point counter
                    mPointsCounter.setText(String.valueOf(mPoints));
                    updateTextViews();
                    return;
                }

                // Determine whether or not we need to reset the game...
                if (mInNeedOfReset) {
                    resetGame();    // Reset the game
                }

                if (!mFirstClick) {
                    if (mLeftIsCorrect) {
                        // Update the points. Since they clicked the correct color, pass true.
                        updatePoints(true);
                        // Update the statistic for the number of whatever color was clicked by
                        // referencing the proper achievement using the color's name
                        Achievements.updateNumericalAchievement("achievementTotal" + mLeftColor.getColorName(), 1, false);
                        Log.i(TAG, "LEFT was clicked.");
                        Log.i(TAG, "Value of leftIsCorrect: " + mLeftIsCorrect);
                        updateTextViews();
                    } else {
                        // Update the points. Since they clicked the incorrect color, pass false.
                        updatePoints(false);
                        // Update the statistical value for the number of incorrectly clicked colors
                        Achievements.updateNumericalAchievement("achievementTotalClickedBad", 1, false);
                        // Update the statistic for the number of whatever color was clicked by
                        // referencing the proper achievement using the color's name
                        Achievements.updateNumericalAchievement("achievementTotal" + mLeftColor.getColorName(), 1, false);
                        Log.i(TAG, "LEFT was clicked.");
                        Log.i(TAG, "Value of leftIsCorrect: " + mLeftIsCorrect);
                        updateTextViews();
                    }
                }
            }
        });

        mRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(TAG, "mFirstClick: " + mFirstClick);

                // Determine if the user is clicking for the first time. If the user is,
                // start the timer now. Otherwise, the timer will be handled in other methods.
                if(mFirstClick) {
                    Log.i(TAG, "mFirstClick true for right text");
                    mTimer.resetTimer();         // Start the timer
                    mFirstClick = false;         // It is no longer the first click
                    mCenterTextView.setText("");     // Reset the text for the middle text view
                    // Reset points to zero
                    mPoints = 0;
                    // Update the point counter
                    mPointsCounter.setText(String.valueOf(mPoints));
                    updateTextViews();
                    return;
                }

                // Determine whether or not we need to reset the game (won't be on first click)
                if (mInNeedOfReset) {
                    resetGame();    // Reset the game
                }

                if (!mFirstClick) {
                    if (mLeftIsCorrect) {
                        // Update the points. Since they clicked the correct color, pass true.
                        updatePoints(false);
                        // Update the statistical value for the number of incorrectly clicked colors
                        Achievements.updateNumericalAchievement("achievementTotalClickedBad", 1, false);
                        // Update the statistic for the number of whatever color was clicked by
                        // referencing the proper achievement using the color's name
                        Achievements.updateNumericalAchievement("achievementTotal" + mRightColor.getColorName(), 1, false);
                        Log.i(TAG, "RIGHT was clicked.");
                        Log.i(TAG, "Value of leftIsCorrect: " + mLeftIsCorrect);
                        updateTextViews();
                    } else {
                        // Call updatePoints(true), incrementing the points
                        updatePoints(true);
                        // Update the statistic for the number of whatever color was clicked by
                        // referencing the proper achievement using the color's name
                        Achievements.updateNumericalAchievement("achievementTotal" + mRightColor.getColorName(), 1, false);
                        Log.i(TAG, "RIGHT was clicked.");
                        Log.i(TAG, "Value of leftIsCorrect: " + mLeftIsCorrect);
                        updateTextViews();
                    }
                }
            }
        });
    }

    protected void updatePoints(boolean increment) {
        // If they clicked correctly, increment their points.
        if (increment) {
            super.updatePoints();
        } else {
            // Ensure the user's points do not go negative.
            if (mPoints > 0) {
                mPoints--;                                          // Decrement the player's points
                mPointsCounter.setText(String.valueOf(mPoints));    // Update the point counter
            }

            // Ensure sound is enabled before playing sounds
            if (mSoundEnabled) {
                // Play the sound for clicking the wrong color
                mPlayer = MediaPlayer.create(this, R.raw.antiding);
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
        }
    }

    public class SprintTimerController extends TimerController {

        /**
         * Constructor for the TimerController object
         *
         * @param startTime the start time for the timer (what the timer should start at)
         * @param interval  the interval at which the timer should update (I think)
         */
        public SprintTimerController(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (mNeedReset) {
                resetTimer();   // Reset the timer
            } else {
                String s = "Time Remaining: " + String.valueOf(millisUntilFinished / 1000) + "." + String.valueOf(millisUntilFinished % 1000);
                mTimerText.setText(s);
            }
        }

        @Override
        public void onFinish() {
            // Ensure sound is enabled before playing sounds
            if (mSoundEnabled) {
                // Play the sound for clicking the wrong color
                mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.wrong);
                mPlayer.start();
                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
                    public void onCompletion(MediaPlayer player) {
                        mPlayer.release();
                    }
                });
            }

            super.onFinish();
        }
    }
}
