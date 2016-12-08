package com.gamedesign.seeker;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Zixiao on 10/25/2016.
 */

public class ChooseRoleFragment extends Fragment {
    public static final String IsPlayer = "is_player";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.choose_role_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Handle navigation view item clicks here.
        final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

        final Bundle args = new Bundle();
        final GamesFragment tmp = new GamesFragment();

        // Add click listener to change to creator mode
        Button creator = (Button) view.findViewById(R.id.creator);
        creator.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                args.putBoolean(IsPlayer, false);
                tmp.setArguments(args);
                ft.replace(R.id.fragment, tmp).addToBackStack(null).commit();
                ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8d5047")));

            }
        });

        // Add click listener to change to receiver mode
        Button player = (Button) view.findViewById(R.id.receiver);
        player.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // do whatever stuff you wanna do here
                args.putBoolean(IsPlayer, true);
                tmp.setArguments(args);
                ft.replace(R.id.fragment, tmp).addToBackStack(null).commit();
                ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5a6561")));
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
