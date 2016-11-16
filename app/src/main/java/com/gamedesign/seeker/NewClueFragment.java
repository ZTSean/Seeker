package com.gamedesign.seeker;

import android.app.AlertDialog;
import android.app.Fragment;
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

import org.w3c.dom.Text;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Zixiao on 10/17/2016.
 */

public class NewClueFragment extends android.support.v4.app.Fragment {

    private int status; // Google Play Services status: whether it is available
    private static final int CAMERA_PIC_REQUEST = 1;
    private static final int MAP_ROUTE_REQUEST = 2;
    private static final int PLACE_PICKER_REQUEST = 3;
    private FloatingActionMenu menu;

    // member of all buttons
    private FloatingActionButton fab_edit;
    private FloatingActionButton fab_map;
    private FloatingActionButton fab_camera;
    private Button button_done;

    private Clue clue;
    private long game_id;
    private AlertDialog ad;

    // Temporary image store field
    private ImageView imageView;
    private String mCurrentPhotoPath;

    private DataBaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_clue_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Found corresponding view in layout file
        menu = (FloatingActionMenu) view.findViewById(R.id.floating_menu);

        fab_edit = (FloatingActionButton) view.findViewById(R.id.fab_edit);
        fab_camera = (FloatingActionButton) view.findViewById(R.id.fab_camera);
        fab_map = (FloatingActionButton) view.findViewById(R.id.fab_map);

        menu.hideMenuButton(false);
        menu.setClosedOnTouchOutside(true);

        final FragmentManager ft = getActivity().getSupportFragmentManager();
        Bundle args = getArguments();
        dbHelper = new DataBaseHelper(getActivity().getApplicationContext());
        clue = new Clue();

        Boolean isNew = args.getBoolean("isNew");
        if (isNew) {

        } else {
            clue.setHint(args.getString(NewGameFragment.CLUE_HINT));
            clue.setSpot_name(args.getString(NewGameFragment.CLUE_SPOT_NAME));
            clue.setSpot_addr(args.getString(NewGameFragment.CLUE_SPOT_ADDR));
            clue.setSpot_latitude(args.getString(NewGameFragment.CLUE_SPOT_LAT));
            clue.setSpot_longitude(args.getString(NewGameFragment.CLUE_SPOT_LNG));
            clue.setImage_path(args.getString(NewGameFragment.CLUE_IMG_PATH));
            clue.setSpot_order(args.getInt(NewGameFragment.CLUE_ORDER));

            updateClueView(clue);
        }
        game_id = args.getLong(GamesFragment.GAME_ID);

        // Hint input dialog builder

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter Hint");
        final EditText input = new EditText(getActivity().getApplicationContext());
        input.setTextColor(Color.parseColor("#212121"));
        builder.setView(input);
        final View v = view;

        // -- set positive button for dialog
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String txt = input.getText().toString();
                clue.setHint(txt);
                TextView hint = (TextView) v.findViewById(R.id.hint_clue);
                hint.setText(txt);
                Log.d("ClueWrite", "update hint to clue.");
            }
        });

        // -- set negative button for dialog
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // -- Create dialog
        ad = builder.create();

        // Finish button
        button_done = (Button) view.findViewById(R.id.done_clue);
        button_done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Add new clue to data base
                long clue_id = dbHelper.insertClue(clue, game_id);

                // return to previous fragment to add new clue
                ft.popBackStack();
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fab_map.setOnClickListener(clickListener);
        fab_camera.setOnClickListener(clickListener);
        fab_edit.setOnClickListener(clickListener);

        menu.showMenuButton(true);
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
            getPlaceFromPicker(data);
        }
    }

    private void getPlaceFromPicker (Intent data) {
        Place placePicked = PlacePicker.getPlace(getContext(), data);
        if (placePicked != null) {
            String spotname = placePicked.getName().toString();
            String spotlat = Double.toString(placePicked.getLatLng().latitude);
            String spotlng = Double.toString(placePicked.getLatLng().longitude);
            String spotaddr = placePicked.getAddress().toString();

            clue.setSpot_name(spotname);
            clue.setSpot_latitude(spotlat);
            clue.setSpot_latitude(spotlng);
            clue.setSpot_addr(spotaddr);

            View v = getView();
            TextView spotName = (TextView) v.findViewById(R.id.spot_name);
            spotName.setText(spotname);
            TextView spotAddr = (TextView) v.findViewById(R.id.spot_addr);
            spotAddr.setText(spotaddr);
            TextView latlng = (TextView) v.findViewById(R.id.spot_lat_and_long);
            latlng.setText(spotlat + ", " + spotlng);

            Log.d("ClueWrite", "update location information for clue.");
        } else {
            // TODO: deal with the situation that no place picked up
        }


    }

    public void updateClueView(Clue c) {
        View v = getView();

        ((TextView)v.findViewById(R.id.hint_clue)).setText(c.getHint());
        ((TextView)v.findViewById(R.id.spot_name)).setText(c.getSpot_name());
        ((TextView)v.findViewById(R.id.spot_addr)).setText(c.getSpot_addr());
        ((TextView)v.findViewById(R.id.spot_lat_and_long)).setText(c.getSpot_latitude() + ", " + c.getSpot_longitude());

        // TODO: update image hint
    }

    /*
    private File createTemporaryFile() throws IOException {
        // Create an image file name: JPEG_yyyyMMdd_HHmmss_
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        // Path for store image
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName, /* Prefix
                ".jpg",         /* suffix
                storageDir      /* directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file" + image.getAbsolutePath();
        return image;
    }*/
}
