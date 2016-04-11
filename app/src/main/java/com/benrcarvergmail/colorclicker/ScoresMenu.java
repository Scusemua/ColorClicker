package com.benrcarvergmail.colorclicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ToggleButton;

/**
 * Created by Benjamin on 4/10/2016.
 */
public class ScoresMenu extends AppCompatActivity {

    private ToggleButton mToggleListViews;          // Reference to the toggle button
    private ListView mListViewLocal;                // Reference to list of local high scores
    private ListView mListViewGlobal;               // Reference to list of global high scores

    private ArrayAdapter<String> mLocalAdapter;     // ArrayAdapter for the local ListView
    private ArrayAdapter<String> mGlobalAdapter;    // ArrayAdapter for the global ListView

    private String[] mHighScoresLocal = MainMenu.sLocalHighScores;      // List of local high scores
    private String[] mHighScoresGlobal = MainMenu.sGlobalHighScores;    // List of global high scores

    private final String TAG = "ColorClickerScoreMenu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoresmenu);

        // Instantiate references to necessary views
        mListViewLocal = (ListView) findViewById(R.id.listViewLocal);
        mListViewGlobal = (ListView) findViewById(R.id.listViewGlobal);
        mToggleListViews = (ToggleButton) findViewById(R.id.togglebutton_listViews);

        // Instantiate the adapters
        mLocalAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.listview_layout, mHighScoresLocal);
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
