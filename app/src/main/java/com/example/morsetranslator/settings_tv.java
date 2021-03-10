package com.example.morsetranslator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import static androidx.core.content.FileProvider.getUriForFile;
import static com.example.morsetranslator.HAMorseCommon.user;
import static java.lang.String.valueOf;

public class settings_tv extends AppCompatActivity {

    Vibrator mvibrator;
    Button pinButton;
    private CoordinatorLayout coordinatorLayout;
    TextView pwTV;
    TextView input;
    TextView tv_vinterval;
    TextView hiddentextview;
    TextView clrTV;
    //TextView enterTV;
    String fileWriteString = "";
    EditText durationET;
    EditText intervalET;
    Button begintutorial;
    private SharedPreferences mPreferences;
    Button continue_tutorial;
    long down = 0;
    public String pw = "";
    //public int progresValue = 0;
    public int count = 0;
    static int timeunit=100;
    public boolean touchevent = false;
    private TextToSpeech t2;
    static int duration;
    String username;
    Bundle bundle;
    private int save;
    static int interval;
    TextView tv_vduration;
    GestureDetector gestureDetector;
    InputMethodManager imm;
    String fromIntent="";
    boolean valForceChanged=false;
    long activityStartTime=0;
    String practiceString="";
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_tv);
        mvibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mPreferences=getSharedPreferences(" ",MODE_PRIVATE);
        pwTV = (TextView) findViewById(R.id.input);
        hiddentextview = (TextView) findViewById(R.id.del);
        clrTV = (TextView) findViewById(R.id.clr);
        continue_tutorial = (Button) findViewById(R.id.continue_button);
        durationET=(EditText)findViewById(R.id.etDuration);
        intervalET=(EditText)findViewById(R.id.etInterval);
        imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        final SharedPreferences.Editor preferencesEditor=mPreferences.edit();
        tv_vinterval = (TextView) findViewById(R.id.vibration_interval);
        tv_vduration=(TextView)findViewById(R.id.vibrationintervaltv);
        activityStartTime=(Calendar.getInstance().getTimeInMillis());



        initializeVariables();
        retrieveItemsFromBundle();

        continue_tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromIntent.equals("Main")){

                    fileWriteString = "1.0,Settings,fromMain"+","+ HAMorseCommon.dateTime()+","+String.valueOf(activityStartTime)+","+valueOf(Calendar.getInstance().getTimeInMillis())+","+valueOf(duration)+"," + valueOf(interval) + ","  + "\n";
                    HAMorseCommon.writeAnswerToFile(getApplicationContext(), fileWriteString);
                    addToBundleAndOpenActivity(mainscreen.class);
                }else{
                    if (fromIntent.equals("Study")){
                        fileWriteString = "1.1,Settings,fromStudy"+","+ HAMorseCommon.dateTime()+","+String.valueOf(activityStartTime)+","+valueOf(Calendar.getInstance().getTimeInMillis())+","+valueOf(duration)+"," + valueOf(interval) + ","  + "\n";
                        HAMorseCommon.writeAnswerToFile(getApplicationContext(), fileWriteString);
                        //addToBundleAndOpenActivity(mainscreen.class);
                        addToBundleAndOpenActivity(Settingsverification.class);
                    }
                }

                //startActivityForResult(intent,1);
            }
        });
        pinButton = (Button) findViewById(R.id.PINbutton);
        pwTV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });


        processTVPress(pinButton, 0);
        processTVPress(hiddentextview, 1);
        processTVPress(clrTV, 2);

        durationET.addTextChangedListener(new TextWatcher() {
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
                int val =getValFromET(durationET);
                Log.e("text from ET", "afterGetVal"+valForceChanged);
                val=checkVal(val, duration);
                Log.e("text from ET", "afterValCheck"+valForceChanged);
                Log.e("text from ET", "afterSetET");
                if(valForceChanged){
                    valForceChanged=false;
                    durationET.setText(String.valueOf(val));
                }
                duration= val;
                tv_vduration.setText("Vibration Duration:"+val+"%");
                settings.duration=duration;
                preferencesEditor.putInt("duration",duration).apply();
                Log.e("etDuration", Integer.toString(duration));
            }

        });

        durationET.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    durationET.setText(String.valueOf(duration));
                    return true;
                }
                return false;
            }
        });

        intervalET.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    intervalET.setText(String.valueOf(interval*100/400));
                    return true;
                }
                return false;
            }
        });
        intervalET.addTextChangedListener(new TextWatcher() {
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
                int val =getValFromET(intervalET);
                val=checkVal(val, interval*100/400);
                //intervalET.setText(val);
                if(valForceChanged){
                    valForceChanged=false;
                    intervalET.setText(String.valueOf(val));
                }
                interval= val*400/100;
                tv_vinterval.setText("Vibration Interval:"+val+"%");
                settings.interval=interval;
                preferencesEditor.putInt("interval",interval).apply();
                Log.e("etInterval", Integer.toString(interval));
            }
        });


    }



    void addToBundleAndOpenActivity(Class cls) {
        Intent intent = new Intent(settings_tv.this, cls);
        Log.e("Duration", valueOf(duration));
        Bundle bundle = new Bundle();
        bundle.putString("userName", user);
        bundle.putInt("duration",duration);
        bundle.putInt("interval",interval);
        intent.putExtras(bundle);
        startActivity(intent);
        Log.e("SentBundle", valueOf(bundle));
    }

    void retrieveItemsFromBundle(){
        Log.e("retrieving",fromIntent);
        Intent in = getIntent();
        Bundle b = in.getExtras();
        if (b!=null) {
            fromIntent=b.getString("from");
            Log.e("FromWhere",fromIntent);
        }else{
            Log.e("retrieving","Null");
        }
    }

    public int getValFromET(EditText et){
        if (!et.getText().toString().isEmpty()){
            Log.e("parse",et.getText().toString());
            int etVal=Integer.parseInt(et.getText().toString());
            Log.e("parsedd",String.valueOf(etVal));

            return etVal;


        }else{
            return 1;
        }
    }

    int checkVal(int val, int def){
        if (val<0){
            val=0;
            valForceChanged=true;
        }else{
            if(val>100){
                val=100;
                valForceChanged=true;
            }
        }
        return val;
    }

    private void initializeVariables() {
        duration=mPreferences.getInt("duration",50);
        interval=mPreferences.getInt("interval",50);
        durationET.setText(String.valueOf(duration),TextView.BufferType.EDITABLE);
        intervalET.setText(String.valueOf(interval/4),TextView.BufferType.EDITABLE);
        tv_vduration.setText("Vibration Duration:"+duration+"%");
        tv_vinterval.setText("Vibration Interval:"+(interval/4)+"%");
        settings.duration=duration;
        settings.interval=interval;
    }




    public void processTVPress(final TextView t, final int t1) {

        t.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e("Tag", "I am here!!");
                        imm.hideSoftInputFromWindow(durationET.getWindowToken(), 0);
                        imm.hideSoftInputFromWindow(intervalET.getWindowToken(), 0);
//
                        if (t1 == 0) {
                            touchevent = true;
                            new Thread(new settings_tv.TouchVibe()).start();
                        }


                        break;
                    case MotionEvent.ACTION_MOVE:

                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d("TAG", "ACTION_UP");
                        if (t1 == 0) {
                            touchevent = false;
                        }
                        if (t1 == 1) {
                            fileWriteString="settings,keylog,"+ HAMorseCommon.dateTime()+","+String.valueOf(activityStartTime)+","+valueOf(Calendar.getInstance().getTimeInMillis())+","+"Del"+"\n";
                            HAMorseCommon.writeAnswerToFile(getApplicationContext(), fileWriteString);
                            if (pw != null && pw.length() > 0) {
                                pw = pw.substring(0, pw.length() - 1);
                            }
                            pwTV.setText(pw);

                        }
                        if (t1 == 2) {
                            fileWriteString="2.0,settings,keylog,"+ HAMorseCommon.dateTime()+","+String.valueOf(activityStartTime)+","+valueOf(Calendar.getInstance().getTimeInMillis())+","+"CLR"+"\n";
                            HAMorseCommon.writeAnswerToFile(getApplicationContext(), fileWriteString);
                            pw = "";
                            pwTV.setText(pw);


                        }

                        break;
                }

                return true;

            }
        });
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
                    Vibrator mvibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    mvibrator.vibrate(duration);


                    count++;
                    if (count == 10 || count > 10) {
                        count = 0;
                    }
                }
            }



            if (!touchevent) {

                mvibrator.cancel();
                Log.d("PW", valueOf(count));
                pw = pw + count;
                fileWriteString="2.0,settings,keylog,"+ HAMorseCommon.dateTime()+","+String.valueOf(activityStartTime)+","+valueOf(Calendar.getInstance().getTimeInMillis())+","+count+"\n";
                HAMorseCommon.writeAnswerToFile(getApplicationContext(), fileWriteString);
                pwTV.setText(pw);
                count = 0;
            }



        }
    }




}