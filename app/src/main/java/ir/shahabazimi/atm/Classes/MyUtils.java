package ir.shahabazimi.atm.Classes;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ir.shahabazimi.atm.R;

public class MyUtils {

    private static MyUtils instance=null;
    private Activity context;


    public static MyUtils getInstance(Context context){
        if(instance==null){
            instance =  new MyUtils(context);
        }

        return instance;

    }



    private MyUtils(Context context){

        this.context=(Activity) context;
    }

    public void setAppLanguage(){

        String languageToLoad=MySharedPreference.getInstance(context).getLanguage();
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getBaseContext().getResources().updateConfiguration(config,
                context.getBaseContext().getResources().getDisplayMetrics());
    }
    public boolean checkInternet() {
        try {
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
            return activeInfo!=null && activeInfo.isConnected();

        }catch (NullPointerException e){return false;}

    }
    public int checkPermission(String Permission){
        int status = PackageManager.PERMISSION_DENIED;
        switch (Permission){
            case Const.READ_PHONE_STATE:
                status = ActivityCompat.checkSelfPermission(context,Manifest.permission.READ_PHONE_STATE);
                break;

        }
        return status;
    }
    public void hideKeyboard(){
        try {
            InputMethodManager inputManager = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }catch (Exception e){}
    }
    public boolean isEmailValid(String email)
    {
        Pattern pattern;
        boolean res;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        if(matcher.matches()){
            email=email.toLowerCase();
            if(email.contains("@yahoo.")||email.contains("@gmail.")||email.contains("@aol.")||email.contains("@hotmail.")||email.contains("@ymail.")||email.contains("@live.")){
                res= true;
            }else {
                res= false;
            }
        }else {res=false;}
        return  res;
    }
}
