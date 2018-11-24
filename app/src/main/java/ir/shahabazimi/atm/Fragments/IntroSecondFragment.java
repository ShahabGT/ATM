package ir.shahabazimi.atm.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import ir.shahabazimi.atm.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntroSecondFragment extends Fragment {
    GridLayout logo;


    public IntroSecondFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_intro_second, container, false);
        return v;

    }


}
