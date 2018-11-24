package ir.shahabazimi.atm.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ir.shahabazimi.atm.Activities.LoginActivity;
import ir.shahabazimi.atm.Classes.AsyncClass;
import ir.shahabazimi.atm.Classes.MySharedPreference;
import ir.shahabazimi.atm.Classes.MyToast;
import ir.shahabazimi.atm.Classes.MyUtils;
import ir.shahabazimi.atm.Listeners.UpdateListener;
import ir.shahabazimi.atm.R;



public class IntroFourthFragment extends Fragment {

    private Button start;
    private ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_intro_fourth, container, false);
        init(v);
        return v;
    }

    private void init(View v){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.progressdialogtext));


        start = v.findViewById(R.id.ifb);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncClass asyncClass = new AsyncClass(getActivity(), new UpdateListener() {
                    @Override
                    public void updated(boolean done) {
                        progressDialog.dismiss();
                       if(done){
                           MySharedPreference.getInstance(getActivity()).setIsFirst(false);
                           startActivity(new Intent(getContext(),LoginActivity.class));
                           getActivity().finish();

                       }else {
                           MyToast.Create(getContext(),getString(R.string.error));

                       }
                    }
                });
                if(MyUtils.getInstance(getActivity()).checkInternet()) {
                    progressDialog.show();
                    asyncClass.execute("first");
                }
                else
                    MyToast.Create(getContext(),getString(R.string.internet_error));


            }
        });


    }

}
