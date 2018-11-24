package ir.shahabazimi.atm.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;



@Database(entities = {Tables.class,Activities.class,History.class,Message.class},version = 1,exportSchema = false)
public abstract class MyRoomDatabase extends androidx.room.RoomDatabase {

    public abstract MyDao myDao();
}
