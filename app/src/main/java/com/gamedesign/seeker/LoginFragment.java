package com.gamedesign.seeker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Zixiao on 12/4/2016.
 */

public class LoginFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment
                , container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Handle navigation view item clicks here.
        final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

        Button login_button = (Button) view.findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ft.replace(R.id.fragment, new ChooseRoleFragment()).addToBackStack(null).commit();
            }
        });

    }
}
