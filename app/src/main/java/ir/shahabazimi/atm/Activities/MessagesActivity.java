package ir.shahabazimi.atm.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import ir.shahabazimi.atm.Adapters.MessagesAdapter;
import ir.shahabazimi.atm.Classes.MyToast;
import ir.shahabazimi.atm.Database.Message;
import ir.shahabazimi.atm.Database.MyRoomDatabase;
import ir.shahabazimi.atm.R;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MessagesActivity extends AppCompatActivity {
    private TextView toolbarText;
    private ImageView menuOpener;
    private MessagesAdapter adapter;
    private MyRoomDatabase myRoomDatabase;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        init();
    }

    private void init(){
        toolbarText = findViewById(R.id.toolbar_text);
        toolbarText.setText(getString(R.string.messages));

        menuOpener = findViewById(R.id.toolbar_menu);
        menuOpener.setVisibility(View.GONE);
        myRoomDatabase = Room.databaseBuilder(this,MyRoomDatabase.class,"ATMDB").allowMainThreadQueries().build();
        recyclerView = findViewById(R.id.message_recycler);
        List<Message> arrayList = myRoomDatabase.myDao().getMessages();
        if(arrayList.size()>0) {
            adapter = new MessagesAdapter(this, arrayList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(true);
        }else{
            MyToast.Create(MessagesActivity.this,getString(R.string.message_empty));
            onBackPressed();
        }

    }
}
