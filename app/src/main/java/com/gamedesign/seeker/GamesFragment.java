package com.gamedesign.seeker;

import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by Zixiao on 10/25/2016.
 */

public class GamesFragment extends android.support.v4.app.Fragment {
    private DataBaseHelper dbHelper;
    public static final String GAME_ID = "game_id";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.games_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Handle navigation view item clicks here.
        final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

        // initialize database
        dbHelper = new DataBaseHelper(getActivity().getApplicationContext());

        // Add click listener: Create new game
        FloatingActionButton addnewgame = (FloatingActionButton) view.findViewById(R.id.fab);
        addnewgame.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Create new name for new game
                int count = dbHelper.getAllGames().size() + 1;
                String newName = "game_" + Integer.toString(count);

                // Start new game
                Game g = new Game(newName);

                // Create new "Game" entry in database
                long game_id = dbHelper.insertGame(g);

                // Add arguments to fragment
                ThemeFragment tmp = new ThemeFragment();
                Bundle args = new Bundle();
                args.putLong(GAME_ID, game_id);
                tmp.setArguments(args);

                // Go to next fragment: choose theme
                ft.replace(R.id.fragment, tmp).addToBackStack(null).commit();
                Log.d("Transaction", "Create New Game.");
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
