package ir.shahabazimi.atm.Adapters;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import ir.shahabazimi.atm.Classes.MySharedPreference;
import ir.shahabazimi.atm.Database.Activities;
import ir.shahabazimi.atm.Fragments.ActivityFragment;
import ir.shahabazimi.atm.R;

public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.ViewHolder> {

    private AppCompatActivity context;
    private List<Activities> list,filterlist;
    private String language;


    public ActivitiesAdapter(AppCompatActivity context,List<Activities> activitiesArrayList){

        this.context=context;
        this.list=activitiesArrayList;
        filterlist=new ArrayList<>();
        filterlist.addAll(list);
        this.language=MySharedPreference.getInstance(context).getLanguage();


    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_row,viewGroup,false);


        return new ViewHolder(view);
    }

    public void search(String title){
        filterlist.clear();
        if(TextUtils.isEmpty(title))
        {
            filterlist.addAll(list);

        }else{
            for(Activities item: list){
                if(item.getName().toLowerCase().contains(title.toLowerCase()) ||item.getNameFa().contains(title) ){
                    filterlist.add(item);
                }
            }
        }
        notifyDataSetChanged();



    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        Activities model = filterlist.get(i);

        Uri uri = Uri.parse(context.getString(R.string.activity_pic_url,model.getPic()));

        holder.id.setText(model.getId()+"");
        switch (language){
            case "en":
                holder.name.setText(model.getName());
                break;
            case "fa":
                holder.name.setText(model.getNameFa());
                break;

        }
        holder.nameEn.setText(model.getName());
        holder.pic.setImageURI(uri);

    }

    @Override
    public int getItemCount() {
        return filterlist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView id,name,nameEn;
        private SimpleDraweeView pic;


        ViewHolder(@NonNull View v) {
            super(v);
            init(v);
            v.setOnClickListener(this);

        }



        private void init(View v){
            id = v.findViewById(R.id.act_row_id);
            name = v.findViewById(R.id.act_row_title);
            nameEn = v.findViewById(R.id.act_name_en);
            pic = v.findViewById(R.id.act_row_img);
        }

        @Override
        public void onClick(View v) {
            context.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter,R.anim.exit).replace(R.id.main_container2,new ActivityFragment()).commit();
            MySharedPreference.getInstance(context).setActiveActivityName(name.getText().toString());
            MySharedPreference.getInstance(context).setActiveActivityNameEn(nameEn.getText().toString());
            MySharedPreference.getInstance(context).setActiveActivityPic(context.getString(R.string.activity_pic_url,nameEn.getText().toString().replace(" ","_").toLowerCase()));


        }
    }
}
