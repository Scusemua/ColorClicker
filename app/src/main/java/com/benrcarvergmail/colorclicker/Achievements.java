package com.benrcarvergmail.colorclicker;

import android.content.SharedPreferences;

/**
 * Created by Benjamin on 4/15/2016.
 */
public class Achievements {
    // Static reference to SharedPreferences object
    private static SharedPreferences mSharedPrefs = MainMenu.sSharedPref;
    // Static reference to a list (array) of all the achievement references
    public static String[] achievementReferences = new String[] {
            "achievementCurrentPoints",         // 0
            "achievementTotalBlue",             // 1
            "achievementTotalRed",              // 2
            "achievementTotalGreen",            // 3
            "achievementTotalOrange",           // 4
            "achievementTotalPurple",           // 5
            "achievementTotalPink",             // 6
            "achievementTotalWhite",            // 7
            "achievementTotalYellow",           // 8
            "achievementTotalCyan",             // 9
            "achievementTotalClickedGood",      // 10
            "achievementTotalClickedBad",       // 11
            "achievementTotalPointsSpent",      // 12
            "achievementTotalColorsBought"      // 13
    };

    /**
     * Updates a numerical achievement or statistic
     * @param reference reference to the SharedPreference value
     * @param value the value to change the reference by
     * @param replace if this is true, we replace the current value in the reference. Otherwise,
     *                we simply add "value" to whatever is located within reference.
     */
    public static void updateNumericalAchievement(String reference, int value, boolean replace) {
        SharedPreferences.Editor editor = mSharedPrefs.edit(); // Create a SharedPreferences editor
        // If we are to replace the value, do so. Otherwise, add/subtract to the value.
        if (replace) {
            editor.putInt(reference, value);
        } else {
            // Get the current value
            int temp = mSharedPrefs.getInt(reference, 0);
            // Change the value by adding the parameter
            editor.putInt(reference, temp + value);
        }
        // Apply the changes
        editor.apply();
    }

    /**
     * Updates a String-based achievement or statistic
     * @param reference reference to the SharedPreference value
     * @param value the value to change the reference by
     * @param replace if this is true, we replace the current value in the reference. Otherwise,
     *                we simply add "value" to whatever is located within reference.
     */
    public static void updateStringAchievement(String reference, String value, boolean replace) {
        SharedPreferences.Editor editor = mSharedPrefs.edit(); // Create a SharedPreferences editor
        // If we are to replace the value, do so. Otherwise, concatenate the value.
        if (replace) {
            editor.putString(reference, value);
        } else {
            // Get the current value
            String temp = mSharedPrefs.getString(reference, "");
            // Change the value by concatenating the parameter
            editor.putString(reference, temp + value);
        }
    }

    /**
     * Returns the numerical value for a given achievement reference. This is just prettier than
     * doing mSharedPreferences.getInt(reference, defaultValue) even though it really isn't.
     * @param reference the reference to the SharedPReference object
     * @return the value of the data stored at that reference. It will return 0 if there is nothing there.
     */
    public static int getNumericalAchievementValue(String reference) {
        return mSharedPrefs.getInt(reference, 0);
    }

    /**
     * Returns the String value for a given achievement reference. This is just prettier than
     * doing mSharedPreferences.getString(reference, defaultString) even though it really isn't.
     * @param reference the reference to the SharedPReference object
     * @return the value of the data stored at that reference. It will return null if there is nothing there.
     */
    public static String getStringAchievementValue(String reference) {
        return mSharedPrefs.getString(reference, null);
    }
}
