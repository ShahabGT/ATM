package ir.shahabazimi.atm.Database;


import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MyDao {



    @Query("Select version from tables where name=:tableName")
    int getVersion(String tableName);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addActivity(Activities activities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addHistory(History history);

    @Query("SELECT * from history where uploaded=0")
    Cursor readHistory();


    @Query("SELECT * FROM activities")
    List<Activities> getActivities();

    @Query("SELECT * FROM messages")
    List<Message> getMessages();

    @Query("SELECT nameFa FROM activities where name=:name")
    String getActivityFaName(String name);

    @Query("DELETE FROM activities")
    void deleteActivities();

    @Query("DELETE FROM history")
    void deleteHistory();

    @Query("DELETE FROM tables")
    void deleteTables();

    @Query("SELECT time(sum(strftime('%s', time) - strftime('%s', '00:00:00')),'unixepoch') as total FROM history WHERE (julianday(:PreviousDayDate) - julianday(startdate))=0  AND activityname=:activityName;")
    String getTodayData(String PreviousDayDate, String activityName);

    @Query("SELECT time(sum(strftime('%s', time) - strftime('%s', '00:00:00')),'unixepoch') as total FROM history WHERE (julianday(:PreviousDayDate) - julianday(startdate))>-7  AND activityname=:activityName;")
    String getPreviousWeekData(String PreviousDayDate, String activityName);

    @Query("SELECT time(sum(strftime('%s', time) - strftime('%s', '00:00:00')),'unixepoch') as total FROM history WHERE (julianday(:PreviousDayDate) - julianday(startdate))>-31  AND activityname=:activityName;")
    String getPreviousMonthData(String PreviousDayDate, String activityName);

    @Query("SELECT time(sum(strftime('%s', time) - strftime('%s', '00:00:00')),'unixepoch') as total FROM history WHERE (julianday(:PreviousDayDate) - julianday(startdate))>-365  AND activityname=:activityName;")
    String getPreviousYearData(String PreviousDayDate, String activityName);

    @Query("SELECT time(sum(strftime('%s', time) - strftime('%s', '00:00:00')),'unixepoch') as total FROM history WHERE (julianday(startdate) - julianday('2018-11-01'))>0  AND activityname=:activityName;")
    String getAllData(String activityName);

    @Query("UPDATE history set uploaded=1 WHERE id=:id")
    void updateHistoryRow(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addTabe(Tables tables);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addMessage(Message message);

    @Query("SELECT time(sum(strftime('%s', time) - strftime('%s', '00:00:00')),'unixepoch') as total FROM History WHERE (julianday(:todayDate) - julianday(startdate))=0 AND activityname=:activity;")
    String getStat(String activity,String todayDate);

}
