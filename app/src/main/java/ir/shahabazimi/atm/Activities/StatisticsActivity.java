package ir.shahabazimi.atm.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import ir.shahabazimi.atm.Adapters.StatisticsViewPagerAdapter;
import ir.shahabazimi.atm.Fragments.AllTimeFragment;
import ir.shahabazimi.atm.Fragments.LastMonthFragment;
import ir.shahabazimi.atm.Fragments.LastWeekFragment;
import ir.shahabazimi.atm.Fragments.LastYearFragment;
import ir.shahabazimi.atm.Fragments.TodayFragment;
import ir.shahabazimi.atm.R;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

public class StatisticsActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private StatisticsViewPagerAdapter viewPagerAdapter;
    private TextView toolbarText;
    private MaterialSearchView searchView;
    private ImageView menuOpener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        init();
        viewPagerAdapter = new StatisticsViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new TodayFragment(),getString(R.string.stats_fragment_today));
        viewPagerAdapter.addFragment(new LastWeekFragment(),getString(R.string.stats_fragment_lastweek));
        viewPagerAdapter.addFragment(new LastMonthFragment(),getString(R.string.stats_fragment_lastmonth));
        viewPagerAdapter.addFragment(new LastYearFragment(),getString(R.string.stats_fragment_lastyear));
        viewPagerAdapter.addFragment(new AllTimeFragment(),getString(R.string.stats_fragment_alltime));

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void init(){
        searchView = findViewById(R.id.stat_search_view);
        toolbarText = findViewById(R.id.toolbar_text);
        toolbarText.setText(getString(R.string.statistics));

        menuOpener = findViewById(R.id.toolbar_menu);
        menuOpener.setVisibility(View.GONE);


        tabLayout =  findViewById(R.id.stats_tablayout);
        viewPager =  findViewById(R.id.stats_viewpager);



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
        }else
        super.onBackPressed();
    }
}
