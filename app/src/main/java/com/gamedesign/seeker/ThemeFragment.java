package com.gamedesign.seeker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Zixiao on 10/17/2016.
 */

public class ThemeFragment extends android.support.v4.app.Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.theme_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Handle navigation view item clicks here.


        // Add click listener: Create new game
        Button chooseRomantic = (Button) view.findViewById(R.id.Romantic);
        Button chooseCelebratory = (Button) view.findViewById(R.id.Celebratory);
        Button chooseSeasonal = (Button) view.findViewById(R.id.Seasonal);

        chooseRomantic.setOnClickListener(clickListener);
        chooseCelebratory.setOnClickListener(clickListener);
        chooseSeasonal.setOnClickListener(clickListener);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

            Bundle args = getArguments();
            NewGameFragment tmp = new NewGameFragment();
            tmp.setArguments(args);

            /*
            switch (v.getId()) {
                case R.id.Romantic:
                    ft.replace(R.id.fragment, tmp).commit();
                    break;
                case R.id.Celebratory:
                    ft.replace(R.id.fragment, tmp).commit();
                    break;
                case R.id.Seasonal:
                    ft.replace(R.id.fragment, tmp).commit();
                    break;
            }*/

            ft.replace(R.id.fragment, tmp).addToBackStack(null).commit();
        }
    };
}
