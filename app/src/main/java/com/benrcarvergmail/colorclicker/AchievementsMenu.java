package com.benrcarvergmail.colorclicker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;

/**
 * Created by Benjamin on 4/14/2016.
 */
public class AchievementsMenu extends Activity {

    // References to various necessary TextViews
    private TextView mTextViewTotalClickedGood;
    private TextView mTextViewTotalClickedBad;
    private TextView mTextViewRed;
    private TextView mTextViewBlue;
    private TextView mTextViewGreen;
    private TextView mTextViewOrange;
    private TextView mTextViewYellow;
    private TextView mTextViewPink;
    private TextView mTextViewPurple;
    private TextView mTextViewWhite;
    private TextView mTextViewNickname;
    private TextView mTextViewCurrentPoints;
    private TextView mTextViewNewColorsBought;

    private String mNickname;   // Reference to current nickname

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_stats);

        Intent myIntent = getIntent();
        mNickname = myIntent.getStringExtra("nickname");

        mTextViewTotalClickedGood = (TextView) findViewById(R.id.textview_totalClickedGood);
        mTextViewTotalClickedBad = (TextView) findViewById(R.id.textview_totalClickedBad);
        mTextViewRed = (TextView) findViewById(R.id.textview_totalRed);
        mTextViewBlue = (TextView) findViewById(R.id.textview_totalBlue);
        mTextViewGreen = (TextView) findViewById(R.id.textview_totalGreen);
        mTextViewOrange = (TextView) findViewById(R.id.textview_totalOrange);
        mTextViewYellow = (TextView) findViewById(R.id.textview_totalYellow);
        mTextViewPink = (TextView) findViewById(R.id.textview_totalPink);
        mTextViewPurple = (TextView) findViewById(R.id.textview_totalPurple);
        mTextViewWhite = (TextView) findViewById(R.id.textview_totalWhite);
        mTextViewNickname = (TextView) findViewById(R.id.textview_nicknameAchievement);
        mTextViewCurrentPoints = (TextView) findViewById(R.id.textview_currentPointsAchievement);
        mTextViewNewColorsBought = (TextView) findViewById(R.id.textview_totalColorsBought);

        // Change the parameters for the background image. This makes it scroll more slowly.
        KenBurnsView backgroundKBV = (KenBurnsView)findViewById(R.id.background_kenburnsview);
        RandomTransitionGenerator generator = new RandomTransitionGenerator(20000, new AccelerateDecelerateInterpolator());
        backgroundKBV.setTransitionGenerator(generator);

        // Display all the values in the TextViews
        loadAchievementValuesIntoTextViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Update the nickname value
        mNickname = MainMenu.sSharedPref.getString(getString(R.string.sharedPreferences_nickname), "NO NICKNAME SET");
        // Update all the values
        loadAchievementValuesIntoTextViews();
    }

    /**
     * Loads the proper achievement values into the TextViews
     */
    private void loadAchievementValuesIntoTextViews() {
        mTextViewTotalClickedGood.setText(getString(R.string.defaultmTextViewTotalClickedGood) +
                " " + Achievements.getNumericalAchievementValue("achievementTotalClickedGood"));
        mTextViewTotalClickedBad.setText(getString(R.string.defaultmTextViewTotalClickedBad) +
                " " + Achievements.getNumericalAchievementValue("achievementTotalClickedBad"));
        mTextViewRed.setText( " " + Achievements.getNumericalAchievementValue("achievementTotalRed"));
        mTextViewBlue.setText( " " + Achievements.getNumericalAchievementValue("achievementTotalBlue"));
        mTextViewGreen.setText( " " + Achievements.getNumericalAchievementValue("achievementTotalGreen"));
        mTextViewOrange.setText(" " + Achievements.getNumericalAchievementValue("achievementTotalOrange"));
        mTextViewYellow.setText(" " + Achievements.getNumericalAchievementValue("achievementTotalYellow"));
        mTextViewPink.setText(" " + Achievements.getNumericalAchievementValue("achievementTotalPink"));
        mTextViewPurple.setText(" " + Achievements.getNumericalAchievementValue("achievementTotalPurple"));
        mTextViewWhite.setText(" " + Achievements.getNumericalAchievementValue("achievementTotalWhite"));
        mTextViewCurrentPoints.setText(getString(R.string.defaultmTextViewCurrentPoints) +
                " " + Achievements.getNumericalAchievementValue("achievementCurrentPoints"));
        mTextViewNewColorsBought.setText(getString(R.string.defaultmTextViewNewColorsBought) +
                " " + Achievements.getNumericalAchievementValue("achievementTotalColorsBought"));
        mTextViewNickname.setText(getString(R.string.defaultmTextViewNickname) + " " + mNickname);
    }
}
