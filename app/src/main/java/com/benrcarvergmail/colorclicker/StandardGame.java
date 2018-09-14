package com.benrcarvergmail.colorclicker;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * Created by Benjamin on 4/20/2016.
 */
public class StandardGame extends RootGame {

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

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
                        // Call updatePoints(true), incrementing the points
                        updatePoints();
                        // Update the statistic for the number of whatever color was clicked by
                        // referencing the proper achievement using the color's name
                        Achievements.updateNumericalAchievement("achievementTotal" + mLeftColor.getColorName(), 1, false);
                        // We need the timer to restart since the user did not lose
                        mTimer.setNeedReset(true);
                        Log.i(TAG, "LEFT was clicked.");
                        Log.i(TAG, "Value of leftIsCorrect: " + mLeftIsCorrect);
                        updateTextViews();
                    } else {
                        // The user lost the game so just stop the timer for now
                        mTimer.cancel();
                        loseGame(R.string.game_lost_because_wrong);
                        // Update the statistical value for the number of incorrectly clicked colors
                        Achievements.updateNumericalAchievement("achievementTotalClickedBad", 1, false);
                        // Update the statistic for the number of whatever color was clicked by
                        // referencing the proper achievement using the color's name
                        Achievements.updateNumericalAchievement("achievementTotal" + mLeftColor.getColorName(), 1, false);
                        Log.i(TAG, "LEFT was clicked.");
                        Log.i(TAG, "Value of leftIsCorrect: " + mLeftIsCorrect);
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
                        // The user lost the game so just stop the timer for now
                        mTimer.cancel();
                        // Call updatePoints(false), which will handle the user losing the game
                        loseGame(R.string.game_lost_because_wrong);
                        // Update the statistical value for the number of incorrectly clicked colors
                        Achievements.updateNumericalAchievement("achievementTotalClickedBad", 1, false);
                        // Update the statistic for the number of whatever color was clicked by
                        // referencing the proper achievement using the color's name
                        Achievements.updateNumericalAchievement("achievementTotal" + mRightColor.getColorName(), 1, false);
                        Log.i(TAG, "RIGHT was clicked.");
                        Log.i(TAG, "Value of leftIsCorrect: " + mLeftIsCorrect);
                    } else {
                        // Call updatePoints(true), incrementing the points
                        updatePoints();
                        // Update the statistic for the number of whatever color was clicked by
                        // referencing the proper achievement using the color's name
                        Achievements.updateNumericalAchievement("achievementTotal" + mRightColor.getColorName(), 1, false);
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
}
