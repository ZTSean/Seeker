package com.gamedesign.seeker;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Zixiao on 10/17/2016.
 */

public class NewClueFragment extends android.support.v4.app.Fragment {

    private int status; // Google Play Services status: whether it is available
    private static final int CAMERA_PIC_REQUEST = 1001;
    private static final int MAP_ROUTE_REQUEST = 1002;
    private static final int PLACE_PICKER_REQUEST = 1003;
    private static final int HINT_REQUEST = 1004;
    private static final int RADIO_REQUEST = 1005;
    private static final int AR_REQUEST = 1006;
    private FloatingActionMenu menu;

    // member of all buttons
    private FloatingActionButton fab_edit;
    private FloatingActionButton fab_map;
    private FloatingActionButton fab_camera;
    private FloatingActionButton fab_radio;
    private FloatingActionButton fab_ar;
    private Button button_done;

    private Clue clue;
    private long game_id;
    private AlertDialog ad;

    // Temporary image store field
    private ImageView imageView;
    private Uri mImageUri;
    private String imagePath = "";

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
        fab_ar = (FloatingActionButton) view.findViewById(R.id.fab_AR);
        fab_radio = (FloatingActionButton) view.findViewById(R.id.fab_label);

        menu.hideMenuButton(false);
        menu.setClosedOnTouchOutside(true);

        imageView = (ImageView) view.findViewById(R.id.image_clue);

        final FragmentManager ft = getActivity().getSupportFragmentManager();
        Bundle args = getArguments();
        dbHelper = new DataBaseHelper(getActivity().getApplicationContext());
        game_id = args.getLong(GamesFragment.GAME_ID);
        clue = new Clue();

        final Boolean isNew = args.getBoolean(NewGameFragment.IS_NEW_HINT);
        Log.d("-----", "isnew: " + Boolean.toString(isNew));
        if (!isNew) {
            // isn't new clue
            clue.setId(args.getInt(NewGameFragment.CLUE_ID));
            clue.setHint(args.getString(NewGameFragment.CLUE_HINT));
            clue.setSpot_name(args.getString(NewGameFragment.CLUE_SPOT_NAME));
            clue.setSpot_addr(args.getString(NewGameFragment.CLUE_SPOT_ADDR));
            clue.setSpot_latitude(args.getString(NewGameFragment.CLUE_SPOT_LAT));
            clue.setSpot_longitude(args.getString(NewGameFragment.CLUE_SPOT_LNG));
            clue.setImage_path(args.getString(NewGameFragment.CLUE_IMG_PATH));
            clue.setSpot_order(args.getInt(NewGameFragment.CLUE_ORDER));
            Log.d("------", "id: " + Integer.toString(clue.getId()));

            updateClueView(clue);
        }


        /* ---------------------- replaced by addhintactivity --------------------
        ft
        ------------------------------------------------------------------------*/

        // Finish button
        button_done = (Button) view.findViewById(R.id.done_clue);
        button_done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Add new clue to data base
                if (isNew) {
                    long clue_id = dbHelper.insertClue(clue, game_id);
                } else {
                    dbHelper.updateClue(clue);
                }


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
            Bundle args = getArguments();
            switch (v.getId()) {
                case R.id.fab_camera: // --------------- Camera
                    // new camera activity
                    Uri outputFileUri = Uri.fromFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM));
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                    File photo = null;
                    try
                    {
                        // place where to store camera taken picture
                        photo = createTemporaryFile("picture", ".jpg");
                        photo.delete();
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                        Log.v("------", "Can't create file to take picture!");
                        Toast.makeText(getActivity(), "Please check SD card! Image shot is impossible!", 10000);
                    }
                    mImageUri = Uri.fromFile(photo);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                    clue.setImage_path(mImageUri.getPath().toString());
                    Log.d("444444444", mImageUri.getPath().toString());



                    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                    break;
                case R.id.fab_map: // ------------------ Map
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
                    //ad.show();
                    Intent hintintent = new Intent(getActivity(), AddHintActivity.class);
                    if (!args.getBoolean(NewGameFragment.IS_NEW_HINT)) {
                        // is not new
                        if (!(clue.getHint().equals(Clue.NO_TEXT_HINT))) {
                            hintintent.putExtra(AddHintActivity.HINT_CONTENT, clue.getHint());
                        }
                    }
                    startActivityForResult(hintintent, HINT_REQUEST);
                    break;
                case R.id.fab_radio:
                    Intent radiointent = new Intent(getActivity(), AddRadioActivity.class);
                    startActivityForResult(radiointent, RADIO_REQUEST);
                    break;
                case R.id.fab_AR:
                    break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {
            //Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap) extras.get("data");
            grabImage(imageView, mImageUri);
            //imageView.setImageBitmap(imageBitmap);
            // TODO: get full size photo, data from get extras is good for an icon but not a lot more.
        } else if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            // Get location data picked by user
            getPlaceFromPicker(data);
        } else if (requestCode == HINT_REQUEST && resultCode == RESULT_OK) {
            // get hint content from addhintactivity
            String hint = data.getStringExtra(AddHintActivity.HINT_CONTENT);
            clue.setHint(hint);
            updateClueView(clue);
        } else if (requestCode == RADIO_REQUEST && resultCode == RESULT_OK) {

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

        if (!c.getImage_path().equals("")) {
            Uri imageUri = Uri.fromFile(new File(c.getImage_path()));
            grabImage((ImageView)v.findViewById(R.id.image_clue), imageUri);
        }


        // TODO: update image hint
    }


    public File createTemporaryFile(String part, String ext) throws Exception
    {
        File tempDir= Environment.getExternalStorageDirectory();
        imagePath = tempDir.getAbsolutePath()+"/.temp/";
        tempDir = new File(imagePath);
        Log.d("------", tempDir.getPath());
        if(!tempDir.exists())
        {
            tempDir.mkdirs();
        }
        return File.createTempFile(part, ext, tempDir);
    }

    public void grabImage(ImageView imageView, Uri imageUri)
    {
        getActivity().getContentResolver().notifyChange(imageUri, null);
        ContentResolver cr = getActivity().getContentResolver();
        Bitmap bitmap;
        try
        {
            Log.d("33333333333", imageUri.getPath());
            bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, imageUri);
            //imageView.setImageBitmap(bitmap);
        }
        catch (Exception e)
        {
            Toast.makeText(getActivity(), "Failed to load", Toast.LENGTH_SHORT).show();
            Log.d("----", "Failed to load", e);
        }
    }
}
