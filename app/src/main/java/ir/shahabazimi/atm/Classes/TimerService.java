package ir.shahabazimi.atm.Classes;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import java.util.Timer;
import java.util.TimerTask;

public class TimerService extends Service {

    private Timer timer;
    private TimerTask timerTask;
    private Context context;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startTimer();
        return START_STICKY;
    }


    @Override
    public boolean stopService(Intent name) {
        stoptimertask();
        return super.stopService(name);

    }

    @Override
    public void onDestroy() {
        stoptimertask();
        super.onDestroy();
    }


    public void startTimer() {

        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 1000, 1000);
    }


    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Intent intent = new Intent("timeBroadcast");
                long data = System.currentTimeMillis()-MySharedPreference.getInstance(context).getStartTime();
                intent.putExtra("millis",data);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

            }
        };
    }


    public void stoptimertask() {
        MySharedPreference.getInstance(context).stopTimer();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
