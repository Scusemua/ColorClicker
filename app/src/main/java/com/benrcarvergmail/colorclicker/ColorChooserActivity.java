package com.benrcarvergmail.colorclicker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

/**
 * Created by Benjamin on 4/12/2016.
 */
public class ColorChooserActivity extends AppCompatActivity {

    private int mCollapsedWidth;    // Width of a color before it's expanded
    private int mExpandedWidth;    // Width of a color when it's expanded

    private CheckBox mCheckBoxRed;              // Checkbox for enabling/disabling the color red

    private View mCyanColorView;                 // The red color view

    private static final String TAG = "ColorClickerCChooser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colorchooser);

        // Instantiate necessary references to views
        mCheckBoxRed = (CheckBox) findViewById(R.id.colorcheckbox_cyan);
        mCyanColorView = findViewById(R.id.view_colorCyan);

        // Save the width for the expanded width
        mExpandedWidth = mCyanColorView.getLayoutParams().width;

        Log.i(TAG, "Expanded Width: " + mExpandedWidth);

        // Calculate the collapsed with
        mCollapsedWidth = mExpandedWidth / 2;

        Log.i(TAG, "Collapsed Width: " + mCollapsedWidth);

        // Set a listener for the checkbox to listen for when it gets checked/unchecked
        mCheckBoxRed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO: Implement the enabling/disabling of new colors
                // When it gets checked, expand the color (and eventually enable it)
                if (isChecked) {
                    ResizeAnimation resizeAnimation = new ResizeAnimation(mCyanColorView, mExpandedWidth);
                    resizeAnimation.setDuration(600);
                    mCyanColorView.startAnimation(resizeAnimation);
                } else { // When it's unchecked, collapse the color (and eventually disable it)
                    ResizeAnimation resizeAnimation = new ResizeAnimation(mCyanColorView, mCollapsedWidth);
                    resizeAnimation.setDuration(600);
                    mCyanColorView.startAnimation(resizeAnimation);
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        saveCheckBoxState(mCheckBoxRed.isChecked(), "red");
    }

    @Override
    public void onResume() {
        super.onResume();
        mCheckBoxRed.setChecked(loadCheckBoxState("red"));
    }

    private void saveCheckBoxState(final boolean isChecked, String checkbox) {
        SharedPreferences sharedPreferences = MainMenu.sSharedPref;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (checkbox.equals("red")) {
            editor.putBoolean("redIsChecked", isChecked);
        }
        editor.apply();
    }

    private boolean loadCheckBoxState(String checkbox) {
        SharedPreferences sharedPreferences = MainMenu.sSharedPref;
        if (checkbox.equals("red")) {
            return sharedPreferences.getBoolean("redIsChecked", false);
        }
        return false;
    }
}
