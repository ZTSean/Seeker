package com.gamedesign.seeker;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.util.Locale;

/**
 * Created by Zixiao on 11/17/2016.
 */

public class PlayerClueActivity extends AppCompatActivity{

    private static final int GET_ANSWER_REQUEST = 3001;
    private static final int PLAYER_PIC_REQUEST = 3002;

    private Clue clue;
    private long game_id;
    private AlertDialog ad;

    // Temporary image store field
    private ImageView imageView;
    private String mCurrentPhotoPath;

    private ImageButton button_radio;
    private ImageButton button_camera;
    private ImageButton button_edit;
    private ImageButton button_ar;
    private ImageButton button_locate;

    private DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_clue);

        // Toolbar set
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FragmentManager ft = getSupportFragmentManager();
        dbHelper = new DataBaseHelper(this);
        clue = new Clue();

        dbHelper = new DataBaseHelper(this);
        clue = dbHelper.getClue(getIntent().getIntExtra(NewGameFragment.CLUE_ID, -1));

        // set layout stuff
        String title = "Solve your No." + Integer.toString(clue.getId()) + " Clue";
        ((TextView) findViewById(R.id.hint_title)).setText(title);
        ((TextView) findViewById(R.id.hint_clue)).setText(clue.getHint());

        // load image
        ImageView imageView = (ImageView) findViewById(R.id.image_clue);
        File image = new File(clue.getImage_path());
        Uri mImageUri = Uri.fromFile(image);
        grabImage(imageView, mImageUri);

        // buttons setting up
        button_radio = (ImageButton) findViewById(R.id.button_radio);
        button_camera = (ImageButton) findViewById(R.id.button_camera);
        button_edit = (ImageButton) findViewById(R.id.button_edit);
        button_ar = (ImageButton) findViewById(R.id.button_ar);
        button_locate = (ImageButton) findViewById(R.id.button_locate);

        /*
        // Open AR button
        button_open_ar = (Button) view.findViewById(R.id.open_ar);
        button_open_ar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getActivity(), ARClueActivity.class);
                //startActivity(intent);
            }
        });
        */

        // Start solve clue button
        Button solve_button = (Button) findViewById(R.id.solve_clue);
        solve_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent getAnswer = new Intent(PlayerClueActivity.this, AnswerActivity.class);
                getAnswer.putExtra(NewGameFragment.CLUE_ID, clue.getId());
                startActivityForResult(getAnswer, GET_ANSWER_REQUEST);
            }
        });

        // determine whether is correct answer or not ----------------------------------------
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Is Answer Correct?");
        final TextView input = new TextView(this);
        builder.setView(input);

        // -- set positive button for dialog
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Done with this clue, go to next clue
                Intent intent = new Intent();
                intent.putExtra(NewGameFragment.CLUE_ID, clue.getId());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        // -- set negative button for dialog
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(PlayerClueActivity.this, "Incorrect answer!", Toast.LENGTH_SHORT).show();
            }
        });

        // -- Create dialog
        ad = builder.create();


        // ----------------------- Button functions -----------------------
        // input photo answer
        button_camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // new camera activity
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, PLAYER_PIC_REQUEST);
                finish();
            }
        });

        // see location hint
        button_camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // new map activity
                if (clue.getId() != -1) {
                    if ((!clue.getSpot_latitude().equals("")) && (!clue.getSpot_longitude().equals(""))) {
                        double lat = Double.parseDouble(clue.getSpot_latitude());
                        double lng = Double.parseDouble(clue.getSpot_longitude());

                        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", lat, lng);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        startActivity(intent);
                    }
                }

                /*
                Intent mapIntent = new Intent(AnswerActivity.this, MapsActivity.class);
                startActivity(mapIntent);*/

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_ANSWER_REQUEST && resultCode == RESULT_OK) {
            // Jump up whether it is correct dialog
            ad.show();
        } else if (requestCode == PLAYER_PIC_REQUEST && resultCode == RESULT_OK) {
            ad.show();
        }
    }

    /*
    @Override
    public void onBackPressed() {
    }

    */

    public void grabImage(ImageView imageView, Uri imageUri)
    {
        this.getContentResolver().notifyChange(imageUri, null);
        ContentResolver cr = this.getContentResolver();
        Bitmap bitmap;
        try
        {
            Log.d("33333333333", imageUri.getPath());
            bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, imageUri);
            imageView.setImageBitmap(bitmap);
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
            Log.d("----", "Failed to load", e);
        }
    }
}
