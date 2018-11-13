package com.example.opilane.texttospeech;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeechService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Locale;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button btnSpeak, clear;
    EditText kõneks;
    ImageButton mic;
    TextView kõneldav;
    private final int REQ_CODE_SPEECH_OUTPUT = 143;


    TextToSpeech textToSpeech;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        btnSpeak = findViewById( R.id.räägi);
        kõneks = findViewById(R.id.kõneks);
        mic = findViewById(R.id. mic);
        kõneldav = findViewById(R.id.kõneldav);
        clear = findViewById(R.id.puhasta);

        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenMic();
            }
        });

        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kõneks.setText("");
            }
        });

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if (status == TextToSpeech.SUCCESS){
                    int result = textToSpeech.setLanguage(Locale.ENGLISH);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                        Toast.makeText(MainActivity.this, "This language is not supported", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        textToSpeech.setPitch(0.6f);
                        textToSpeech.setSpeechRate(1.0f);
                        speak();
                    }
                }
            }
        });





    }

    private void OpenMic() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak noq");

        try {

            startActivityForResult(intent, REQ_CODE_SPEECH_OUTPUT);

        }
        catch (ActivityNotFoundException tim){

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_OUTPUT:{
                if (resultCode == RESULT_OK && null != data){
                    ArrayList <String> voiceInText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    kõneldav.setText(voiceInText.get(0));

                }
                break;
            }
        }
    }

    private void speak() {
        String text = kõneks.getText().toString();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        else
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech !=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();

    }
}
