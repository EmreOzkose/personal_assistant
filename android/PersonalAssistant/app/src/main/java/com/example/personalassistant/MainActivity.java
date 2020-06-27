package com.example.personalassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    NetworkManager networkManager;
    SpeechManager speechManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        networkManager = new NetworkManager("192.168.2.21", 5000, this);
        speechManager = new SpeechManager(this);

        Button button_summarize =  findViewById(R.id.button1);
        button_summarize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEV", "CLICKED");
                speechManager.say_sentence("Hazır mısın bebek!");
                networkManager.send_google_cal_request(speechManager);
            }
        });

    }
}
