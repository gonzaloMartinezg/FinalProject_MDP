package com.example.finalproject_mdp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;

public class mqtt extends AppCompatActivity {

    Button send_btn;
    EditText edittext_write;
    final String serverUri = "tcp://broker.hivemq.com:1883";
    String subscriptionTopic;
    String publishTopic;
    String publishMessage;
    MqttAndroidClient mqttAndroidClient;
    String clientId = "ExampleAndroidClient";
    private HistoryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqtt);
        send_btn = findViewById(R.id.btn_send);
        edittext_write = findViewById(R.id.edittext_inbox);

        String soccer = getIntent().getStringExtra("soccer");
        String tennis = getIntent().getStringExtra("tennis");
        String soccer_tennis = getIntent().getStringExtra("soccer_tennis");
        if (soccer!=null&&soccer_tennis==null){
            publishTopic = "sports/"+soccer;
            subscriptionTopic = "sports/"+soccer;
            //Toast.makeText(this,publishTopic,Toast.LENGTH_SHORT).show();
        }
        if (tennis!=null&&soccer_tennis==null){
            publishTopic = "sports/"+tennis;
            subscriptionTopic = "sports/"+tennis;
            //Toast.makeText(this,publishTopic,Toast.LENGTH_SHORT).show();
        }
        if (soccer_tennis!=null){
            publishTopic = "sports/"+soccer_tennis;
            subscriptionTopic = "sports/"+soccer_tennis;
            //Toast.makeText(this,publishTopic,Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(this,"topic/"+soccer,Toast.LENGTH_SHORT).show();
        //Toast.makeText(this,"topic/"+tennis,Toast.LENGTH_SHORT).show();
        //Toast.makeText(this,"topic/"+soccer_tennis,Toast.LENGTH_SHORT).show();

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publishMessage();
                edittext_write.getText().clear();
            }
        });

        RecyclerView mRecyclerView = findViewById(R.id.history_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new HistoryAdapter(new ArrayList<>());
        mRecyclerView.setAdapter(mAdapter);

        clientId = clientId + System.currentTimeMillis();

        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

                if (reconnect) {
                    addToHistory("Reconnected to : " + serverURI);
                    // Because Clean Session is true, we need to re-subscribe
                    subscribeToTopic();
                } else {
                    addToHistory("Connected to: " + serverURI);
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                addToHistory("The Connection was lost.");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                addToHistory("Incoming message: " + new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        //mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setCleanSession(true);

        addToHistory("Connecting to " + serverUri + "...");
        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    subscribeToTopic();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    addToHistory("Failed to connect to: " + serverUri +
                            ". Cause: " + ((exception.getCause() == null)?
                            exception.toString() : exception.getCause()));
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
            addToHistory(e.toString());
        }

    }

    private void addToHistory(String mainText) {
        System.out.println("LOG: " + mainText);
        mAdapter.add(mainText);
        //quit logs
        //Snackbar.make(findViewById(android.R.id.content), mainText, Snackbar.LENGTH_LONG).setAction("Action", null).show();

    }
    public void subscribeToTopic() {
        try {
            mqttAndroidClient.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    addToHistory("Subscribed to: " + subscriptionTopic);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    addToHistory("Failed to subscribe");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
            addToHistory(e.toString());
        }

    }
    public void publishMessage() {
        MqttMessage message = new MqttMessage();
        //get the text from edit text
        publishMessage = edittext_write.getText().toString();
        message.setPayload(publishMessage.getBytes());
        message.setRetained(false);
        message.setQos(0);
        try {
            mqttAndroidClient.publish(publishTopic, message);
            addToHistory("Message Published: " + publishMessage);
        } catch (Exception e) {
            e.printStackTrace();
            addToHistory(e.toString());
        }
        if (!mqttAndroidClient.isConnected()) {
            addToHistory("Client not connected!");
        }
    }

}