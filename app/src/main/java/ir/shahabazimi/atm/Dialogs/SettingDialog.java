package ir.shahabazimi.atm.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jaredrummler.materialspinner.MaterialSpinner;

import ir.shahabazimi.atm.Classes.MySharedPreference;
import ir.shahabazimi.atm.R;

import androidx.annotation.NonNull;

public class SettingDialog extends Dialog {

    private Context context;
    private Button ok,cancel;
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;


    public SettingDialog(@NonNull Context context) {
        super(context);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_setting);
        sp = context.getSharedPreferences("ATMPreference",0);
        spEditor =sp.edit();
        setLanguage();
        setButtons();
    }

    private void setLanguage(){
        MaterialSpinner spinner =  findViewById(R.id.setting_lngspinner);
        spinner.setItems("English","فارسی");
        switch (MySharedPreference.getInstance(context).getLanguage()){
            case "en": spinner.setSelectedIndex(0);
                break;
            case "fa":spinner.setSelectedIndex(1);
                break;
        }
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                switch (position){
                    case 0: spEditor.putString("language","en");
                        break;
                    case 1:spEditor.putString("language","fa");
                        break;
                }

            }
        });


    }
    private void setButtons(){
        cancel = findViewById(R.id.settings_cancel);
        ok = findViewById(R.id.settings_ok);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               spEditor.commit();
                System.exit(0);
            }
        });


    }
}
