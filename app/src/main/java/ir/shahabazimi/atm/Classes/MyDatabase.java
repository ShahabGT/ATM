package ir.shahabazimi.atm.Classes;

import androidx.room.Room;
import android.content.Context;

import ir.shahabazimi.atm.Database.MyRoomDatabase;

public class MyDatabase {
    private static MyDatabase instanse=null;
    private MyRoomDatabase myRoomDatabase;


    public static MyDatabase getInstance(Context context){
        if(instanse==null){
            instanse =  new MyDatabase(context);
        }

        return instanse;

    }


    private MyDatabase(Context context){

        myRoomDatabase = Room.databaseBuilder(context,MyRoomDatabase.class,"ATMDB").allowMainThreadQueries().build();


    }
}
