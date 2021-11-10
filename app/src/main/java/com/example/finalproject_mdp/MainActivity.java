package com.example.finalproject_mdp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;

public class MainActivity extends AppCompatActivity {

    Button bt_Clear;
    Button bt_submit;
    EditText et1_name;
    EditText et2_surname;
    CheckBox cB_soccer;
    CheckBox cB_tennis;
    ImageView IfutbolTennis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_ly);
        bt_Clear = findViewById(R.id.clean_btn);
        et1_name = findViewById(R.id.et1);
        et2_surname = findViewById(R.id.et2);
        bt_submit = findViewById(R.id.bSubmit);
        cB_soccer = findViewById(R.id.checkBox_soccer);
        cB_tennis = findViewById(R.id.checkBox_tennis);
        IfutbolTennis = findViewById(R.id.imageView);

        bt_Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et1_name.getText().clear();
                et2_surname.getText().clear();
                cB_soccer.setChecked(false);
                cB_tennis.setChecked(false);
            }
        });
        cB_soccer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cB_soccer.isChecked()) {
                    IfutbolTennis.setImageResource(R.drawable.futbol);
                    if (cB_tennis.isChecked()) {
                        IfutbolTennis.setImageResource(R.drawable.sportscenter);
                    }
                } else {
                    if (cB_tennis.isChecked()) {
                        IfutbolTennis.setImageResource(R.drawable.tenniscourt);
                    } else {
                        IfutbolTennis.setImageResource(R.drawable.sportscenter);
                    }
                }
            }

        });
        cB_tennis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cB_tennis.isChecked()){
                IfutbolTennis.setImageResource(R.drawable.tenniscourt);
                    if(cB_soccer.isChecked()){
                        IfutbolTennis.setImageResource(R.drawable.sportscenter);
                    }
                }
                else {
                    if (cB_soccer.isChecked()){
                        IfutbolTennis.setImageResource(R.drawable.futbol);}
                    else {
                        IfutbolTennis.setImageResource(R.drawable.sportscenter);
                    }
                }
            }
        });

    }
    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("football",cB_soccer.isChecked());
        editor.putBoolean("tennis", cB_tennis.isChecked());
        editor.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        cB_soccer.setChecked(sharedPref.getBoolean("football",false));
        cB_tennis.setChecked(sharedPref.getBoolean("tennis",false));

        if (cB_tennis.isChecked()){
            IfutbolTennis.setImageResource(R.drawable.tenniscourt);
            if(cB_soccer.isChecked()){
                IfutbolTennis.setImageResource(R.drawable.sportscenter);
            }
        }
        else {
            if (cB_soccer.isChecked()){
                IfutbolTennis.setImageResource(R.drawable.futbol);}
            else {
                IfutbolTennis.setImageResource(R.drawable.sportscenter);
            }
        }

    }
    public void activity_recycler (View view){
        Intent i_act_recycler = new Intent(this,Recycler.class);
        i_act_recycler.putExtra("name",et1_name.getText().toString());
        
        if (cB_soccer.isChecked()&&cB_tennis.isChecked()){
            i_act_recycler.putExtra("soccer_tennis",cB_soccer.getText().toString()+cB_tennis.getText().toString());
        }
        if (cB_soccer.isChecked()){
            i_act_recycler.putExtra("soccer",cB_soccer.getText().toString());

        }
        if (cB_tennis.isChecked()){
            i_act_recycler.putExtra("tennis",cB_tennis.getText().toString());
        }
        if((!cB_soccer.isChecked()&&!cB_tennis.isChecked()) ||
                TextUtils.isEmpty(et1_name.getText().toString()) ||
                TextUtils.isEmpty(et2_surname.getText().toString())){
            Toast.makeText(MainActivity.this, "Please complete all the information",
                    Toast.LENGTH_SHORT).show();}
            else {
            startActivity(i_act_recycler);
        }
    }


}