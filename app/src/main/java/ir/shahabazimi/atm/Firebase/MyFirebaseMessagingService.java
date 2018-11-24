package ir.shahabazimi.atm.Firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.room.Room;
import ir.shahabazimi.atm.Activities.IntroActivity;
import ir.shahabazimi.atm.Activities.MessagesActivity;
import ir.shahabazimi.atm.Classes.Const;
import ir.shahabazimi.atm.Database.Message;
import ir.shahabazimi.atm.Database.MyRoomDatabase;
import ir.shahabazimi.atm.R;



public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
     MyRoomDatabase myRoomDatabase = Room.databaseBuilder(this,MyRoomDatabase.class,"ATMDB").allowMainThreadQueries().build();

       String date = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.ENGLISH).format(new Date(remoteMessage.getSentTime()));
        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String body = data.get("body");
        String sender = data.get("sender");

     Message message = new Message();
     message.setBody(body);
     message.setDate(date);
     message.setSender(sender);
     message.setTitle(title);
     myRoomDatabase.myDao().addMessage(message);
     createNotification(title,body);

    }
 private void createNotification(String title, String message){

  Intent intent = new Intent(this,MessagesActivity.class);
  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
  PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
  Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
  createNotificationChannel();
  NotificationCompat.Builder builder = new NotificationCompat.Builder(this,Const.CHANNEL_CODE);
  builder.setSmallIcon(R.mipmap.ic_atm);
  builder.setContentTitle(getString(R.string.notificationTitle,title));
  builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_fmmessage));
  builder.setContentText(message);
  builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
  builder.setAutoCancel(true);
  builder.setContentIntent(pendingIntent);
  builder.setSound(alarmSound,AudioManager.STREAM_NOTIFICATION);
  builder.setVibrate(new long[] {1000,1000});
  NotificationManagerCompat notificationManagerCompat =  NotificationManagerCompat.from(this);
  notificationManagerCompat.notify(Const.NOTIFICATION_ID,builder.build());


 }


 private void createNotificationChannel(){
  if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
   CharSequence name = "ATM Message Notifications";
   String description = "Using this channel to display notification for atm app";
   int importance = NotificationManager.IMPORTANCE_DEFAULT;

   NotificationChannel notificationChannel = new NotificationChannel(Const.CHANNEL_CODE,name,importance);
   notificationChannel.setDescription(description);
   NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
   notificationManager.createNotificationChannel(notificationChannel);
  }
 }
}
