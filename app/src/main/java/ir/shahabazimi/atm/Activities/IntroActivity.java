package ir.shahabazimi.atm.Activities;


import android.content.Intent;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import ir.shahabazimi.atm.Adapters.ViewPagerAdapter;
import ir.shahabazimi.atm.Classes.MySharedPreference;
import ir.shahabazimi.atm.Classes.MyUtils;
import ir.shahabazimi.atm.R;

public class IntroActivity extends AppCompatActivity {

    ViewPager viewPager;
    ViewPagerAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        MyUtils.getInstance(this).setAppLanguage();
        setContentView(R.layout.activity_intro);
        if(!MySharedPreference.getInstance(this).getIsFirst())
        {
            if(MySharedPreference.getInstance(this).getUsername().isEmpty()) {
                startActivity(new Intent(this, LoginActivity.class));
                IntroActivity.this.finish();
            }else {
                startActivity(new Intent(this, MainActivity.class));
                IntroActivity.this.finish();
            }
        }
        viewPager = findViewById(R.id.viewPager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

}
