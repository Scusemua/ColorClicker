package com.benrcarvergmail.colorclicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;

import java.util.ArrayList;

/**
 * Created by Benjamin on 4/10/2016.
 */
public class ScoresMenu extends AppCompatActivity {

    private ToggleButton mToggleListViews;          // Reference to the toggle button
    private ListView mListViewLocal;                // Reference to list of local high scores
    private ListView mListViewGlobal;               // Reference to list of global high scores

    private ArrayAdapter<String> mLocalAdapter;     // ArrayAdapter for the local ListView
    private ArrayAdapter<String> mGlobalAdapter;    // ArrayAdapter for the global ListView

    private String mNickname;                       // The user's nickname
    private String mUniqueUserId;                   // The user's unique user id

    // List of local high scores and nicknames
    private ArrayList<String> mHighScoresLocal = new ArrayList<>();
    // List of global high scores and nicknames
    private ArrayList<String> mHighScoresGlobal = new ArrayList<>();

    private static final String TAG = "ColorClickerScoreMenu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent myIntent = getIntent();
        mNickname = myIntent.getStringExtra("nickname");
        mUniqueUserId = myIntent.getStringExtra("uniqueUserId");
        setContentView(R.layout.activity_scoresmenu);

        // Instantiate references to necessary views
        mListViewLocal = (ListView) findViewById(R.id.listViewLocal);
        mListViewGlobal = (ListView) findViewById(R.id.listViewGlobal);
        mToggleListViews = (ToggleButton) findViewById(R.id.togglebutton_listViews);

        // Change the parameters for the background image. This makes it scroll more slowly.
        KenBurnsView backgroundKBV = (KenBurnsView)findViewById(R.id.background_kenburnsview);
        RandomTransitionGenerator generator = new RandomTransitionGenerator(20000, new AccelerateDecelerateInterpolator());
        backgroundKBV.setTransitionGenerator(generator);

        // Add the scores to the ArrayList
        for(int i = 0; i < MainMenu.sLocalHighScores.size(); i++) {
            mHighScoresLocal.add(MainMenu.sLocalHighScores.get(i).getNickname() + "                                       " + MainMenu.sLocalHighScores.get(i).getScore());
        }

        // Add the scores to the ArrayList
        for(int i = 0; i < MainMenu.sGlobalHighScores.size(); i++) {
            mHighScoresGlobal.add(MainMenu.sLocalHighScores.get(i).getNickname() + "                                       " + MainMenu.sGlobalHighScores.get(i).getScore());
        }


        // Instantiate the adapters
        mLocalAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.listview_layout,
                R.id.label, mHighScoresLocal);
        // mGlobalAdapter = new ArrayAdapter<Integer>(getApplicationContext(), R.layout.listview_layout, mHighScoresGlobal);

        mListViewLocal.setAdapter(mLocalAdapter);

        // Add a listener for the toggle button that basically listens for when it is toggled or not
        mToggleListViews.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mListViewLocal.setVisibility(View.GONE);
                    mListViewGlobal.setVisibility(View.VISIBLE);
                    Log.i(TAG, "ToggleButton is toggled");
                } else {
                    mListViewLocal.setVisibility(View.VISIBLE);
                    mListViewGlobal.setVisibility(View.GONE);
                    Log.i(TAG, "ToggleButton is NOT toggled");
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onResume() method called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume() method called");
    }

}
