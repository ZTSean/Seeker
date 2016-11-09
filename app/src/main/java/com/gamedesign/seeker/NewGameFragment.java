package com.gamedesign.seeker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

/**
 * Created by Zixiao on 10/31/2016.
 */

public class NewGameFragment extends android.support.v4.app.Fragment {

    // Adapter and view for list items
    private ListView cluesViewList;
    private List<Clue> mCluesList;
    private CluesAdapter adapter;
    private DataBaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_game_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // initialize database
        dbHelper = new DataBaseHelper(getActivity().getApplicationContext());
        Long game_id = getArguments().getLong(GamesFragment.GAME_ID);
        String game_name = getArguments().getString(GamesFragment.GAME_NAME);

        // Get clue data from data base
        mCluesList = dbHelper.getAllCluesByGame(game_name);

        // List stuff handle
        cluesViewList = (ListView) view.findViewById(R.id.clues);
        adapter = new CluesAdapter(getActivity().getApplicationContext(), mCluesList);
        cluesViewList.setAdapter(adapter);


        // Handle navigation view item clicks here. ==============================================================
        final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

        // Add click listener: Create new game
        FloatingActionButton addnewclue = (FloatingActionButton) view.findViewById(R.id.fab);
        addnewclue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                NewClueFragment tmp = new NewClueFragment();
                tmp.setArguments(getArguments()); // pass game_id to next level fragment

                // start new clue
                ft.replace(R.id.fragment, tmp).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
