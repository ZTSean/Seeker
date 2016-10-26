package com.gamedesign.seeker;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Zixiao on 10/17/2016.
 */

public class FloatingMenuFragment extends android.support.v4.app.Fragment {

    private int status; // Google Play Services status: whether it is available
    private static final int CAMERA_PIC_REQUEST = 1;
    private static final int MAP_ROUTE_REQUEST = 2;
    private static final int PLACE_PICKER_REQUEST = 3;
    private FloatingActionMenu menu;

    // member of all buttons
    private FloatingActionButton fab_edit;
    private FloatingActionButton fab_map;
    private FloatingActionButton fab_camera;

    // Temporary image store field
    private ImageView imageView;
    private String mCurrentPhotoPath;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.floating_menu_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Found corresponding view in layout file
        imageView = (ImageView) view.findViewById(R.id.imagePreview);
        menu = (FloatingActionMenu) view.findViewById(R.id.floating_menu);

        fab_edit = (FloatingActionButton) view.findViewById(R.id.fab_edit);
        fab_camera = (FloatingActionButton) view.findViewById(R.id.fab_camera);
        fab_map = (FloatingActionButton) view.findViewById(R.id.fab_map);

        menu.hideMenuButton(false);

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
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.fab_edit:
                    break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST && requestCode == RESULT_OK) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            // TODO: get full size photo, data from get extras is good for an icon but not a lot more.
            imageView.setImageBitmap(image);
        }
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
