package com.gamedesign.seeker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Zixiao on 11/17/2016.
 */

public class PlayerClueFragment extends android.support.v4.app.Fragment{

    private static final int CAMERA_PIC_REQUEST = 1;
    private static final int MAP_ROUTE_REQUEST = 2;
    private static final int PLACE_PICKER_REQUEST = 3;
    private static final int GET_ANSWER_REQUEST = 1004;

    private Clue clue;
    private long game_id;
    private AlertDialog ad;

    // Temporary image store field
    private ImageView imageView;
    private String mCurrentPhotoPath;
    private Button button_open_ar;

    private DataBaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.player_clue_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final FragmentManager ft = getActivity().getSupportFragmentManager();
        Bundle args = getArguments();
        dbHelper = new DataBaseHelper(getActivity().getApplicationContext());
        clue = new Clue();

        game_id = args.getLong(GamesFragment.GAME_ID);

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
        Button solve_button = (Button) view.findViewById(R.id.solve_clue);
        solve_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent getAnswer = new Intent(getActivity(), AnswerActivity.class);
                startActivityForResult(getAnswer, GET_ANSWER_REQUEST);
            }
        });

        // determine whether is correct answer or not ----------------------------------------
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Is Answer Correct?");
        final TextView input = new TextView(getActivity());
        builder.setView(input);
        final View v = getView();

        // -- set positive button for dialog
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Done with this clue, go to next clue
                ft.popBackStack();
            }
        });

        // -- set negative button for dialog
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Incorrect answer!", Toast.LENGTH_SHORT).show();
            }
        });

        // -- Create dialog
        ad = builder.create();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_ANSWER_REQUEST && resultCode == RESULT_OK) {
            // Jump up whether it is correct dialog
            ad.show();
        }
    }
}
