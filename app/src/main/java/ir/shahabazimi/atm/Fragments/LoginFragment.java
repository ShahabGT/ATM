package ir.shahabazimi.atm.Fragments;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import ir.shahabazimi.atm.Activities.MainActivity;
import ir.shahabazimi.atm.Classes.Const;
import ir.shahabazimi.atm.Models.JsonResponseModel;
import ir.shahabazimi.atm.Classes.MySharedPreference;
import ir.shahabazimi.atm.Classes.MyToast;
import ir.shahabazimi.atm.Classes.MyUtils;
import ir.shahabazimi.atm.Classes.PermissionUtil;
import ir.shahabazimi.atm.Data.DataApi;
import ir.shahabazimi.atm.Data.LoginUserController;
import ir.shahabazimi.atm.R;


public class LoginFragment extends Fragment {

    private Button login;
    private EditText user,pass;
    private TextInputLayout userLayout,passLayout;
    private TextView reset,register;
    private ProgressDialog progressDialog;

    private String p ;
    private  String u ;
    private String t;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        init(v);
        return v;

    }

    private void init(View v){

        login = v.findViewById(R.id.login_login);
        user = v.findViewById(R.id.login_user);
        pass = v.findViewById(R.id.login_pass);
        userLayout = v.findViewById(R.id.login_user_input);
        passLayout = v.findViewById(R.id.login_pass_input);
        reset = v.findViewById(R.id.login_resetpass);
        register = v.findViewById(R.id.login_register);
        progressDialog = new ProgressDialog(getContext());

        onClicks();
    }

    private void onClicks(){
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.login_container,new ResetPassFragment()).addToBackStack(null).commit();

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.login_container,new RegisterFragment()).addToBackStack(null).commit();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.getInstance(getActivity()).hideKeyboard();
                p = pass.getText().toString();
                u = user.getText().toString();
                if(u.isEmpty()){
                    userLayout.setError(getString(R.string.login_user_error));

                }else if(p.isEmpty()){
                    passLayout.setError(getString(R.string.login_pass_error));
                    userLayout.setErrorEnabled(false);
                }else{
                    userLayout.setErrorEnabled(false);
                    passLayout.setErrorEnabled(false);
                    if(!MyUtils.getInstance(getActivity()).checkInternet()){
                        MyToast.Create(getContext(),getString(R.string.internet_error));
                    }else{
                        t = FirebaseInstanceId.getInstance().getToken();
                        requestReadPhoneState();
                    }
                }

            }
        });
    }
    private void requestReadPhoneState(){
        PermissionUtil permissionUtil = new PermissionUtil(getActivity());

        if(MyUtils.getInstance(getActivity()).checkPermission(Const.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_PHONE_STATE)){
                showExplanation(Const.READ_PHONE_STATE,getString(R.string.read_phone_state_title),getString(R.string.read_phone_state_message));

            }

            else if(!permissionUtil.check(Const.READ_PHONE_STATE)){
                requestPermission(Const.READ_PHONE_STATE);
                permissionUtil.update(Const.READ_PHONE_STATE);
            }else{
                MyToast.Create(getActivity(),getString(R.string.permission_settings));
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package",getContext().getPackageName(),null);
                intent.setData(uri);
                startActivity(intent);
            }
        }else{
            readPhoneState();
        }

    }
    private  void showExplanation(final String Permission,final String title,final String message){
        AlertDialog.Builder builder;
        AlertDialog alertDialog;
        builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(getContext().getString(R.string.permission_allow), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestPermission(Permission);
            }
        });
        builder.setNegativeButton(getContext().getString(R.string.permission_deny), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog =builder.create();
        alertDialog.show();

    }
    private void requestPermission(String Permission){
        switch (Permission){
            case Const.READ_PHONE_STATE:
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        Const.READ_PHONE_STATE_CODE
                );
                break;
        }

    }
    private void readPhoneState() throws SecurityException{
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

        String deviceid = telephonyManager.getDeviceId();
        if (deviceid == null) {
            deviceid = Build.SERIAL;
        }
        registerUser(u,p,t,deviceid);
    }
    private void registerUser(String username, String password, String token, String imei){

        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.progressdialogtext));
        progressDialog.show();
        LoginUserController loginUserController = new LoginUserController(loginUserListener);
        loginUserController.start(username,password,token,imei);


    }
    private DataApi.loginUserListener loginUserListener = new DataApi.loginUserListener() {
        @Override
        public void getMessage(boolean successful, JsonResponseModel message) {
            progressDialog.dismiss();
            if(successful){
                switch (message.getResponse()){

                    case "user notexists":
                        MyToast.Create(getActivity(),getString(R.string.login_user_notexists));
                        break;
                    case "wrong pass":
                        MyToast.Create(getActivity(),getString(R.string.login_wrong_pass));
                        break;
                    case "error":
                        MyToast.Create(getActivity(),getString(R.string.error));
                        break;
                    default:

                            String[] res = message.getResponse().split(":::");
                            String n =res[0];
                            String e= res[1];
                            String g= res[2];

                            MySharedPreference.getInstance(getActivity()).setUsername(u);
                            MySharedPreference.getInstance(getActivity()).setPassword(p);
                            MySharedPreference.getInstance(getActivity()).setName(n);
                            MySharedPreference.getInstance(getActivity()).setEmail(e);
                            MySharedPreference.getInstance(getActivity()).setSex(g);
                            MyToast.Create(getActivity(), getString(R.string.login_success, u));
                            startActivity(new Intent(getContext(), MainActivity.class));
                        FirebaseMessaging.getInstance().subscribeToTopic(Const.FCM_TOPIC);

                        getActivity().finish();

                        break;

                }

            }else{
                MyToast.Create(getActivity(),getString(R.string.error));
            }

        }
    };



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case Const.READ_PHONE_STATE_CODE:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    readPhoneState();
                }else{
                    MyToast.Create(getActivity(),getString(R.string.permission_denied));
                }
        }
    }

}
