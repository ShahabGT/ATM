package ir.shahabazimi.atm.Fragments;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import androidx.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.ValueLineSeries;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import ir.shahabazimi.atm.Activities.MainActivity;
import ir.shahabazimi.atm.Classes.Const;
import ir.shahabazimi.atm.Classes.MySharedPreference;
import ir.shahabazimi.atm.Classes.MyUtils;
import ir.shahabazimi.atm.Classes.TimerService;
import ir.shahabazimi.atm.Data.DataApi;
import ir.shahabazimi.atm.Data.SendRoomDataController;
import ir.shahabazimi.atm.Database.History;
import ir.shahabazimi.atm.Database.MyRoomDatabase;
import ir.shahabazimi.atm.Models.JsonResponseModel;
import ir.shahabazimi.atm.R;


public class ActivityFragment extends Fragment {

    private TextView name,timer;
    private Button start,stop;
    private SimpleDraweeView pic;
    private LinearLayout linearLayout,selectLinear;
    private MyRoomDatabase myRoomDatabase;
    private FrameLayout frameLayout;
    private BarChart mBarChart;
    private ImageView selectImg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_activity, container, false);

        init(v);

        return v;
    }

    private void  init(View v){
        myRoomDatabase = Room.databaseBuilder(getActivity(),MyRoomDatabase.class,"ATMDB").allowMainThreadQueries().build();
        mBarChart = v.findViewById(R.id.barchart);
        stop = v.findViewById(R.id.activity_stop);
        start = v.findViewById(R.id.activity_start);
        timer = v.findViewById(R.id.activity_timer);
        name = v.findViewById(R.id.activity_name);
        pic = v.findViewById(R.id.activity_pic);
        selectImg = v.findViewById(R.id.activity_select_img);
        linearLayout = v.findViewById(R.id.activity_linear);
        selectLinear = v.findViewById(R.id.activity_select);
        frameLayout = getActivity().findViewById(R.id.main_container);
        haveActiveActivity();
        onClicks();
        checkStat();
        getStats();
    }
    private void selectImgAnimation(boolean start){

        ObjectAnimator trasntaltionAnim = ObjectAnimator.ofFloat(
                selectImg,
                "TranslationY",
                -50f,50f
        );
        trasntaltionAnim.setDuration(500);
        trasntaltionAnim.setRepeatCount(ValueAnimator.INFINITE);
        trasntaltionAnim.setRepeatMode(ValueAnimator.REVERSE);
        if(start) trasntaltionAnim.start();
        else trasntaltionAnim.cancel();



    }

    private void haveActiveActivity(){
        if(!MySharedPreference.getInstance(getContext()).getActiveActivityName().isEmpty()){
            linearLayout.setVisibility(View.VISIBLE);
            selectLinear.setVisibility(View.GONE);
            selectImgAnimation(false);
            name.setText(MySharedPreference.getInstance(getContext()).getActiveActivityName());
            pic.setImageURI(Uri.parse(MySharedPreference.getInstance(getContext()).getActiveActivityPic()));
        }else {
            linearLayout.setVisibility(View.GONE);
            selectLinear.setVisibility(View.VISIBLE);
            selectImgAnimation(true);
        }
    }
    private void checkStat(){
        if( MySharedPreference.getInstance(getContext()).getHaveActiveActivity()){
            frameLayout.setVisibility(View.GONE);
            start.setEnabled(false);
            start.setBackground(getResources().getDrawable(R.drawable.graybuttonbg));
            stop.setBackground(getResources().getDrawable(R.drawable.buttonbg));
            stop.setEnabled(true);
        }else {
            frameLayout.setVisibility(View.VISIBLE);
            start.setEnabled(true);
            start.setBackground(getResources().getDrawable(R.drawable.greenbuttonbg));
            stop.setEnabled(false);
            stop.setBackground(getResources().getDrawable(R.drawable.graybuttonbg));
        }
    }

    private void onClicks(){
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopActivity();
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity();
            }
        });

    }

    private void startActivity(){
        frameLayout.setVisibility(View.GONE);
        MySharedPreference.getInstance(getContext()).setHaveActiveActivity(true);
        start.setEnabled(false);
        start.setBackground(getResources().getDrawable(R.drawable.graybuttonbg));
        stop.setBackground(getResources().getDrawable(R.drawable.buttonbg));
        stop.setEnabled(true);
        long starttime = System.currentTimeMillis();
        MySharedPreference.getInstance(getContext()).setStartTime(starttime);
        Intent intent = new Intent(getContext(),TimerService.class);
        getActivity().startService(intent);
        createNotification(name.getText().toString(),getString(R.string.notificationMessage));
    }

    private void stopActivity(){
        frameLayout.setVisibility(View.VISIBLE);
        start.setEnabled(true);
        stop.setEnabled(false);
        start.setBackground(getResources().getDrawable(R.drawable.greenbuttonbg));
        stop.setBackground(getResources().getDrawable(R.drawable.graybuttonbg));
        Intent intent = new Intent(getContext(),TimerService.class);
        getContext().stopService(intent);
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(Const.NOTIFICATION_ID);
        MySharedPreference.getInstance(getContext()).setHaveActiveActivity(false);
        MySharedPreference.getInstance(getContext()).setActiveActivityName("");

        MySharedPreference.getInstance(getContext()).setActiveActivityPic("");
        checkStat();
        if(MyUtils.getInstance(getActivity()).checkInternet())
            addToServerDB();
        else
        addToLocalDB(0);

    }



    private void addToLocalDB(int uplodaded){
        History history = new History();
        history.setActivityname(MySharedPreference.getInstance(getActivity()).getActiveActivityNameEn());
        history.setTime(timer.getText().toString());
        history.setUsername(MySharedPreference.getInstance(getActivity()).getUsername());
        history.setUploaded(uplodaded);
        history.setStartdate(MySharedPreference.getInstance(getActivity()).getStartDate());
        myRoomDatabase.myDao().addHistory(history);
        MySharedPreference.getInstance(getContext()).setActiveActivityNameEn("");
    }
    private void addToServerDB(){
        final History history = new History();
        history.setActivityname(MySharedPreference.getInstance(getActivity()).getActiveActivityNameEn());
        history.setTime(timer.getText().toString());
        history.setUsername(MySharedPreference.getInstance(getActivity()).getUsername());
        history.setStartdate(MySharedPreference.getInstance(getActivity()).getStartDate());
        SendRoomDataController sendRoomDataController = new SendRoomDataController(new DataApi.sendRoomDataListener() {
            @Override
            public void getMessage(boolean successful, JsonResponseModel message) {
                if(successful){
                    if(message.getResponse().equals("success")){
                        addToLocalDB(1);
                    }else {
                        addToLocalDB(0);
                    }
                }else {
                    addToLocalDB(0);
                }

            }
        });
        sendRoomDataController.start(history.getUsername(),MySharedPreference.getInstance(getContext()).getPassword(),history.getActivityname(),history.getStartdate(),history.getTime());
    }
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long millisUntilFinished = intent.getLongExtra("millis",0);
            timer.setText(convertToTimeFormat(millisUntilFinished));
        }
    };
    private String convertToTimeFormat(long millisecond){
        Date date = new Date(millisecond);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss",Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(date);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver,new IntentFilter("timeBroadcast"));
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);

        super.onPause();
    }

    private void createNotification(String title, String message){

        Intent intent = new Intent(getContext(),MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(),Const.CHANNEL_CODE);
        builder.setSmallIcon(R.mipmap.ic_atm);
        builder.setContentTitle(getString(R.string.notificationTitle,title));
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_clock));
        builder.setContentText(message);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setUsesChronometer(true);
        builder.setOngoing(true);
        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat =  NotificationManagerCompat.from(getContext());

        notificationManagerCompat.notify(Const.NOTIFICATION_ID,builder.build());


    }


    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name = "ATM Activities Notifications";
            String description = "Using this channel to display notification for atm app";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(Const.CHANNEL_CODE,name,importance);
            notificationChannel.setDescription(description);
            NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void getStats(){
        Locale locale= new Locale("en");
        switch (MySharedPreference.getInstance(getActivity()).getLanguage()){
            case "en":
                locale = new Locale("en");
                break;

            case "fa":
                locale = new Locale("fa");
                break;
        }
        ValueLineSeries series = new ValueLineSeries();
        series.setColor(0xFF56B7F1);



        for(int i=1;i<8;i++) {
            float data;
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH,-i);
            String dofw = new SimpleDateFormat("EEE",locale).format(calendar.getTime());
            String today =  new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH).format(calendar.getTime());
            String dataString = myRoomDatabase.myDao().getStat(MySharedPreference.getInstance(getActivity()).getActiveActivityNameEn(),today);

            if(dataString==null) {
                data = 0f;
            }else {
                data= (Integer.parseInt(dataString.substring(0,2))*3600)+ (Integer.parseInt(dataString.substring(3,5))*60)+ (Integer.parseInt(dataString.substring(6,8)));
                data = Float.valueOf(String.format(Locale.ENGLISH,"%.2f", (data/3600)));
            }

            mBarChart.addBar(new BarModel(dofw,data, getResources().getColor(R.color.Blue_Gray_900)));
        }
        mBarChart.startAnimation();

    }


}
