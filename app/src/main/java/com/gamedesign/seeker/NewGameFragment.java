package com.gamedesign.seeker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

    public static final String CLUE_HINT = "clue_hint";
    public static final String CLUE_SPOT_ADDR = "clue_spot_addr";
    public static final String CLUE_SPOT_LAT = "clue_spot_lat";
    public static final String CLUE_SPOT_LNG = "clue_spot_lng";
    public static final String CLUE_SPOT_NAME = "clue_spot_name";
    public static final String CLUE_IMG_PATH = "clue_img_path";
    public static final String CLUE_ORDER = "clue_order";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_game_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // initialize database
        dbHelper = new DataBaseHelper(getActivity());
        Long game_id = getArguments().getLong(GamesFragment.GAME_ID);
        String game_name = getArguments().getString(GamesFragment.GAME_NAME);

        // Get clue data from data base
        mCluesList = dbHelper.getAllCluesByGame(game_name);

        // List stuff handle
        cluesViewList = (ListView) view.findViewById(R.id.clues);
        adapter = new CluesAdapter(getActivity(), mCluesList);
        cluesViewList.setAdapter(adapter);

        // Handle navigation view item clicks here. ==============================================================
        final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

        cluesViewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                NewClueFragment tmp = new NewClueFragment();

                Bundle args = getArguments();
                Clue c = mCluesList.get(position);
                args.putBoolean("isNew", false);
                args.putString(CLUE_HINT, c.getHint());
                args.putString(CLUE_SPOT_NAME, c.getSpot_name());
                args.putString(CLUE_SPOT_ADDR, c.getSpot_addr());
                args.putString(CLUE_SPOT_LAT, c.getSpot_latitude());
                args.putString(CLUE_SPOT_LNG, c.getSpot_longitude());
                args.putString(CLUE_IMG_PATH, c.getImage_path());
                args.putInt(CLUE_ORDER, c.getSpot_order());
                tmp.setArguments(getArguments());

                ft.replace(R.id.fragment, tmp).addToBackStack(null).commit();

            }
        });



        // Add click listener: Create new game
        FloatingActionButton addnewclue = (FloatingActionButton) view.findViewById(R.id.fab);
        addnewclue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                NewClueFragment tmp = new NewClueFragment();
                Bundle args = getArguments();
                args.putBoolean("isNew", true);
                tmp.setArguments(args); // pass game_id to next level fragment

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
