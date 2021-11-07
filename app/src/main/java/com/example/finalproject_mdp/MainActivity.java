package com.example.finalproject_mdp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;

public class MainActivity extends AppCompatActivity {

    Button bt_Clear;
    Button bt_submit;
    EditText et1_name;
    EditText et2_surname;
    CheckBox cB_soccer;
    CheckBox cB_tennis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_Clear = findViewById(R.id.clean_btn);
        et1_name = findViewById(R.id.et1);
        et2_surname = findViewById(R.id.et2);
        bt_submit = findViewById(R.id.bSubmit);
        cB_soccer = findViewById(R.id.checkBox_soccer);
        cB_tennis = findViewById(R.id.checkBox_tennis);

        bt_Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et1_name.getText().clear();
                et2_surname.getText().clear();
            }
        });

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
        if(!cB_soccer.isChecked()&&!cB_tennis.isChecked()){
            Toast.makeText(MainActivity.this, "Please select at least 1 item", Toast.LENGTH_SHORT).show();
        }else {
            startActivity(i_act_recycler);
        }
    }


}