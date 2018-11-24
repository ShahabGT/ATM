package ir.shahabazimi.atm.Fragments;


import androidx.room.Room;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import ir.shahabazimi.atm.Adapters.ActivitiesAdapter;
import ir.shahabazimi.atm.Classes.MySharedPreference;
import ir.shahabazimi.atm.Database.MyRoomDatabase;
import ir.shahabazimi.atm.R;


public class HomeFragment extends Fragment {


    private RecyclerView recyclerView;
    private MyRoomDatabase myRoomDatabase;
    private ActivitiesAdapter adapter;
    private MaterialSearchView searchView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_home, container, false);
        init(v);

        return v;

    }
    private void init(View v){
        searchView = getActivity().findViewById(R.id.search_view);
        recyclerView = v.findViewById(R.id.home_recycler);
        myRoomDatabase = Room.databaseBuilder(getActivity(),MyRoomDatabase.class,"ATMDB").allowMainThreadQueries().build();
        loadActivities();



    }
    private void loadActivities(){
        adapter = new ActivitiesAdapter((AppCompatActivity) getActivity(),myRoomDatabase.myDao().getActivities());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(MySharedPreference.getInstance(getActivity()).getHaveActiveActivity()){
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container2,new ActivityFragment()).commit();
        }else {

        }

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
    }
}
