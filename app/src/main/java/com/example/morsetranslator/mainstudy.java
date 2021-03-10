package com.example.morsetranslator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Random;

import static com.example.morsetranslator.HAMorseCommon.trialNum;
import static com.example.morsetranslator.HAMorseCommon.user;
import static java.lang.String.valueOf;

public class mainstudy extends AppCompatActivity {

    Vibrator mvibrator;
    Bundle bundle;
    Button tv;
    TextView pwTV;
    TextView delTV;
    TextView clrTV;
    TextView conditionTV;
    Button continue_trial;
    //TextView enterTV;
    TextView diary;
    TextView duration_tv;
    TextView interval_tv;
    TextView testPWtv;
    TextView trialTV;
    TextView welcomeTV;
    String fileWriteString="";
    Button change;
    AlertDialog.Builder builder;
    int duration= settings.duration;
    int interval= settings.interval;
    long keyStartTime=0;

    private TextToSpeech t2;
    //String pwstore="";
    //TextView conditionTV;
    Random r = new Random();
    static long startSleep = 0;//for start sleep time
    int evalPW=0;
    long startTime=0;
    long endTime=0;
    long down=0;
    public String pw="";
    public int count=0;

    int expCondition=HAMorseCommon.conditionArray[HAMorseCommon.conditionIndex];
    static int conditionIndex=0;
    public boolean touchevent=false;
    int tempInterval=0;
    long startDown=0;
    boolean firstTouch=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainstudy);
        mvibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        bundle=getIntent().getExtras();
        retrieveItemsFromBundle();

        tv = (Button) findViewById(R.id.tvb);
        diary=(TextView)findViewById(R.id.welcome);

        testPWtv=(TextView) findViewById(R.id.testpw);

        pwTV=(TextView) findViewById(R.id.input);
        delTV=(TextView) findViewById(R.id.del);
        clrTV=(TextView) findViewById(R.id.clr);
        //enterTV=(TextView) findViewById(R.id.enter);
        continue_trial=(Button)findViewById(R.id.continue_button);
        trialTV=(TextView) findViewById(R.id.tvtrial);
        duration_tv=(TextView)findViewById(R.id.duration);
        interval_tv=(TextView)findViewById(R.id.interval);
        conditionTV=(TextView) findViewById(R.id.tvcondition);
        welcomeTV=(TextView) findViewById(R.id.welcome);
        updateVItvs();

        change=(Button)findViewById(R.id.change_button);
        builder = new AlertDialog.Builder(this);
        change.setEnabled(false);






        generateEvaluationPassword();
        retrieveItemsFromBundle();
        Log.e("Condition", String.valueOf(HAMorseCommon.conditionIndex));
        Log.e("Trial", String.valueOf(HAMorseCommon.trialNum));

        processTVPress(tv,0);
        processTVPress(delTV,1);
        processTVPress(clrTV,2);
        //processTVPress(enterTV,3);

    }

    //final GestureDetector gdt = new GestureDetector(new enterPWtutorial());



    private void generateEvaluationPassword(){
        firstTouch=true;
        evalPW=r.nextInt(9999);
        testPWtv.setText("Please Enter the PIN: "+String.format("%04d", evalPW));
    }

    void updateVItvs(){
        duration_tv.setText("Vibration Duration: "+String.valueOf(duration)+"%");
        interval_tv.setText("Vibration Interval: "+String.valueOf(interval/4)+"%");
        welcomeTV.setText("Experiment no " + ((HAMorseCommon.trialNum + 1) + (HAMorseCommon.conditionIndex * 3)) + " of 12");

    }



    public void processTVPress(final TextView t, final int  t1) {
        t.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        Log.e("Tag","I am here!!");
                        if (t1==0) {
                            touchevent = true;
                            down = System.currentTimeMillis();
                            startDown=System.currentTimeMillis();
                            Log.e("TOUCH-condition",String.valueOf(expCondition));
                            Log.e("TOUCH-startSleep",String.valueOf(startSleep));
                            Log.e("TOUCH-tempInterval",String.valueOf(tempInterval));
                            Log.e("TOUCH-interval",String.valueOf(interval));
                            Log.e("TOUCH-duration",String.valueOf(duration));
                            Log.e("TOUCH-firstTouch",String.valueOf(firstTouch));
                            if(firstTouch){
                                startTime = Calendar.getInstance().getTimeInMillis();
                                firstTouch=false;
                            }
                            Log.e("TOUCH-startTime",String.valueOf(startTime));
                            keyStartTime= Calendar.getInstance().getTimeInMillis();;
                            new Thread(new TouchVibe()).start();
                        }

//                        store += t.getText();
//                        input.setText(store);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //Log.d("Tag","ACTION MOVE");
                        //Log.d("Tag",String.valueOf("Move"));

                        //isMoving = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d("TAG", "ACTION_UP");
                        if (t1==0) {
                            touchevent = false;
                        }
                        if (t1==1){
                            fileWriteString="2.3,practice,keylog,"+ HAMorseCommon.dateTime()+","+String.valueOf(startTime)+","+valueOf(Calendar.getInstance().getTimeInMillis())+","+ String.valueOf(HAMorseCommon.conditionArray[HAMorseCommon.conditionIndex])+","+String.valueOf(HAMorseCommon.trialNum)+ ","+"DEL"+ ","+String.valueOf(interval)+","+String.valueOf(duration)+","+String.valueOf(startSleep)+","+String.valueOf(tempInterval)+"\n";
                            HAMorseCommon.writeAnswerToFile(getApplicationContext(), fileWriteString);
                            if (pw != null && pw.length() > 0) {
                                pw = pw.substring(0, pw.length() - 1);
                            }
                            pwTV.setText(pw);
                        }
                        if (t1==2){
                            fileWriteString="2.3,practice,keylog,"+ HAMorseCommon.dateTime()+","+String.valueOf(startTime)+","+valueOf(Calendar.getInstance().getTimeInMillis())+","+ String.valueOf(HAMorseCommon.conditionArray[HAMorseCommon.conditionIndex])+","+String.valueOf(HAMorseCommon.trialNum)+ ","+"CLR"+ ","+String.valueOf(interval)+","+String.valueOf(duration)+","+String.valueOf(startSleep)+","+String.valueOf(tempInterval)+"\n";
                            HAMorseCommon.writeAnswerToFile(getApplicationContext(), fileWriteString);
                            pw="";
                            pwTV.setText(pw);
                        }
//
                        break;
                }

                return true;

            }
        });
        if(expCondition==1) {
            startSleep=0;
            tempInterval=interval;
        }
        if(expCondition==2) {
            startSleep= 200+(new Random()).nextInt(200); //randomization
            tempInterval=interval;
            Log.d("Hi, I am condition 2,Start time is random here:",String.valueOf(startSleep));

        }
        if(expCondition==3) {
            startSleep=0;
            tempInterval=interval+ (new Random()).nextInt(200);
            Log.d("hi i am condition3,random interval is:", String.valueOf(interval));
        }

        if(expCondition==4) {
            startSleep= 200+ (new Random()).nextInt(200); //randomization
            tempInterval=interval+ (new Random()).nextInt(200);
            Log.d("Hi i am condition 4:Start time is random here:",String.valueOf(startSleep));
        }


        continue_trial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTime=Calendar.getInstance().getTimeInMillis();
                if (pw.equals(String.format("%04d", evalPW))) {
                    Log.e("Eval", String.valueOf(evalPW) + " CORRECT " + pw);
                    //return false;
                } else {
                    Log.e("Eval", String.valueOf(evalPW) + " INCORRECT " + pw);
                }
                Log.e("Answers", "Empty Text lah");
                fileWriteString="1.4,mainStudy,answer,"+ HAMorseCommon.dateTime()+","+String.valueOf(startTime)+","+String.valueOf(endTime)+","+ String.valueOf(HAMorseCommon.conditionArray[HAMorseCommon.conditionIndex])+","+String.valueOf(HAMorseCommon.trialNum)+ ","+String.format("%04d", evalPW) +","+ pw +","+ String.valueOf(interval)+","+String.valueOf(duration)+","+String.valueOf(startSleep)+","+String.valueOf(tempInterval)+ "\n";
                HAMorseCommon.writeAnswerToFile(getApplicationContext(), fileWriteString);
                builder.setMessage("Required PIN: " + String.format("%04d", evalPW) + "\nPIN you entered: " + pw)

                        .setPositiveButton("Okay!", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                generateEvaluationPassword();
                                pw = "";
                                pwTV.setText(pw);

                                //shifts=0;
                                HAMorseCommon.trialNum++;
                                //updateVItvs();
                                if (HAMorseCommon.trialNum >= 3) {

                                    HAMorseCommon.conditionIndex++;
                                    //Log.e("CONDITIONS:",String.valueOf(HAMorseCommon.conditionArray[HAMorseCommon.conditionIndex]));
                                    HAMorseCommon.conditionIndex--;
                                    HAMorseCommon.trialNum=0;
                                    addToBundleAndOpenActivity(survey_tb.class);

                                }else{
                                    addToBundleAndOpenActivity(mainstudy.class);
                                }

                                updateTV();



                            }
                        });
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("PIN information");

                alert.show();






            }
        });
    };

    void addToBundleAndOpenActivity(Class cls){
        Intent intent = new Intent(mainstudy.this, cls);

        Bundle bundle=new Bundle();

        bundle.putString("userName",user);
        bundle.putInt("duration", duration);
        bundle.putInt("interval", interval);
        intent.putExtras(bundle);
        startActivity(intent);
        this.finish();
        Log.e("SentBundle",String.valueOf(bundle));
    }

    void retrieveItemsFromBundle(){
        if (bundle!=null) {


        }

    }
    private void updateTV()  {
        //SystemClock.sleep(startSleep);
        //Log.e("system has slept for:",String.valueOf(startSleep));


        trialTV.setText("Trial "+String.valueOf(HAMorseCommon.trialNum+1)+"/3 ");
        conditionTV.setText(" "+(String.valueOf(HAMorseCommon.conditionIndex+1))
                +"/"+String.valueOf(HAMorseCommon.conditionArray.length)
                +"("+String.valueOf(HAMorseCommon.conditionArray[HAMorseCommon.conditionIndex])+")");
    }
    void sendEmail(){
        getIntent().addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);  getIntent().addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        HAMorseCommon.sendEmail(this);
    }

    class TouchVibe implements Runnable {
        @Override
        public void run() {





            while (touchevent){
                if(Math.abs(startDown-System.currentTimeMillis())>startSleep) {
                    //Log.e("duration", String.valueOf(duration));
                    //Log.e("interval", String.valueOf(interval));
                    if (Math.abs(down - System.currentTimeMillis()) > tempInterval) {
                        Log.e("values",String.valueOf(down)+","+String.valueOf(System.currentTimeMillis())+","+String.valueOf(startDown)+","+String.valueOf(tempInterval)+",");

                        //Log.d(" and interval ", String.valueOf(interval));

                        down = System.currentTimeMillis();


                        mvibrator.vibrate(duration);


                        count++;


                        if (count == 10 || count > 10) {
                            count = 0;
                        }
                    }
                }
            }


            if (!touchevent){
                mvibrator.cancel();
                Log.d("PW", String.valueOf(count));
                fileWriteString="2.4,mainStudy,keylog"+","+ HAMorseCommon.dateTime()+","+String.valueOf(startTime)+","+String.valueOf(Calendar.getInstance().getTimeInMillis())+","+String.valueOf(keyStartTime)+","+
                        String.valueOf(HAMorseCommon.conditionArray[HAMorseCommon.conditionIndex])+","+String.valueOf(HAMorseCommon.trialNum)+ ","+count+","+String.valueOf(interval)+","+String.valueOf(duration)+","+String.valueOf(startSleep)+","+String.valueOf(tempInterval)+"\n";
                HAMorseCommon.writeAnswerToFile(getApplicationContext(), fileWriteString);
                pw=pw+String.valueOf(count);
                pwTV.setText(pw);
                count=0;
            }
        }

    }

}