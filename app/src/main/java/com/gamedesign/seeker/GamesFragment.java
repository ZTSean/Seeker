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
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Zixiao on 10/25/2016.
 */

public class GamesFragment extends android.support.v4.app.Fragment {
    private DataBaseHelper dbHelper;
    private ListView gameViewList;
    private List<Game> mGamesList;
    private GamesAdapter adapter;
    private GameListAdapter gAdapter;
    public static final String GAME_ID = "game_id";
    public static final String GAME_NAME = "game_name";


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

        // Get game data from data base
        mGamesList = dbHelper.getAllGames();

        // List stuff handle ---------------------------------------
        gameViewList = (ListView) view.findViewById(R.id.games);
        /*
        adapter = new GamesAdapter(getActivity().getApplicationContext(), mGamesList);
        gameViewList.setAdapter(adapter);
        */


        gAdapter = new GameListAdapter(getActivity(), mGamesList, dbHelper, ft, getArguments());


        // list item click listener
        //gameViewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        /*
        gAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                // open game fragment

                Log.d("adapter", "#### gId passed into : " + Long.toString(gId));

                // load game fragment, although name is newgamefragment
                NewGameFragment tmp = new NewGameFragment();
                Bundle args = getArguments();
                Log.d("#####", Boolean.toString(args.getBoolean(ChooseRoleFragment.IsPlayer)));
                args.putString(GAME_NAME, gName);
                args.putLong(GAME_ID, gId);
                tmp.setArguments(args);

                ft.replace(R.id.fragment, tmp).addToBackStack(null).commit();

            }
        });
        */
        gameViewList.setAdapter(gAdapter);


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
                Log.d("adapter", "#### gId passed into : " + Long.toString(game_id));

                // Add arguments to fragment
                ThemeFragment tmp = new ThemeFragment();
                Bundle args = new Bundle();
                args.putString(GAME_NAME, newName);
                args.putLong(GAME_ID, game_id);
                tmp.setArguments(args);

                // Go to next fragment: choose theme
                ft.replace(R.id.fragment, tmp).addToBackStack(null).commit();
                Log.d("Transaction", "Create New Game.");
            }
        });

        // hide floating action bar if is player
        Bundle args = getArguments();
        Boolean isPlayer = args.getBoolean(ChooseRoleFragment.IsPlayer);
        if (isPlayer) {
            addnewgame.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
