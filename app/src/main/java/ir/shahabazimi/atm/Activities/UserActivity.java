package ir.shahabazimi.atm.Activities;

import androidx.appcompat.app.AppCompatActivity;
import ir.shahabazimi.atm.Classes.MySharedPreference;
import ir.shahabazimi.atm.Classes.MyToast;
import ir.shahabazimi.atm.Classes.MyUtils;
import ir.shahabazimi.atm.Classes.VolleyQue;
import ir.shahabazimi.atm.R;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserActivity extends AppCompatActivity {

    private TextView name,email,username;
    private EditText pass,repass,oldpass;
    private TextInputLayout oldpassLayout,passLayout,repassLayout;
    private Button apply;
    private ProgressDialog progressDialog;
    private ImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        init();
    }

    private void init(){
        progressDialog= new ProgressDialog(UserActivity.this);
        progressDialog.setMessage(getString(R.string.progressdialogtext));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        name = findViewById(R.id.user_name);
        email = findViewById(R.id.user_email);
        username = findViewById(R.id.user_username);
        oldpass = findViewById(R.id.user_oldpass);
        pass = findViewById(R.id.user_pass);
        repass = findViewById(R.id.user_repass);
        avatar = findViewById(R.id.user_avatar);
        passLayout = findViewById(R.id.user_pass_input);
        oldpassLayout = findViewById(R.id.user_oldpass_input);
        repassLayout = findViewById(R.id.user_repass_input);

        apply = findViewById(R.id.user_apply);


        name.setText(MySharedPreference.getInstance(UserActivity.this).getName());
        email.setText(MySharedPreference.getInstance(UserActivity.this).getEmail());
        username.setText(MySharedPreference.getInstance(UserActivity.this).getUsername());
        if(MySharedPreference.getInstance(UserActivity.this).getSex().toLowerCase().equals("female"))
            avatar.setImageDrawable(getResources().getDrawable(R.drawable.female));

        onClicks();
    }

    private void onClicks(){
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Update(oldpass.getText().toString(),pass.getText().toString(),repass.getText().toString());



            }
        });


    }
    private void Update(String pass,String newPass,String newRepass){
        MyUtils.getInstance(UserActivity.this).hideKeyboard();
        String oldPass = MySharedPreference.getInstance(UserActivity.this).getPassword();
        String user = MySharedPreference.getInstance(UserActivity.this).getUsername();
        if(oldPass.equals(pass)){
            oldpassLayout.setErrorEnabled(false);
            if(newPass.isEmpty()||newRepass.isEmpty()){
                passLayout.setError(getString(R.string.chngpass_fillform));
            }else if(newPass.length()<8){
                passLayout.setError(getString(R.string.chngpass_passlenght));
            }else if (!newPass.equals(newRepass)){
                passLayout.setError(getString(R.string.chngpass_passnotmach));
            }else if (oldPass.equals(newPass)) {
                passLayout.setError(getString(R.string.chngpass_passduplicate));
            }else{
                 passLayout.setErrorEnabled(false);
                 updateDB(user,newPass,oldPass);
            }


        }else{
            oldpassLayout.setError(getString(R.string.chngpass_wrongpass));
        }
    }
    private void updateDB(final String user, final String password, final String oldpassword){

        if(MyUtils.getInstance(UserActivity.this).checkInternet()){

            progressDialog.show();
            final String url=getString(R.string.base_url)+"update.php";
            StringRequest updateRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String res=jsonObject.getString("message");
                        if(res.equals("successful")) {
                            MyToast.Create(UserActivity.this, getString(R.string.chngpass_success));
                            MySharedPreference.getInstance(UserActivity.this).setPassword(password);
                            UserActivity.this.finish();
                        }
                            else
                            MyToast.Create(UserActivity.this,getString(R.string.error));

                    }catch (Exception e){
                        MyToast.Create(UserActivity.this,getString(R.string.error));
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    MyToast.Create(UserActivity.this,getString(R.string.error));


                }
            }){

                @Override
                protected Map<String, String> getParams() {
                    Map<String,String> params = new HashMap<>();
                    params.put("username",user);
                    params.put("password",password);
                    params.put("oldpassword",oldpassword);
                    return params;
                }
            };
            VolleyQue.getmInstance(UserActivity.this).addToRequestque(updateRequest);

        }else{
            MyToast.Create(UserActivity.this,getString(R.string.internet_error));

        }
    }
}
