package com.example.finalproject_mdp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

public class Graph_sensor extends AppCompatActivity implements SensorEventListener {

    Button step_btn;
    TextView tv_measures_steps;
    private SensorManager sensorManager;
    private Sensor StepSensor;
    boolean stepSensorIsActive;
    int count1=0;
    int vibra_count;

    public ArrayList<Float> Sensor_values = new ArrayList<Float>();
    LineChart chart;
    //BarChart chart;
    LineData lineData;
    LineDataSet dataSetLight;
    List<Entry> entriesLight = new ArrayList<Entry>(); // array for light values

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_sensor);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_ly);
        chart = findViewById(R.id.chart1);

        //Set sensor
        stepSensorIsActive = false;
        step_btn = findViewById(R.id.btn_step);
        tv_measures_steps = findViewById(R.id.tv_measures_stepdetector);

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        StepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        //Array for dataset
        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSetLight = new LineDataSet(entriesLight,"Steps");
        dataSetLight.setColor(Color.BLACK); // lines color
        dataSetLight.setCircleColor(Color.RED); // circle color
        lineData = new LineData(dataSets); // create line data

        // add datasets to the list of datasets:
        dataSets.add(dataSetLight);

        // set data to chart:
        chart.setData(lineData);
        // configure chart:
        chart.getDescription().setEnabled(false);
        chart.invalidate(); // refresh

        //customize line chart
        chart.setNoDataText("No data for the moment");

        step_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stepSensorIsActive) {
                    // unregister listener and make the appropriate changes in the UI:
                    sensorManager.unregisterListener(Graph_sensor.this, StepSensor);
                    step_btn.setText(R.string.step_off_btn);
                    step_btn.setBackground(getResources().getDrawable(R.drawable.round_button_off));
                    tv_measures_steps.setText("Light sensor is OFF");
                    stepSensorIsActive = false;
                } else {
                    // register listener and make the appropriate changes in the UI:
                    sensorManager.registerListener(Graph_sensor.this, StepSensor, SensorManager.SENSOR_DELAY_NORMAL);
                    step_btn.setText(R.string.step_on_btn);
                    step_btn.setBackground(getResources().getDrawable(R.drawable.round_button_on));
                    tv_measures_steps.setText("Waiting for step sensor");
                    stepSensorIsActive = true;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            do {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        entriesLight.add(new Entry(entriesLight.size(),count1));
                                        dataSetLight.notifyDataSetChanged();
                                        lineData.notifyDataChanged();
                                        chart.notifyDataSetChanged();
                                        chart.invalidate(); // refresh
                                    }
                                });
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }
                            while (stepSensorIsActive == true);
                        }
                    }).start();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // Code to make vibrate the device
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_DETECTOR){
            count1 = (int) (count1 + sensorEvent.values[0]);
            tv_measures_steps.setText(String.valueOf(count1));
            vibra_count = count1%20;

            //graph each light value
            entriesLight.add(new Entry(entriesLight.size(),count1));// we put size as x Axis, each light value, x Axis increase by 1 value
            dataSetLight.notifyDataSetChanged();
            lineData.notifyDataChanged();
            chart.notifyDataSetChanged();
            chart.invalidate(); // refresh

            // Code to vibrate the device
            if (vibra_count == 0){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //deprecated in API 26
                    v.vibrate(500);
                }
            }

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}