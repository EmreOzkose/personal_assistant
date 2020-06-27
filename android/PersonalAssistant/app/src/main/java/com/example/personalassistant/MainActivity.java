package com.example.personalassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private final int REQ_CODE_SPEECH_INPUT = 2;

    NetworkManager networkManager;
    SpeechManager speechManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        networkManager = new NetworkManager("192.168.2.21", 5000, this);
        speechManager = new SpeechManager(this);
        speechManager.setREQ_CODE_SPEECH_INPUT(REQ_CODE_SPEECH_INPUT);

        String call_name = get_stored_call_name();
        speechManager.setCall_name(call_name);

        final Activity this_act = this;

        ImageButton button_speech =  findViewById(R.id.speech_button);
        button_speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEV", "CLICKED");
                    speechManager.promptSpeechInput(this_act);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode != RESULT_OK || null == data) Log.d("DEV", "NULL INTENT DATA");

            switch (requestCode) {
                case REQ_CODE_SPEECH_INPUT:
                    assert data != null;
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    assert result != null;
                    String result_text = result.get(0);
                    if (result_text == null) result_text = "";

                    if (result_text.toLowerCase().contains("günü özetle") || result_text.toLowerCase().contains("günün özeti")){
                        speechManager.say_sentence("tabiki " + speechManager.getCall_name());
                        sleep_app(2);

                        networkManager.send_google_cal_request(speechManager);
                    }
                    else if (result_text.toLowerCase().contains("artık bana")){
                        speechManager.setCall_name(result_text.toLowerCase().replace("artık bana", "").replace("de", "").trim());
                        speechManager.say_sentence("Sana artık " + speechManager.getCall_name() + " diyeceğim.");
                        set_stored_call_name(speechManager.getCall_name());
                    }
                    else if  (result_text.toLowerCase().contains("teşekkürler"))
                        speechManager.say_sentence("Ne demek canım, öpüyorum.");
                    else if  (result_text.toLowerCase().contains("ben kimim")) {
                        speechManager.say_sentence("Hah ! Sen ki " + speechManager.getCall_name());
                        sleep_app(2);
                        speechManager.say_sentence("Ki ben senin asistanınım!");
                    }

                    break;
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something Went Wrong.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private String get_stored_call_name(){
        SharedPreferences sharedPreferences = getSharedPreferences("call_name", MODE_PRIVATE);

        return sharedPreferences.getString("call_name", speechManager.getCall_name());
    }

    private void set_stored_call_name(String call_name){
        SharedPreferences sharedPreferences = getSharedPreferences("call_name", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("call_name", call_name);
        editor.apply();
    }

    private void sleep_app(int seconds) throws InterruptedException {
        TimeUnit.SECONDS.sleep(seconds);
    }

}
