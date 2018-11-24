package ir.shahabazimi.atm.Classes;

import android.app.Activity;
import androidx.room.Room;
import android.os.AsyncTask;

import java.util.List;

import ir.shahabazimi.atm.Data.DataApi;
import ir.shahabazimi.atm.Data.GetActivitiesController;
import ir.shahabazimi.atm.Data.GetHistoryController;
import ir.shahabazimi.atm.Data.GetTablesController;
import ir.shahabazimi.atm.Data.GetVersionController;
import ir.shahabazimi.atm.Database.Activities;
import ir.shahabazimi.atm.Database.History;
import ir.shahabazimi.atm.Database.MyRoomDatabase;
import ir.shahabazimi.atm.Database.Tables;
import ir.shahabazimi.atm.Listeners.UpdateListener;
import ir.shahabazimi.atm.Models.ActivitiesArrayModel;
import ir.shahabazimi.atm.Models.HistoryArrayModel;
import ir.shahabazimi.atm.Models.JsonResponseModel;
import ir.shahabazimi.atm.Models.TablesArrayModel;

public class AsyncClass extends AsyncTask<String,Void,String> {

    private Activity activity;
    private UpdateListener listener;
    private  MyRoomDatabase myRoomDatabase;
    private String tableName;

    public AsyncClass(Activity activity, UpdateListener listener){
        myRoomDatabase = Room.databaseBuilder(activity,MyRoomDatabase.class,"ATMDB").allowMainThreadQueries().build();
        this.activity=activity;
        this.listener=listener;
    }
    @Override
    protected String doInBackground(String... params) {
        switch (params[0]){

            case "first":
                getTables();
                break;

            case "update":
                tableName=params[1];
                getVersion(tableName);
                break;



            case "history":
                updateHistory();
                break;


        }
        return null;
    }
    private void updateHistory(){


        GetHistoryController getHistoryController = new GetHistoryController(new DataApi.getHistoryListener() {
            @Override
            public void getMessage(boolean successful, HistoryArrayModel list) {
                if(successful){
                    List<History> histories = list.getList();
                    for(int i=0;i<histories.size();i++){
                        myRoomDatabase.myDao().addHistory(histories.get(i));

                    }
                    listener.updated(true);
                }else{
                    listener.updated(false);
                }

            }
        });
        getHistoryController.start(MySharedPreference.getInstance(activity).getUsername(),MySharedPreference.getInstance(activity).getPassword());
    }

    private void getActivites(){

        GetActivitiesController getActivitiesController = new GetActivitiesController(getActivitiesListener);
        getActivitiesController.start();

    }
    private void getTables(){

        GetTablesController getTablesController = new GetTablesController(getTablesListener);
        getTablesController.start();

    }
    private void getVersion(String table){

        GetVersionController getActivitiesController = new GetVersionController(getVersionListener);
        getActivitiesController.start(table);

    }
    private DataApi.getActivitiesListener getActivitiesListener = new DataApi.getActivitiesListener() {
        @Override
        public void getMessage(boolean successful, ActivitiesArrayModel activitiesArrayModel) {

            if(successful){
                List<Activities> list = activitiesArrayModel.getList();
                for(int i=0;i<list.size();i++)
                    myRoomDatabase.myDao().addActivity(list.get(i));
                listener.updated(true);

            }else{
                listener.updated(false);
            }

        }
    };
    private DataApi.getTablesListener getTablesListener = new DataApi.getTablesListener() {
        @Override
        public void getMessage(boolean successful, TablesArrayModel tablesArrayModel) {

            if(successful){
                List<Tables> list = tablesArrayModel.getList();
                for(int i=0;i<list.size();i++)
                    myRoomDatabase.myDao().addTabe(list.get(i));
                getActivites();

            }else{
               listener.updated(false);
            }

        }
    };

    private DataApi.getVersionListener getVersionListener = new DataApi.getVersionListener() {
        @Override
        public void getMessage(boolean successful, JsonResponseModel jsonResponseModel) {

            if(successful){
                int newVersion = Integer.parseInt(jsonResponseModel.getResponse());
                int oldVersion = myRoomDatabase.myDao().getVersion(tableName);
                if(newVersion>oldVersion)
                    getTables();

            }

        }
    };
}
