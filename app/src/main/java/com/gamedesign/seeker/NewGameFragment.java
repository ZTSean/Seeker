package com.gamedesign.seeker;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Zixiao on 10/31/2016.
 */


public class NewGameFragment extends android.support.v4.app.Fragment {

    // Adapter and view for list items
    private ListView cluesViewList;
    private List<Clue> mCluesList;
    private CluesAdapter adapter;
    private DataBaseHelper dbHelper;
    private HashMap<Integer, Boolean> isEnabled;

    public static final String CLUE_ID = "clue_id";
    public static final String CLUE_HINT = "clue_hint";
    public static final String CLUE_SPOT_ADDR = "clue_spot_addr";
    public static final String CLUE_SPOT_LAT = "clue_spot_lat";
    public static final String CLUE_SPOT_LNG = "clue_spot_lng";
    public static final String CLUE_SPOT_NAME = "clue_spot_name";
    public static final String CLUE_IMG_PATH = "clue_img_path";
    public static final String CLUE_ORDER = "clue_order";

    public static final String IS_NEW_HINT = "isNew";

    public static final int PLAYER_CLUE_REQUEST = 2001;
    public static final int FINISH_GAME = 2002;

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
        Boolean isPlayer = getArguments().getBoolean(com.gamedesign.seeker.ChooseRoleFragment.IsPlayer);
        String game_name = getArguments().getString(GamesFragment.GAME_NAME);

        // Player: disable button for all incomplete clue
        isEnabled = new HashMap<>();

        // Get clue data from data base
        mCluesList = dbHelper.getAllCluesByGame(game_name);

        for (Clue c: mCluesList) {
            isEnabled.put(c.getId(), true);
        }

        // List stuff handle
        cluesViewList = (ListView) view.findViewById(R.id.clues);
        adapter = new CluesAdapter(getActivity(), mCluesList, isPlayer, isEnabled);
        cluesViewList.setAdapter(adapter);

        Log.d("#########", "New Game fragment " + isPlayer.toString());

        // Handle navigation view item clicks here. ==============================================================
        final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

        if (isPlayer) {
            cluesViewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Intent intent = new Intent(getActivity(), PlayerClueActivity.class);
                    Log.d("#########", "is player");
                    Clue c = mCluesList.get(position);
                    intent.putExtra(CLUE_ID, c.getId());
                    startActivityForResult(intent, PLAYER_CLUE_REQUEST);

                    Log.d("123456","gesgeadfawefawfs");

                }
            });

            // hide floating action button
            view.findViewById(R.id.fab).setVisibility(View.INVISIBLE);



        } else {
            cluesViewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    NewClueFragment tmp = new NewClueFragment();

                    Bundle args = getArguments();
                    Clue c = mCluesList.get(position);
                    args.putBoolean(IS_NEW_HINT, false);
                    args.putInt(CLUE_ID, c.getId());
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
        }



        // Add click listener: Create new game
        FloatingActionButton addnewclue = (FloatingActionButton) view.findViewById(R.id.fab);
        addnewclue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                NewClueFragment tmp = new NewClueFragment();
                Bundle args = getArguments();
                args.putBoolean(IS_NEW_HINT, true);
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

    public void updateClueView () {
        adapter.updateMap(isEnabled);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLAYER_CLUE_REQUEST && resultCode == RESULT_OK) {
            //set clue button to be disabled, next clue enabled
            int cId = data.getIntExtra(CLUE_ID, -1);
            if (cId != -1) {
                isEnabled.put(cId, false);
                // not completed
                int nextClue = 0;
                for (; nextClue < mCluesList.size(); nextClue++) {
                    if (mCluesList.get(nextClue).getId() == cId) {
                        break;
                    }
                }

                if (nextClue < mCluesList.size() - 1) {
                    // exist next clue
                    nextClue += 1;
                }

                Log.d("((((((((", Integer.toString(nextClue));
                if (isEnabled.get(nextClue) != null) {
                    isEnabled.put(nextClue, true);
                }

                // check all completed or not
                Boolean isCompleted = true;
                for (int key : isEnabled.keySet()) {
                    Log.d("))))))))))))))))", "Clue " + Integer.toString(key) + " " + isEnabled.get(key).toString());
                    if (isEnabled.get(key))
                    {
                        isCompleted = false;
                        break;
                    }
                }



                if (isCompleted) {
                    // show you finished and jump back to home page
                    Intent intent = new Intent(getActivity(), CongratsActivity.class);
                    startActivity(intent);
                }

                updateClueView();
            }
        } else if (requestCode == FINISH_GAME && resultCode == RESULT_OK){
            FragmentManager fragment = getActivity().getSupportFragmentManager();
            fragment.popBackStack();
        }
    }

    private void clearStack() {
        /*
         * Here we are clearing back stack fragment entries
         */
        int backStackEntry = getActivity().getSupportFragmentManager().getBackStackEntryCount();

        if (backStackEntry > 0) {
            for (int i = 0; i < backStackEntry; i++) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        }
    }
}
