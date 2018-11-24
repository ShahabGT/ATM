package ir.shahabazimi.atm.Fragments;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.content.Context;
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
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import ir.shahabazimi.atm.Activities.MainActivity;
import ir.shahabazimi.atm.Classes.Const;
import ir.shahabazimi.atm.Models.JsonResponseModel;
import ir.shahabazimi.atm.Classes.MySharedPreference;
import ir.shahabazimi.atm.Classes.MyToast;
import ir.shahabazimi.atm.Classes.MyUtils;
import ir.shahabazimi.atm.Classes.PermissionUtil;
import ir.shahabazimi.atm.Data.DataApi;
import ir.shahabazimi.atm.Data.RegisterUserController;
import ir.shahabazimi.atm.R;

public class RegisterFragment extends Fragment {

    private RadioGroup sex;
    private ImageView avatar;
    private EditText name,email,pass,repass,username;
    private TextInputLayout userLayout,passLayout,nameLayout,emailLayout,repassLayout;
    private Button register;
    private ProgressDialog progressDialog;

    private String n ;
    private String e ;
    private String p ;
    private String rp ;
    private  String u ;
    private String t;
    private String g;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        init(v);
        return v;
    }

    private void init(View v){
        sex= v.findViewById(R.id.reg_sex);
        avatar= v.findViewById(R.id.reg_avatar);
        name = v.findViewById(R.id.reg_name);
        email = v.findViewById(R.id.reg_email);
        pass = v.findViewById(R.id.reg_pass);
        repass = v.findViewById(R.id.reg_repass);
        username = v.findViewById(R.id.reg_user);
        userLayout = v.findViewById(R.id.reg_user_input);
        passLayout = v.findViewById(R.id.reg_pass_input);
        nameLayout = v.findViewById(R.id.reg_name_input);
        emailLayout = v.findViewById(R.id.reg_email_input);
        repassLayout = v.findViewById(R.id.reg_repass_input);
        register = v.findViewById(R.id.reg_reg);
        progressDialog = new ProgressDialog(getActivity());

        onClicks(v);

    }

    private void onClicks(final View onClickView){
        sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.reg_male:
                        avatar.setImageDrawable(getResources().getDrawable(R.drawable.male));
                        break;

                    case R.id.reg_female:
                        avatar.setImageDrawable(getResources().getDrawable(R.drawable.female));
                        break;
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.getInstance(getActivity()).hideKeyboard();
                 n = name.getText().toString();
                 e = email.getText().toString();
                 p = pass.getText().toString();
                 rp = repass.getText().toString();
                 u = username.getText().toString();

                if(!rp.equals(p) || p.length()<8){
                    passLayout.setError(getString(R.string.reg_pass_error));
                }else if(n.isEmpty()||n.length()<3){
                    nameLayout.setError(getString(R.string.reg_name_error));
                    passLayout.setErrorEnabled(false);
                }else if(e.isEmpty() || !MyUtils.getInstance(getActivity()).isEmailValid(e)){
                    emailLayout.setError(getString(R.string.reg_email_error));
                    passLayout.setErrorEnabled(false);
                    nameLayout.setErrorEnabled(false);
                }else if(u.isEmpty()||u.length()<5){
                    userLayout.setError(getString(R.string.reg_user_error));
                    passLayout.setErrorEnabled(false);
                    nameLayout.setErrorEnabled(false);
                    emailLayout.setErrorEnabled(false);
                }else{
                    nameLayout.setErrorEnabled(false);
                    emailLayout.setErrorEnabled(false);
                    userLayout.setErrorEnabled(false);
                    passLayout.setErrorEnabled(false);
                    if(!MyUtils.getInstance(getActivity()).checkInternet()){
                        MyToast.Create(getContext(),getString(R.string.internet_error));
                    }else{
                        int selectedId = sex.getCheckedRadioButtonId();
                        RadioButton radioButton =  onClickView.findViewById(selectedId);
                        t = FirebaseInstanceId.getInstance().getToken();
                        g = radioButton.getText().toString();

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
    private void readPhoneState() throws SecurityException{
            TelephonyManager telephonyManager = (TelephonyManager) getActivity().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

            String deviceid = telephonyManager.getDeviceId();
            if (deviceid == null) {
                deviceid = Build.SERIAL;
            }
        registerUser(n,u,e,p,t,deviceid,g);
    }
    private void showExplanation(final String Permission,final String title,final String message){
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
    private void registerUser(String name, String username, String email, String password, String token, String imei, String sex){

        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.progressdialogtext));
        progressDialog.show();
        RegisterUserController registerUserController = new RegisterUserController(registerUserListener);
        registerUserController.start(name,username,email,password,token,imei,sex);


    }
    private DataApi.registerUserListener registerUserListener = new DataApi.registerUserListener() {
        @Override
        public void getMessage(boolean successful, JsonResponseModel message) {
            progressDialog.dismiss();
            if(successful){
                switch (message.getResponse()){
                    case "successful":
                        FirebaseMessaging.getInstance().subscribeToTopic(Const.FCM_TOPIC);
                        MySharedPreference.getInstance(getActivity()).setUsername(u);
                        MySharedPreference.getInstance(getActivity()).setPassword(p);
                        MySharedPreference.getInstance(getActivity()).setName(n);
                        MySharedPreference.getInstance(getActivity()).setEmail(e);
                        MySharedPreference.getInstance(getActivity()).setSex(g);
                        MyToast.Create(getActivity(),getString(R.string.reg_success,u));
                        startActivity(new Intent(getContext(),MainActivity.class));
                        getActivity().finish();
                        break;
                    case "user exists":
                        MyToast.Create(getActivity(),getString(R.string.reg_user_exists));
                        break;
                    case "email exists":
                        MyToast.Create(getActivity(),getString(R.string.reg_email_exists));
                        break;
                    case "error":
                        MyToast.Create(getActivity(),getString(R.string.error));
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
