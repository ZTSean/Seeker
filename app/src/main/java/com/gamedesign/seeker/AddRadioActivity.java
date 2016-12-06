package com.gamedesign.seeker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gamedesign.seeker.GamesFragment;
import com.gamedesign.seeker.R;

/**
 * Created by Zixiao on 12/5/2016.
 */

public class AddRadioActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_radio);

        // Toolbar set
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // store radio file into local
        Button record = (Button) findViewById(R.id.record_radio);
        record.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO: start record
            }
        });

        // store radio file into local
        Button reset = (Button) findViewById(R.id.reset_radio);
        reset.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO: reset radio
            }
        });


        // store radio file into local
        Button submit = (Button) findViewById(R.id.submit_radio);
        submit.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO: store radio
            }
        });
    }


}