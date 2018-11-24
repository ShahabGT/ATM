package ir.shahabazimi.atm.Classes;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class PermissionUtil {
    private SharedPreferences sp;

    public PermissionUtil(Activity activity){

        sp = activity.getSharedPreferences("Permission",Context.MODE_PRIVATE);
    }

    public void update(String Permission){
        switch (Permission){
            case Const.READ_PHONE_STATE:
                sp.edit().putBoolean(Const.READ_PHONE_STATE,true).apply();
                break;
            case "call":
                sp.edit().putBoolean("call",true).apply();
                break;
        }
    }
    public boolean check(String Permission){
        boolean isRequested=false;
        switch (Permission){
            case Const.READ_PHONE_STATE:
                isRequested = sp.getBoolean(Const.READ_PHONE_STATE,false);
                break;
            case "call":
                isRequested = sp.getBoolean("call",false);
                break;
        }
        return isRequested;
    }
}
