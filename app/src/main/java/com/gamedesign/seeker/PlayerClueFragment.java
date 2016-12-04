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

        // Open AR button
        button_open_ar = (Button) view.findViewById(R.id.open_ar);
        button_open_ar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ARClueActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab_camera:
                    // new camera activity
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                    break;
                case R.id.fab_map:
                    /*
                    Intent mapIntent = new Intent(getActivity().getApplicationContext(), MapsActivity.class);
                    startActivity(mapIntent);
                    */
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    try {
                        startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                        Log.d("ClueWrite", "Start Picking");
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.fab_edit:
                    ad.show();
                    break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            // TODO: get full size photo, data from get extras is good for an icon but not a lot more.
            imageView.setImageBitmap(image);
        } else if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            // Get location data picked by user
        }
    }
}
