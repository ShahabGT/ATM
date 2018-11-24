package ir.shahabazimi.atm.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.messaging.FirebaseMessaging;

import ir.shahabazimi.atm.Activities.MainActivity;
import ir.shahabazimi.atm.Classes.Const;
import ir.shahabazimi.atm.Classes.MySharedPreference;
import ir.shahabazimi.atm.Classes.MyToast;
import ir.shahabazimi.atm.Classes.MyUtils;
import ir.shahabazimi.atm.Data.DataApi;
import ir.shahabazimi.atm.Data.ResetPassController;
import ir.shahabazimi.atm.Models.JsonResponseModel;
import ir.shahabazimi.atm.R;


public class ResetPassFragment extends Fragment {
    private EditText email;
    private Button reset;
    private TextInputLayout emailLayout;
    private ProgressDialog progressDialog;






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reset_pass, container, false);

        init(v);

        return v;
    }

    private void init(View v){
        email = v.findViewById(R.id.reset_email);
        reset = v.findViewById(R.id.reset_reset);
        emailLayout = v.findViewById(R.id.reset_email_layout);
        progressDialog = new ProgressDialog(getActivity());


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e = email.getText().toString();

                if(MyUtils.getInstance(getContext()).isEmailValid(e)){
                    if(MyUtils.getInstance(getContext()).checkInternet())
                        resetPass(e);
                    else
                        MyToast.Create(getContext(),getString(R.string.internet_error));

                }else
                    emailLayout.setError(getString(R.string.reg_email_error));

            }
        });
    }
    private void resetPass(String email){
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.progressdialogtext));
        progressDialog.show();
        ResetPassController resetPassController = new ResetPassController(new DataApi.resetPassListener() {
            @Override
            public void getMessage(boolean successful, JsonResponseModel message) {
                progressDialog.dismiss();
                if(successful){
                    switch (message.getResponse()){
                        case "ok":
                            MyToast.Create(getContext(),getString(R.string.reset_ok));
                            getActivity().getSupportFragmentManager().popBackStack();
                            break;
                        case "notexist":
                            MyToast.Create(getActivity(),getString(R.string.reg_user_exists));
                            break;
                        case "error":
                            MyToast.Create(getActivity(),getString(R.string.error));
                            break;
                    }
                }else{
                    MyToast.Create(getActivity(),getString(R.string.error));
                }
            }
        });
        resetPassController.start(email);


    }

}
