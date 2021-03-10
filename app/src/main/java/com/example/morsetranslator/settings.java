 package com.example.morsetranslator;

 import android.annotation.SuppressLint;
 import android.content.Context;
 import android.content.Intent;
 import android.content.SharedPreferences;
 import android.media.MediaScannerConnection;
 import android.net.Uri;
 import android.os.Bundle;
 import android.os.Vibrator;
 import android.speech.tts.TextToSpeech;
 import android.util.Log;
 import android.view.GestureDetector;
 import android.view.MotionEvent;
 import android.view.View;
 import android.widget.Button;
 import android.widget.SeekBar;
 import android.widget.TextView;

 import androidx.appcompat.app.AppCompatActivity;
 import androidx.coordinatorlayout.widget.CoordinatorLayout;

 import java.io.BufferedWriter;
 import java.io.File;
 import java.io.FileWriter;
 import java.io.IOException;
 import java.util.Calendar;

 import static androidx.core.content.FileProvider.getUriForFile;
 import static com.example.morsetranslator.HAMorseCommon.user;
 import static java.lang.String.valueOf;

//import static com.example.morsetranslator.HAMorseCommon.timeunit;

public class settings extends AppCompatActivity {

    Vibrator mvibrator;
    private static SeekBar seekBar_vinterval;
    private  static SeekBar seekBar_vduration;
    Button tv;
    private CoordinatorLayout coordinatorLayout;
    TextView pwTV;
    TextView input;
    TextView tv_vinterval;
    TextView hiddentextview;
    TextView clrTV;
    //TextView enterTV;
    String fileWriteString = "";


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
    private static final String Key_PROGRESS_1 = "bar1_progress";
    private int save;
    private String Key_PROGRESS_2 = "bar2_progress";
    static int interval;
    TextView tv_vduration;
    GestureDetector gestureDetector;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        mvibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mPreferences=getSharedPreferences(" ",MODE_PRIVATE);



    initializeVariables();




        //mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor=mPreferences.edit();
        initializeVariables();
        retrieveItemsFromBundle();

        continue_tutorial = (Button) findViewById(R.id.continue_button);
        continue_tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToBundleAndOpenActivity(mainscreen.class);

