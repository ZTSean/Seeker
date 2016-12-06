package com.gamedesign.seeker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.gamedesign.seeker.R;

/**
 * Created by Zixiao on 12/5/2016.
 */

public class AddHintActivity extends AppCompatActivity {
    public static String HINT_CONTENT = "hint_content";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hint);

        // Toolbar set
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText edt = (EditText) findViewById(R.id.clue_hint);
        // if it is not new
        String content = getIntent().getStringExtra(HINT_CONTENT);
        if (content != null) {
            edt.setText(content);
        }


        Button reset = (Button) findViewById(R.id.submit_hint);
        reset.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                String hint = edt.getText().toString();

                // send back to newcluefragment
                Intent intent = new Intent();
                intent.putExtra(HINT_CONTENT, hint);
                setResult(RESULT_OK, intent);
                finish();

            }
        });
    }
}
