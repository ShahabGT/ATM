package ir.shahabazimi.atm.Fragments;


import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import ir.shahabazimi.atm.Adapters.HistoryAdapter;
import ir.shahabazimi.atm.Classes.MyToast;
import ir.shahabazimi.atm.Database.Activities;
import ir.shahabazimi.atm.Database.History;
import ir.shahabazimi.atm.Database.MyRoomDatabase;
import ir.shahabazimi.atm.R;


public class AllTimeFragment extends Fragment {
    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private MyRoomDatabase myRoomDatabase;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_all_time, container, false);



        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View v){
        myRoomDatabase = Room.databaseBuilder(getActivity(),MyRoomDatabase.class,"ATMDB").allowMainThreadQueries().build();
        recyclerView = v.findViewById(R.id.alltime_recycler);
        List<History> historyList = getData();
        if(historyList.size()<1)
            MyToast.Create(getContext(),getString(R.string.no_data));
        adapter = new HistoryAdapter(getActivity(),historyList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);


    }
    private List<History> getData(){
        List<History> list = new ArrayList<>();
        List<Activities> activitiesList;
        activitiesList = myRoomDatabase.myDao().getActivities();
        History history;


        for(int i=0;i<activitiesList.size();i++) {

            history = new History();

            String total = myRoomDatabase.myDao().getAllData(activitiesList.get(i).getName());
            if (total!=null && !total.isEmpty()) {

                history.setTime(total);
                history.setActivityname(activitiesList.get(i).getName());
                history.setId(i);
                list.add(history);
            }
        }

        return list;

    }
}
