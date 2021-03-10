package com.example.morsetranslator;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Settingsverification extends AppCompatActivity {


    String user = HAMorseCommon.user;
    int expCondition = HAMorseCommon.conditionArray[HAMorseCommon.conditionIndex];
    private TextToSpeech t1;
    TextView particpant;
    Button button;
    Button change_settings;
    TextView duration_tv;
    TextView interval_tv;
    int duration= settings.duration;
    int interval= settings.interval;



@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_verification);
        button=(Button)findViewById(R.id.continue_button);
        change_settings=(Button)findViewById(R.id.change_settings);
        duration_tv=(TextView)findViewById(R.id.duration);
        interval_tv=(TextView)findViewById(R.id.interval);

    button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToBundleAndOpenActivity(mainstudy.class);
            }
        });
        change_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToBundleAndOpenActivity(settings_tv.class);
            }
        });
        updateVItvs();
    }


    void addToBundleAndOpenActivity (Class cls){
        Intent intent = new Intent(Settingsverification.this, cls);
        Bundle bundle = new Bundle();
        bundle.putString("from", "Study");
        intent.putExtras(bundle);
        startActivity(intent);
        this.finish();;
    }

    void updateVItvs(){
        duration_tv.setText("Vibration Duration: "+String.valueOf(duration)+"%");
        interval_tv.setText("Vibration Interval: "+String.valueOf(interval/4)+"%");

    }

}





