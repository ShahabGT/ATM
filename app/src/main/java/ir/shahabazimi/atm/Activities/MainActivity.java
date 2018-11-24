package ir.shahabazimi.atm.Activities;

import androidx.room.Room;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.messaging.FirebaseMessaging;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import ir.shahabazimi.atm.Classes.AsyncClass;
import ir.shahabazimi.atm.Classes.Const;
import ir.shahabazimi.atm.Classes.MySharedPreference;
import ir.shahabazimi.atm.Classes.MyToast;
import ir.shahabazimi.atm.Classes.MyUtils;
import ir.shahabazimi.atm.Classes.NetworkMonitor;
import ir.shahabazimi.atm.Classes.TimerService;
import ir.shahabazimi.atm.Database.MyRoomDatabase;
import ir.shahabazimi.atm.Dialogs.SettingDialog;
import ir.shahabazimi.atm.Fragments.ActivityFragment;
import ir.shahabazimi.atm.Fragments.HomeFragment;
import ir.shahabazimi.atm.Listeners.UpdateListener;
import ir.shahabazimi.atm.R;

public class MainActivity extends AppCompatActivity {
    private TextView toolbarText;
    private MaterialSearchView searchView;
    private Drawer result;
    private boolean doubleBackToExitPressedOnce;
    private ImageView menuOpener;
    private MyRoomDatabase myRoomDatabase;
    private NetworkMonitor networkMonitor;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        MyUtils.getInstance(this).setAppLanguage();
        setContentView(R.layout.activity_main);
        init();

    }
    private void init(){
        myRoomDatabase = Room.databaseBuilder(this,MyRoomDatabase.class,"ATMDB").allowMainThreadQueries().build();
        doubleBackToExitPressedOnce=false;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchView = findViewById(R.id.search_view);
        toolbarText = findViewById(R.id.toolbar_text);
        menuOpener = findViewById(R.id.toolbar_menu);


        networkMonitor = new NetworkMonitor();


        getSupportFragmentManager().beginTransaction().add(R.id.main_container,new HomeFragment()).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.main_container2,new ActivityFragment()).commit();

        if(MyUtils.getInstance(this).checkInternet())
        checkForUpdated();
        ndrawer();
        getHistory();

        menuOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  drawer.openDrawer(Gravity.RIGHT);
                result.openDrawer();
            }
        });




    }
    private void checkForUpdated(){
        AsyncClass asyncClass = new AsyncClass(MainActivity.this, new UpdateListener() {
            @Override
            public void updated(boolean done) {

            }
        });
        asyncClass.execute("update","activities");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }
    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else if (result.isDrawerOpen()) {
            result.closeDrawer();
        }else{
            if (doubleBackToExitPressedOnce) {
                MainActivity.this.finish();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            MyToast.Create(MainActivity.this,getString(R.string.onbackpressed));

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(networkMonitor,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkMonitor);
        super.onStop();
    }

    public void ndrawer(){

        result = new DrawerBuilder()
                .withActivity(this)
                .withHasStableIds(true)
                .withSelectedItem(-1)
                .withDrawerGravity(Gravity.LEFT)
                .withHeader(R.layout.nav_header)
                .withHeaderDivider(true)
                .withSliderBackgroundColor(Color.parseColor("#2a2a2a"))
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(getString(R.string.navigation_user)).withIcon(R.mipmap.ic_user).withSelectable(false).withIdentifier(1).withTextColor(Color.WHITE),
                        new PrimaryDrawerItem().withName(getString(R.string.navigation_stats)).withIcon(R.mipmap.ic_stats).withSelectable(false).withIdentifier(2).withTextColor(Color.WHITE),
                        new PrimaryDrawerItem().withName(getString(R.string.navigation_message)).withIcon(R.mipmap.ic_message).withSelectable(false).withIdentifier(3).withTextColor(Color.WHITE),
                        new PrimaryDrawerItem().withName(getString(R.string.navigation_settings)).withIcon(R.mipmap.ic_settings).withSelectable(false).withIdentifier(4).withTextColor(Color.WHITE),
                        new PrimaryDrawerItem().withName(getString(R.string.navigation_about)).withIcon(R.mipmap.ic_about).withSelectable(false).withIdentifier(5).withTextColor(Color.WHITE),
                        new PrimaryDrawerItem().withName(getString(R.string.navigation_logout)).withIcon(R.mipmap.ic_logout).withSelectable(false).withIdentifier(6).withTextColor(Color.WHITE)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch ((int)drawerItem.getIdentifier()){
                            case 1:
                                startActivity(new Intent(MainActivity.this,UserActivity.class));
                                break;
                            case 2:
                                startActivity(new Intent(MainActivity.this,StatisticsActivity.class));

                                break;
                            case 3:
                                startActivity(new Intent(MainActivity.this,MessagesActivity.class));

                                break;
                            case 4:
                                SettingDialog cdd = new SettingDialog(MainActivity.this);
                                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                cdd.show();

                                break;
                            case 5:
                                startActivity(new Intent(MainActivity.this,AboutActivity.class));
                                break;
                            case 6:

                                logOut();
                                break;
                        }
                        return false;
                    }
                })
                .build();
    }

    private void logOut(){
        FirebaseMessaging.getInstance().unsubscribeFromTopic(Const.FCM_TOPIC);
        stopService(new Intent(MainActivity.this,TimerService.class));
        MySharedPreference.getInstance(MainActivity.this).clear();
        myRoomDatabase.myDao().deleteActivities();
        myRoomDatabase.myDao().deleteHistory();
        myRoomDatabase.myDao().deleteTables();
        MainActivity.this.finish();

    }

    private void getHistory(){
        if(!MySharedPreference.getInstance(this).getrecivedHistory()){
            AsyncClass asyncClass = new AsyncClass(MainActivity.this, new UpdateListener() {
                @Override
                public void updated(boolean done) {
                    if(done)
                        MySharedPreference.getInstance(MainActivity.this).setrecivedHistory(true);
                }
            });
            asyncClass.execute("history");

        }

    }
}
