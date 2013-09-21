package com.example.slidingmenu.map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.slidingmenu.R;

/**
 * Created by andyiac on 13-9-21.
 */
public class RapidPositioning extends Activity {
    Button back;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.rapid_positioning);
       back = (Button)findViewById(R.id.btn_rapid_positioning);
       back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               RapidPositioning.this.finish();
           }
       });


    }
}