                //startActivityForResult(intent,1);
            }
        });





        seekBar_vinterval.setProgress(mPreferences.getInt(Key_PROGRESS_2,0));
        seekBar_vinterval.getProgress();

        Log.e("Interval",String.valueOf(interval));


        tv_vinterval.setText("Vibration Interval:" + seekBar_vinterval.getProgress() + "/" + seekBar_vinterval.getMax());

        seekBar_vduration.setProgress(mPreferences.getInt(Key_PROGRESS_1,0));
        seekBar_vduration.getProgress();

        Log.e("Duration",String.valueOf(duration));


        tv_vduration.setText("Vibration Duration:" + seekBar_vduration.getProgress() + "/" + seekBar_vduration.getMax());



        seekBar_vduration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            final int seek = 10;
            final int yourStep = 10;

            //int progressValue=0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = ((int) Math.round(progress / yourStep)) * yourStep;
                seekBar.setProgress(progress);
                tv_vduration.setText("Vibration Duration:" + seekBar.getProgress() + "/" + seekBar.getMax());
                seekBar.announceForAccessibility("Hello");

                duration=seekBar_vduration.getProgress();
                tv_vduration.setText("Vibration Duration:" + seekBar_vduration.getProgress() + "/" + seekBar_vduration.getMax());


            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                tv_vduration.setText("Vibration Duration:" + seekBar.getProgress() + "/" + seekBar.getMax());





            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mvibrator.vibrate(100);
                tv_vduration.setText("Vibration Duration:" + seekBar.getProgress() + "/" + seekBar.getMax());
                editor.putInt(Key_PROGRESS_1,seekBar_vduration.getProgress());
                editor.commit();
                tv_vduration.setText("Vibration Duration:" + seekBar_vduration.getProgress() + "/" + seekBar_vduration.getMax());



            }
        });

        seekBar_vinterval.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            final int seek = 0;
            final int yourStep = 100;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progresValue = ((int) Math.round(progresValue / 1)) * 1;
                seekBar.setProgress(progresValue);
                tv_vinterval.setText("Vibration Interval:" + seekBar.getProgress() + "/" + seekBar.getMax());


                //interval = seekBar.getProgress();
                interval=seekBar_vinterval.getProgress();




            }




            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(getApplicationContext(), "Tutorial Speed:", Toast.LENGTH_SHORT).show();
                tv_vinterval.setText("Vibration Interval:" + seekBar.getProgress() + "/" + seekBar.getMax());


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mvibrator.vibrate(100);
                editor.putInt(Key_PROGRESS_2,seekBar_vinterval.getProgress());
                editor.commit();
                //tv_vduration.setText("Vibration Interval:" + seekBar_vduration.getProgress() + "/" + seekBar_vduration.getMax());
                tv_vinterval.setText("Vibration Interval:" + seekBar.getProgress() + "/" + seekBar.getMax());


            }
        });






        tv = (Button) findViewById(R.id.tvb);

        pwTV = (TextView) findViewById(R.id.input);
        pwTV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });

        hiddentextview = (TextView) findViewById(R.id.del);

        clrTV = (TextView) findViewById(R.id.clr);



        processTVPress(tv, 0);
        processTVPress(hiddentextview, 1);
        processTVPress(clrTV, 2);


    }

    void addToBundleAndOpenActivity(Class cls) {
        Intent intent = new Intent(settings.this, cls);



        Log.e("Duration", valueOf(duration));

        Bundle bundle = new Bundle();


        bundle.putString("userName", user);
        bundle.putInt("duration",seekBar_vduration.getProgress());
        bundle.putInt("interval",seekBar_vinterval.getProgress());
        bundle.putInt("seekbar ",seekBar_vduration.getProgress());





        intent.putExtras(bundle);
        startActivity(intent);
        Log.e("SentBundle", valueOf(bundle));
    }



    void retrieveItemsFromBundle(){
        if (bundle!=null) {

            user=bundle.getString("userName");
            duration=bundle.getInt("duration");
            interval=bundle.getInt("interval");




        }
    }


    private void initializeVariables() {
        seekBar_vinterval= (SeekBar) findViewById(R.id.vibration_intervalseekbar);
        tv_vinterval = (TextView) findViewById(R.id.vibration_interval);
        seekBar_vduration=(SeekBar)findViewById(R.id.vibration_durationseekbar);
        tv_vduration=(TextView)findViewById(R.id.vibrationintervaltv);
    }
    public void fileSaving(){
        try {
            // Creates a file in the primary external storage space of the
            // current application.
            // If the file does not exists, it is created.
            File filePath = new File(this.getFilesDir(), "docs");
            if (!filePath.exists()){
                filePath.mkdir();
                //newFile.createNewFile();
                // Adds a line to the file
                //Uri contentUri = getUriForFile(this, "com.example.morsetranslator.fileprovider", newFile);
            }
            File newFile = new File(filePath, "new2_image.txt");
            File testFile = new File(this.getExternalFilesDir(null), "TestFile1.txt");
            Uri paths = getUriForFile(this, "com.example.morsetranslator.provider", newFile);
            Log.e("URIFilePath", paths.toString());
            BufferedWriter writerImage = new BufferedWriter(new FileWriter(newFile, true /*append*/));
            writerImage.write("Current vibration interval is :" + interval + "\n");
            writerImage.write("Current vibration duration is :" + duration + "\n");


            writerImage.close();
            Log.e("FilePath", getApplicationContext().getApplicationContext().getPackageName() + "/TestFile.txt");


            if (!testFile.exists()) {
                testFile.createNewFile();
                // Adds a line to the file
                BufferedWriter writer = new BufferedWriter(new FileWriter(testFile, true /*append*/));
                writer.write("This is a test file.");
                writer.close();
            } else {

                BufferedWriter writer = new BufferedWriter(new FileWriter(testFile, true /*append*/));
                writer.write("Current Vibration Interval is :" + valueOf(interval) + "\n");
                writer.write("Current Vibration Duration is :" + valueOf(duration) + "\n");

                writer.close();
                Log.e("FilePath", getApplicationContext().getApplicationContext().getPackageName() + "/TestFile.txt");
            }
            // Refresh the data so it can seen when the device is plugged in a
            // computer. You may have to unplug and replug the device to see the
            // latest changes. This is not necessary if the user should not modify
            // the files.
            MediaScannerConnection.scanFile(this,
                    new String[]{testFile.toString()},
                    null,
                    null);
            //String filename="contacts_sid.txt";
            //File testFile1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);
            Uri path = Uri.fromFile(testFile);
            Log.e("FilePath",path.toString());
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
// set the type to 'email'
            emailIntent .setType("vnd.android.cursor.dir/email");
            String[] to = {"roshan82@gmail.com"};
            emailIntent .putExtra(Intent.EXTRA_EMAIL, to);
// the attachment
            emailIntent .putExtra(Intent.EXTRA_STREAM, paths);
// the mail subject
            emailIntent .putExtra(Intent.EXTRA_SUBJECT, "Subject");
            startActivity(Intent.createChooser(emailIntent , "Send email..."));
        } catch (IOException e) {
            Log.e("ReadWriteFile", "Unable to write to the file."+e);
        }
    }


    public void processTVPress(final TextView t, final int t1) {

        t.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e("Tag", "I am here!!");
                        if (t1 == 0) {
                            touchevent = true;



                            new Thread(new TouchVibe()).start();


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

                            if (pw != null && pw.length() > 0) {
                                pw = pw.substring(0, pw.length() - 1);
                            }
                            pwTV.setText(pw);

                        }
                        if (t1 == 2) {

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
            while (touchevent) {
                if (Math.abs(down - System.currentTimeMillis()) > seekBar_vinterval.getProgress()) {
                    Log.d("TAG", "Thread");
                    down = System.currentTimeMillis();
                    Vibrator mvibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);








                    fileWriteString = "Duration"+","+valueOf(duration)+","+"Interval"+"," + valueOf(interval) + ","  + "Time" +","+ valueOf(Calendar.getInstance().getTimeInMillis())+","+"Date"+","+ HAMorseCommon.dateTime() + "\n";

                    HAMorseCommon.writeAnswerToFile(getApplicationContext(), fileWriteString);
                    mvibrator.vibrate(seekBar_vduration.getProgress());


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
                    pwTV.setText(pw);
                    count = 0;
                }



        }
    }
}


