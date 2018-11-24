package ir.shahabazimi.atm.Activities;

import androidx.appcompat.app.AppCompatActivity;
import ir.shahabazimi.atm.R;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    private TextView version;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        version =findViewById(R.id.about_version);
        String ver="";
        try{
            ver = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        }catch (PackageManager.NameNotFoundException e){
            ver=0+"";
        }finally {
            version.setText(getString(R.string.about_version,ver));
        }
    }
}
