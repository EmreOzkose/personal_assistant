package com.example.personalassistant;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SpeechManager {


    private int REQ_CODE_SPEECH_INPUT;
    private TextToSpeech textToSpeech;

    private String call_name = "Emre";

    SpeechManager(final Activity activity){
        this.textToSpeech = new TextToSpeech(activity.getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = textToSpeech.setLanguage(Locale.forLanguageTag("tr-TR"));

                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language is not supported!");
                    } else {
                        Log.i("TTS", "Language Supported.");
                    }
                    Log.i("TTS", "Initialization success.");
                } else {
                    Toast.makeText(activity.getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void say_sentence(String sentence){
        int speechStatus = this.textToSpeech.speak(sentence, TextToSpeech.QUEUE_FLUSH, null);

        if (speechStatus == TextToSpeech.ERROR) {
            Log.e("TTS", "Error in converting Text to Speech!");
        }
    }

    public String translate_from_en_to_tr(String en_text){

        Map<String, String> vocab = new HashMap<>();
        vocab.put("hours", "saat");
        vocab.put("ago", "önce");
        vocab.put("in", "");
        String tr_text = en_text;

        for (Map.Entry<String, String> entry : vocab.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            tr_text = tr_text.replace(key, value);
        }

        if (en_text.contains("in")) tr_text += " sonra";

        return tr_text;
    }

    void promptSpeechInput(Activity activity) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.forLanguageTag("tr-TR"));
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                " ");

        try {
            activity.startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(activity.getApplicationContext(),
                    "Speech is not supported",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public int getREQ_CODE_SPEECH_INPUT() {
        return REQ_CODE_SPEECH_INPUT;
    }

    void setREQ_CODE_SPEECH_INPUT(int REQ_CODE_SPEECH_INPUT) {
        this.REQ_CODE_SPEECH_INPUT = REQ_CODE_SPEECH_INPUT;
    }

    public String getCall_name() {
        return call_name;
    }

    public void setCall_name(String call_name) {
        this.call_name = call_name;
    }

}
