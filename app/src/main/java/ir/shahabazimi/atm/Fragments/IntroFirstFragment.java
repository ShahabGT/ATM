package ir.shahabazimi.atm.Fragments;


import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.Locale;

import ir.shahabazimi.atm.Classes.MySharedPreference;
import ir.shahabazimi.atm.Activities.IntroActivity;
import ir.shahabazimi.atm.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntroFirstFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private ImageView logo;
    private Spinner spinner;
    int reset=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_intro_first, container, false);
        init(v);
        return v;
    }

    private void init(View v){
        logo = v.findViewById(R.id.first_img);
        spinner = v.findViewById(R.id.fif_spinner);
        animation();

        spinnerSetUp();
    }


    private void spinnerSetUp(){

        String[] items = new String[]{"English", "فارسی"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);

        spinner.setAdapter(adapter);
        spinner.setSelected(false);
        if(MySharedPreference.getInstance(getActivity()).getLanguage().equals("fa"))
            spinner.setSelection(1,true);
        else
            spinner.setSelection(0,true);
        spinner.setOnItemSelectedListener(this);

    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        switch (position) {
            case 0:
                setAppLanguage("en");
                break;
            case 1:
                setAppLanguage("fa");
                break;
        }
    }
    public void setAppLanguage(String languageToLoad){
        MySharedPreference.getInstance(getActivity()).setLanguage(languageToLoad);
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(config,
                getActivity().getBaseContext().getResources().getDisplayMetrics());

        Intent intent = new Intent(getContext(), IntroActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void animation(){
        ObjectAnimator scaleXAnimator =  ObjectAnimator.ofFloat(
                logo,
                "ScaleX",
                0f,1.5f,1f
        );
        ObjectAnimator scaleYAnimator =  ObjectAnimator.ofFloat(
                logo,
                "ScaleY",
                0f,1.5f,1f
        );
        scaleXAnimator.setDuration(1000);
        scaleYAnimator.setDuration(1000);
        scaleXAnimator.start();
        scaleYAnimator.start();
    }




}
