package ir.shahabazimi.atm.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import ir.shahabazimi.atm.Classes.DateConverter;
import ir.shahabazimi.atm.Classes.MySharedPreference;
import ir.shahabazimi.atm.Database.Message;
import ir.shahabazimi.atm.R;



public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder> {


    private Context context;

    private List<Message> arrayList;


    public MessagesAdapter(Context context, List<Message> arrayList){
        this.arrayList=arrayList;
        this.context=context;
    }


    @Override
    public MessagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_row,parent,false);
        MessagesViewHolder messagesViewHolder = new MessagesViewHolder(view);
        return messagesViewHolder;


    }

    @Override
    public void onBindViewHolder(MessagesViewHolder holder, int position) {
        Message messagesModel = arrayList.get(position);
        holder.id.setText(messagesModel.getId()+"");
        holder.title.setText(messagesModel.getTitle());
        holder.message.setText(messagesModel.getBody());
        holder.sender.setText(messagesModel.getSender());
        DateConverter dateConverter = new DateConverter();
        switch (MySharedPreference.getInstance(context).getLanguage()){
            case "fa":
                int year = Integer.parseInt(messagesModel.getDate().substring(0,4));
                int month = Integer.parseInt(messagesModel.getDate().substring(5,7));
                int day = Integer.parseInt(messagesModel.getDate().substring(8,10));
                dateConverter.gregorianToPersian(year,month,day);
                holder.date.setText(dateConverter.getYear()+"/"+dateConverter.getMonth()+"/"+dateConverter.getDay()+" "+messagesModel.getDate().substring(11));
                break;

            case "en":
                holder.date.setText(messagesModel.getDate());
                break;

        }
    }

    @Override
    public int getItemCount() {

        return arrayList.size();
    }

    class MessagesViewHolder extends RecyclerView.ViewHolder
    {

        private TextView id,title,message,date,sender;
        private ImageView delete;

         MessagesViewHolder(final View view){
            super(view);

            id= view.findViewById(R.id.messages_items_id);
            title= view.findViewById(R.id.messages_items_title);
            message= view.findViewById(R.id.messages_items_message);
            sender= view.findViewById(R.id.messages_items_sender);
            date= view.findViewById(R.id.messages_items_date);

            delete = view.findViewById(R.id.messages_items_delete);
            delete.setVisibility(View.GONE);

        }
    }
}