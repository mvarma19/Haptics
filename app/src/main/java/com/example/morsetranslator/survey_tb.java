package com.example.morsetranslator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Calendar;

public class survey_tb extends AppCompatActivity {

    EditText ETMental;
    EditText ETPhysical;
    EditText ETTemporal;
    EditText ETPerformance;
    EditText ETEffort;
    EditText ETFrustration;

    TextView tvMental;
    TextView tvPhysical;
    TextView tvTemporal;
    TextView tvPerformance;
    TextView tvEffort;
    TextView tvFrustration;

    Button bSubmit;

    Boolean valForceChanged=false;
    int expCondition=HAMorseCommon.conditionArray[HAMorseCommon.conditionIndex];
    String type="qualitative";
    long startTime=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_tb);

        ETMental=findViewById(R.id.mental);
        ETPhysical=findViewById(R.id.physical);
        ETTemporal=findViewById(R.id.temporal);
        ETPerformance=findViewById(R.id.performance);
        ETEffort=findViewById(R.id.effort);
        ETFrustration=findViewById(R.id.frustration);
        

        tvMental=findViewById(R.id.mentaltv);
        tvPhysical=findViewById(R.id.physicaltv);
        tvTemporal=findViewById(R.id.temporaltv);
        tvPerformance=findViewById(R.id.performancetv);
        tvEffort=findViewById(R.id.efforttv);
        tvFrustration=findViewById(R.id.frustrationtv);

        addListnersToETs(ETEffort);
        addListnersToETs(ETMental);
        addListnersToETs(ETPhysical);
        addListnersToETs(ETPerformance);
        addListnersToETs(ETFrustration);
        addListnersToETs(ETTemporal);

        bSubmit = findViewById(R.id.submit);
        startTime=Calendar.getInstance().getTimeInMillis();
        /*
        processET(ETMental, tvMental, "Mental Demand: ");
        processET(ETPhysical, tvPhysical, "Physical Demand: ");
        processET(ETTemporal, tvTemporal, "Temporal Demand: ");
        processET(ETPerformance, tvPerformance, "Performance Demand: ");
        processET(ETEffort, tvEffort, "Effort: ");
        processET(ETFrustration, tvFrustration, "Frustration: ");
        processET(ETSecurity, tvSecurity, "Sense of Security: ");
         */



        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileWriteString="1.5,mainStudy,TLX,"+HAMorseCommon.dateTime()+","+String.valueOf(startTime)+","+String.valueOf(Calendar.getInstance().getTimeInMillis())+","+expCondition+","
                        +ETMental.getText()+","
                        +ETPhysical.getText()+","
                        +ETTemporal.getText()+","
                        +ETPerformance.getText()+","
                        +ETEffort.getText()+","
                        +ETFrustration.getText()+","
                        +"\n";
                HAMorseCommon.writeAnswerToFile(getApplicationContext(),fileWriteString);
                HAMorseCommon.conditionIndex++;
                if (HAMorseCommon.conditionIndex>=HAMorseCommon.conditionArray.length){
                //if (HAMorseCommon.conditionIndex>=0){
                        //sendEmail();
                    addToBundleAndOpenActivity(QualAns.class);
                }else {
                    //HAMorseCommon.conditionIndex--;
                    addToBundleAndOpenActivity(mainstudy.class);
                }
            }
        });


    }

    void addToBundleAndOpenActivity(Class cls){
        Intent intent = new Intent(survey_tb.this, cls);
        finish();
        startActivity(intent);
    }
    void sendEmail(){
        getIntent().addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);  getIntent().addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        HAMorseCommon.sendEmail(this);
    }

    void addListnersToETs(final EditText et){
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do some thing now
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Change the TextView background color
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("text from ET", "ooo");
                int val = getValFromET(et);
                val = checkVal(val);
                Log.e("setValNext","abs");
                if(valForceChanged){
                    valForceChanged=false;
                    et.setText(String.valueOf(val));
                }
            }
        });
    }
    public int getValFromET(EditText et){
        if (!et.getText().toString().isEmpty()){
            Log.e("parse",et.getText().toString());
            int etVal=Integer.parseInt( et.getText().toString() );
            Log.e("parseddd",et.getText().toString());
            return etVal;
        }else{
            return 1;
        }
    }

    int checkVal(int val){
        if (val<0){
            val=0;
            valForceChanged=true;
        }else{
            if(val>20){
                val=20;
                valForceChanged=true;
            }
        }
        return val;
    }
}