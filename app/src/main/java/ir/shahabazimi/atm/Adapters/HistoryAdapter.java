package ir.shahabazimi.atm.Adapters;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import ir.shahabazimi.atm.Classes.MySharedPreference;
import ir.shahabazimi.atm.Database.Activities;
import ir.shahabazimi.atm.Database.History;
import ir.shahabazimi.atm.Database.MyRoomDatabase;
import ir.shahabazimi.atm.R;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context context;
    private List<History> list,filterlist;
    private String language;
    private MyRoomDatabase myRoomDatabase;

    public HistoryAdapter (Context context, List<History> list){
        this.context=context;
        this.list=list;
        filterlist=new ArrayList<>();
        filterlist.addAll(list);
        this.language=MySharedPreference.getInstance(context).getLanguage();
        myRoomDatabase= Room.databaseBuilder(context,MyRoomDatabase.class,"ATMDB").allowMainThreadQueries().build();

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_row,parent,false);

       return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        History history = filterlist.get(position);
        Uri uri = Uri.parse(context.getString(R.string.activity_pic_url,history.getActivityname().toLowerCase().replace(" ","_")));
        holder.id.setText(history.getId()+"");
        holder.time.setText(history.getTime());
        switch (language){
            case "en":
                holder.title.setText(history.getActivityname());
                break;
            case "fa":
                holder.title.setText(myRoomDatabase.myDao().getActivityFaName(history.getActivityname()));
                break;

        }

        holder.pic.setImageURI(uri);

    }

    public void search(String title){
        filterlist.clear();
        if(TextUtils.isEmpty(title))
        {
            filterlist.addAll(list);

        }else{
            for(History item: list){
                if(item.getActivityname().toLowerCase().contains(title.toLowerCase())){
                    filterlist.add(item);
                }
            }
        }
        notifyDataSetChanged();



    }

    @Override
    public int getItemCount() {
        return filterlist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private SimpleDraweeView pic;
        private TextView title,time,id;



        ViewHolder(@NonNull View v) {
            super(v);
            pic = v.findViewById(R.id.his_row_img);
            title = v.findViewById(R.id.his_row_title);
            time = v.findViewById(R.id.his_row_time);
            id = v.findViewById(R.id.his_row_id);
        }
    }
}
