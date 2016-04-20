package com.benrcarvergmail.colorclicker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by Benjamin on 4/19/2016.
 */
public class GamemodeSelector extends Activity {

    private Button mButtonGamemodeStandard;
    private Button mButtonGamemodeReverse;
    private Button mButtonGamemodeSprint;
    private Button mButtonGamemodeStandardHelp;
    private Button mButtonGamemodeReverseHelp;
    private Button mButtonGamemodeSprintHelp;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_gamemodeselector);

        // References to necessary views
        mButtonGamemodeStandard = (Button) findViewById(R.id.button_gamemodeStandard);
        mButtonGamemodeStandardHelp = (Button) findViewById(R.id.button_gamemodeStandardInfo);
        mButtonGamemodeReverseHelp = (Button) findViewById(R.id.button_gamemodeReverseInfo);
        mButtonGamemodeSprintHelp = (Button) findViewById(R.id.button_gamemodeSprintInfo);
        mButtonGamemodeReverse = (Button) findViewById(R.id.button_gamemodeReverse);
        mButtonGamemodeSprint = (Button) findViewById(R.id.button_gamemodeSprint);

        // onClickListener for the button to launch the STANDARD Color Clicker game mode
        mButtonGamemodeStandard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(GamemodeSelector.this, StandardGame.class);
                startActivity(myIntent);
            }
        });

        // onClickListener for the button to describe the STANDARD game mode
        mButtonGamemodeStandardHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(GamemodeSelector.this, StandardGame.class);
                startActivity(myIntent);
            }
        });

        // onClickListener for the button to launch the REVERSE Color Clicker game mode
        mButtonGamemodeReverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(GamemodeSelector.this, StandardGame.class);
                startActivity(myIntent);
            }
        });

        // onClickListener for the button to describe the REVERSE game mode
        mButtonGamemodeReverseHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(GamemodeSelector.this, StandardGame.class);
                startActivity(myIntent);
            }
        });

        // onClickListener for the button to launch the SPRINT Color Clicker game mode
        mButtonGamemodeSprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(GamemodeSelector.this, StandardGame.class);
                startActivity(myIntent);
            }
        });

        // onClickListener for the button to describe the SPRINT game mode
        mButtonGamemodeSprintHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(GamemodeSelector.this, StandardGame.class);
                startActivity(myIntent);
            }
        });
    }
}
