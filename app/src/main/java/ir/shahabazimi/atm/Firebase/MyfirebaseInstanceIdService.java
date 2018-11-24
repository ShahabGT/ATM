package ir.shahabazimi.atm.Firebase;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

import ir.shahabazimi.atm.Classes.Const;
import ir.shahabazimi.atm.Classes.MySharedPreference;




public class MyfirebaseInstanceIdService extends FirebaseInstanceIdService {



    @Override
    public void onTokenRefresh() {
        String recent_token = FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().subscribeToTopic(Const.FCM_TOPIC);
        MySharedPreference.getInstance(this).setToken(recent_token);

    }
}
