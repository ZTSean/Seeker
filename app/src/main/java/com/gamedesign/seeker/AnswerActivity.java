package com.gamedesign.seeker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.Date;
import java.util.Locale;

/**
 * Created by Zixiao on 12/5/2016.
 */

public class AnswerActivity extends AppCompatActivity {
    public static String CLUE_ANSWER = "answer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        // Toolbar set
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText answer = (EditText) findViewById(R.id.answer);
        Button submit_btn = (Button) findViewById(R.id.submit_button);
        submit_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(CLUE_ANSWER, answer.getText());
                setResult(RESULT_OK, intent);
                finish();
            }
        });



    }
}
