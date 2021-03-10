package com.example.morsetranslator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.morsetranslator.settings.duration;
import static com.example.morsetranslator.settings.interval;
import static com.example.morsetranslator.HAMorseCommon.user;

public class information extends AppCompatActivity {


    Bundle bundle;

    TextView tv1;
    TextView tv2;
    TextView tv3;
    TextView tv4;
    Button bt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information);
        bt=(Button)findViewById(R.id.next);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToBundleAndOpenActivity(mainscreen.class);

            }


        void addToBundleAndOpenActivity(Class cls){
            Intent intent = new Intent(information.this, cls);

            Bundle bundle=new Bundle();

            bundle.putString("userName",user);
            bundle.putInt("duration", duration);
            bundle.putInt("interval", interval);
            intent.putExtras(bundle);
            startActivity(intent);
            Log.e("SentBundle",String.valueOf(bundle));
        }

        void retrieveItemsFromBundle(){
            if (bundle!=null) {

                user=bundle.getString("userName");
                duration=bundle.getInt("duration");
                interval=bundle.getInt("interval");
            }

        }


    });
    }
}


