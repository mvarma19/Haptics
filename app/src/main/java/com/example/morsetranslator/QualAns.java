package com.example.morsetranslator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;

public class QualAns extends AppCompatActivity {
    Button bSubmit;

    EditText ETQ1;
    EditText ETQ2;
    EditText ETQ3;
    EditText ETQ4;
    int expCondition=15;//HAMorseCommon.conditionArray[HAMorseCommon.conditionIndex];
    String type="qualitative";
    long startTime=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qual_ans);


        ETQ1=findViewById(R.id.q1);
        ETQ2=findViewById(R.id.q2);
        ETQ3=findViewById(R.id.q3);
        ETQ4=findViewById(R.id.q4);
        setETListner(ETQ1,ETQ2);
        bSubmit = findViewById(R.id.submit);

        Log.e("InQual","Qual");
        startTime=Calendar.getInstance().getTimeInMillis();
        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileWriteString="1.6,mainStudyTLX,QualAnswers,"+HAMorseCommon.dateTime()+","+String.valueOf(startTime)+","+String.valueOf(Calendar.getInstance().getTimeInMillis())+","
                        +ETQ1.getText()+","
                        +ETQ2.getText()+","
                        +ETQ3.getText()+","
                        +ETQ3.getText()+","
                        +"\n";
                HAMorseCommon.writeAnswerToFile(getApplicationContext(),fileWriteString);
                HAMorseCommon.conditionIndex=0;
                    sendEmail();
                    //addToBundleAndOpenActivity(QualAns.class);

            }
        });



    }
    void addToBundleAndOpenActivity(Class cls){
        Intent intent = new Intent(QualAns.this, cls);
        finish();
        startActivity(intent);
    }
    void sendEmail(){
        getIntent().addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);  getIntent().addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        HAMorseCommon.sendEmail(this);
    }

    void setETListner(final EditText et, final EditText nextET){
        Log.e("KeyEvent",String.valueOf(KeyEvent.ACTION_DOWN));
        Log.e("KeyKode",String.valueOf(KeyEvent.KEYCODE_ENTER));
        et.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                Log.e("KeyEvent",String.valueOf(event.getAction()));
                Log.e("KeyKode",String.valueOf(KeyEvent.KEYCODE_ENTER));
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    // Perform action on Enter key press
                    et.clearFocus();
                    nextET.requestFocus();
                    return true;
                }
                return false;
            }
        });



    }
}