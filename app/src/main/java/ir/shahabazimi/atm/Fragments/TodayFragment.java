package ir.shahabazimi.atm.Fragments;


import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import ir.shahabazimi.atm.Adapters.HistoryAdapter;
import ir.shahabazimi.atm.Classes.DateConverter;
import ir.shahabazimi.atm.Classes.MySharedPreference;
import ir.shahabazimi.atm.Classes.MyToast;
import ir.shahabazimi.atm.Database.Activities;
import ir.shahabazimi.atm.Database.History;
import ir.shahabazimi.atm.Database.MyRoomDatabase;
import ir.shahabazimi.atm.R;


public class TodayFragment extends Fragment implements com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog.OnDateSetListener {

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private MyRoomDatabase myRoomDatabase;
    private MaterialSearchView searchView;
    private Calendar myCalendar;
    private Button datePicker;
    private com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog persianDatePickerDialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_today, container, false);


        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View v){
        myRoomDatabase = Room.databaseBuilder(getActivity(),MyRoomDatabase.class,"ATMDB").allowMainThreadQueries().build();
        myCalendar = Calendar.getInstance();
        recyclerView = v.findViewById(R.id.today_recycler);
        searchView = getActivity().findViewById(R.id.stat_search_view);
        datePicker = v.findViewById(R.id.today_datepicker);

        adapter = new HistoryAdapter(getActivity(),getData(Calendar.getInstance()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.search(newText);
                return true;
            }
        });

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (MySharedPreference.getInstance(getContext()).getLanguage()){
                    case "en":
                        new DatePickerDialog(getContext(), date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                        break;

                    case "fa":
                        persianDatePickerDialog.show(getActivity().getFragmentManager(), "Datepickerdialog");
                        break;
                }

            }
        });

        PersianCalendar persianCalendar = new PersianCalendar();
        persianDatePickerDialog = com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog.newInstance(
                this ,
                persianCalendar.getPersianYear(),
                persianCalendar.getPersianMonth(),
                persianCalendar.getPersianDay()
        );

    }
    private List<History> getData(Calendar calendar){
        List<History> list = new ArrayList<>();
        List<Activities> activitiesList;
        activitiesList = myRoomDatabase.myDao().getActivities();
        SimpleDateFormat ndf = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
        String today = ndf.format(calendar.getTime());
        History history;

        for(int i=0;i<activitiesList.size();i++) {

            history = new History();

            String total = myRoomDatabase.myDao().getTodayData(today, activitiesList.get(i).getName());
            if (total!=null && !total.isEmpty()) {

                history.setTime(total);
                history.setActivityname(activitiesList.get(i).getName());
                history.setId(i);
                list.add(history);
            }
        }

            return list;

        }
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };
    private void updateLabel() {
        List<History> historyList = getData(myCalendar);
        if(historyList.size()<1)
            MyToast.Create(getContext(),getString(R.string.no_data));
        adapter = new HistoryAdapter(getActivity(),historyList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        datePicker.setText(sdf.format(myCalendar.getTime()));

    }

    @Override
    public void onDateSet(com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String persianDate = year+"/"+(monthOfYear+1)+"/"+dayOfMonth;
        Log.d("date", "onDateSet: persian: "+persianDate);

        DateConverter converter = new DateConverter();
        converter.persianToGregorian(year, monthOfYear, dayOfMonth);
        String gregorianDate = converter.getYear()+"/"+converter.getMonth()+"/"+converter.getDay();
        Log.d("date", "onDateSet: gregorian: "+gregorianDate);

        myCalendar.set(Calendar.YEAR,  converter.getYear());
        myCalendar.set(Calendar.MONTH,  converter.getMonth());
        myCalendar.set(Calendar.DAY_OF_MONTH,  converter.getDay()-1);
        List<History> historyList = getData(myCalendar);
        if(historyList.size()<1)
            MyToast.Create(getContext(),getString(R.string.no_data));
        adapter = new HistoryAdapter(getActivity(),historyList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        datePicker.setText(persianDate);
    }
}
