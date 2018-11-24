package ir.shahabazimi.atm.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import ir.shahabazimi.atm.Classes.MyUtils;
import ir.shahabazimi.atm.Fragments.LoginFragment;
import ir.shahabazimi.atm.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        MyUtils.getInstance(this).setAppLanguage();
        setContentView(R.layout.activity_login);


        getSupportFragmentManager().beginTransaction().add(R.id.login_container,new LoginFragment()).commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    }
}
