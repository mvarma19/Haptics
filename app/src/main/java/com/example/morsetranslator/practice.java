package com.example.morsetranslator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

//import static com.example.morsetranslator.HABlindTutorial.interval;
//import static com.example.morsetranslator.HABlindTutorial.duration;

import static com.example.morsetranslator.HAMorseCommon.user;
import static java.lang.String.valueOf;


public class practice extends AppCompatActivity {

    Vibrator mvibrator;
    Bundle bundle;
    Button pinButton;
    TextView pwTV;
    TextView delTV;
    TextView clrTV;
    Button continue_trial;

    TextView duration_tv;
    TextView interval_tv;
    TextView testPWtv;
    //TextView trialTV;
    String fileWriteString = "";

    int duration= settings_tv.duration;
    int interval= settings_tv.interval;

    private TextToSpeech t2;

    Random r = new Random();
    int evalPW = 0;
    long startTime = 0;
    long down = 0;
    int trial = 1;
    public String pw = "";
    public int count = 0;
    AlertDialog.Builder builder;
    public boolean touchevent = false;
    String practiceString="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practice);
        mvibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        bundle = getIntent().getExtras();
        //Log.e("XXXXX","enterPWTutorial");

        pinButton = (Button) findViewById(R.id.PINbutton);
        testPWtv = (TextView) findViewById(R.id.testpw);

        pwTV = (TextView) findViewById(R.id.input);
        delTV = (TextView) findViewById(R.id.del);
        clrTV = (TextView) findViewById(R.id.clr);
        continue_trial = (Button) findViewById(R.id.continue_button);
        //trialTV.setText("Trial " + String.valueOf(trial) + "/3 ");


        duration_tv = (TextView) findViewById(R.id.duration);
        interval_tv = (TextView) findViewById(R.id.interval);
        builder = new AlertDialog.Builder(this);
        updateVItvs();






        generateEvaluationPassword();


        processTVPress(pinButton, 0);
        processTVPress(delTV, 1);
        processTVPress(clrTV, 2);


    }
    void updateVItvs(){
        duration_tv.setText("Vibration Duration: "+String.valueOf(duration)+"%");
        interval_tv.setText("Vibration Interval: "+String.valueOf(interval/4)+"%");
    }

    private void generateEvaluationPassword() {

        evalPW = r.nextInt(9999);
        testPWtv.setText("Please Enter the PIN: " + String.format("%04d", evalPW));
        startTime = Calendar.getInstance().getTimeInMillis();

    }




    public void processTVPress(final TextView t, final int t1) {
        t.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e("Tag", "I am here!!");
                        if (t1 == 0) {
                            touchevent = true;
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
                        if (t1 == 0) {
                            touchevent = false;
                        }
                        if (t1 == 1) {
                            fileWriteString="2.3,practice,keylog,"+ HAMorseCommon.dateTime()+","+String.valueOf(startTime)+","+valueOf(Calendar.getInstance().getTimeInMillis())+","+"DEL"+"\n";
                            HAMorseCommon.writeAnswerToFile(getApplicationContext(), fileWriteString);
                            if (pw != null && pw.length() > 0) {
                                pw = pw.substring(0, pw.length() - 1);
                            }
                            pwTV.setText(pw);

                        }
                        if (t1 == 2) {
                            fileWriteString="2.3,practice,keylog,"+ HAMorseCommon.dateTime()+","+String.valueOf(startTime)+","+valueOf(Calendar.getInstance().getTimeInMillis())+","+"CLR"+"\n";
                            HAMorseCommon.writeAnswerToFile(getApplicationContext(), fileWriteString);
                            pw = "";
                            pwTV.setText(pw);

                        }
//
                        break;
                }

                return true;
//
            }
        });
        continue_trial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pw.equals(String.format("%04d", evalPW))) {
                    Log.e("Eval", String.valueOf(evalPW) + " CORRECT " + pw);


                } else {
                    Log.e("Eval", String.valueOf(evalPW) + " INCORRECT " + pw);


                }
                fileWriteString = "1.3,practice,answer" + "," + HAMorseCommon.dateTime()+ "," + String.valueOf(startTime) +"," + String.valueOf(Calendar.getInstance().getTimeInMillis()) +trial +"," + String.format("%04d", evalPW) +","+ pw +"," + "\n";
                HAMorseCommon.writeAnswerToFile(getApplicationContext(), fileWriteString);

                Log.e("Answers", "Empty Text lah");
                builder.setMessage("Required PIN: " + String.format("%04d", evalPW) + "\nPIN you entered: " + pw)

                        .setPositiveButton("Okay!", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                generateEvaluationPassword();
                                pw = "";
                                pwTV.setText(pw);

                                trial++;

                                if (trial >= 4) {

                                    addToBundleAndOpenActivity(mainscreen.class);


                                }
                                updateTV();



                            }
                        });
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("PIN information");
                alert.show();
//                fileWriteString = "1.3,practice,answer" + "," + HAMorseCommon.dateTime()+ "," + String.valueOf(startTime) +"," + String.valueOf(Calendar.getInstance().getTimeInMillis()) +trial +"," + String.format("%04d", evalPW) +","+ pw +"," + "\n";
//                HAMorseCommon.writeAnswerToFile(getApplicationContext(), fileWriteString);



            }
        });




    }


    void addToBundleAndOpenActivity(Class cls) {
        Intent intent = new Intent(practice.this, cls);

        Bundle bundle = new Bundle();

        bundle.putString("userName", user);
        bundle.putInt("duration", duration);
        bundle.putInt("interval", interval);
        intent.putExtras(bundle);
        startActivity(intent);
        Log.e("SentBundle", String.valueOf(bundle));
    }



    private void updateTV() {
        //trialTV.setText("Trial " + String.valueOf(trial) + "/3 ");
        Log.e("trial:" + String.valueOf(trial), String.valueOf(trial));

    }



    class TouchVibe implements Runnable {
        @Override
        public void run() {
            down = System.currentTimeMillis();
            Log.e("ThreadDuration", Integer.toString(duration));
            Log.e("ThreadInterval", Integer.toString(interval));
            while (touchevent) {
                if (Math.abs(down - System.currentTimeMillis()) > interval) {
                    Log.d("TAG", "Thread");
                    down = System.currentTimeMillis();
                    mvibrator.vibrate(duration);
                    count++;

                    if (count == 10 || count > 10) {
                        count = 0;
                    }
                }
            }

            if (!touchevent) {
                mvibrator.cancel();
                Log.d("PW", String.valueOf(count));
                pw = pw + String.valueOf(count);
                fileWriteString="2.3,practice,keylog,"+ HAMorseCommon.dateTime()+","+String.valueOf(startTime)+","+valueOf(Calendar.getInstance().getTimeInMillis())+","+count+"\n";
                HAMorseCommon.writeAnswerToFile(getApplicationContext(), fileWriteString);
                pwTV.setText(pw);
                count = 0;
            }
        }
    }
}



