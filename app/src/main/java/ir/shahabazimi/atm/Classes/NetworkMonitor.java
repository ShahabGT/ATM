package ir.shahabazimi.atm.Classes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import androidx.room.Room;
import ir.shahabazimi.atm.Data.DataApi;
import ir.shahabazimi.atm.Data.SendRoomDataController;
import ir.shahabazimi.atm.Database.History;
import ir.shahabazimi.atm.Database.MyRoomDatabase;
import ir.shahabazimi.atm.Models.JsonResponseModel;

public class NetworkMonitor extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("BroadcastReceiver","ATMBroadcastReceiver: "+"onReceive");
        if(MyUtils.getInstance(context).checkInternet()){
            Log.d("BroadcastReceiver","ATMBroadcastReceiver: "+"internet");
            final MyRoomDatabase myRoomDatabase = Room.databaseBuilder(context,MyRoomDatabase.class,"ATMDB").allowMainThreadQueries().build();
            Cursor cursor =myRoomDatabase.myDao().readHistory();
            if(cursor.moveToFirst()){

                do{
                    final History history = new History();
                    history.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    history.setStartdate(cursor.getString(cursor.getColumnIndex("startdate")));
                    history.setActivityname(cursor.getString(cursor.getColumnIndex("activityname")).replace("_"," "));
                    history.setTime(cursor.getString(cursor.getColumnIndex("time")));
                    history.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                    SendRoomDataController sendRoomDataController = new SendRoomDataController(new DataApi.sendRoomDataListener() {
                        @Override
                        public void getMessage(boolean successful, JsonResponseModel message) {
                            if(successful){
                             if(message.getResponse().equals("success")){
                                 myRoomDatabase.myDao().updateHistoryRow(history.getId());
                             }
                            }

                        }
                    });
                    sendRoomDataController.start(history.getUsername(),MySharedPreference.getInstance(context).getPassword(),history.getActivityname(),history.getStartdate(),history.getTime());


                }while (cursor.moveToNext());
            }



        }else
        Log.d("BroadcastReceiver","ATMBroadcastReceiver: "+"no internet");
    }
}
