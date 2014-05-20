package com.example.slidingmenu.welcome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import com.example.slidingmenu.R;
import com.example.slidingmenu.activity.LoginActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by andyiac on 13-9-22.
 */
public class WelcomeActivity extends Activity {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set screenOrientation landscape
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.welcome);

//        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
//        CLOCL_COLOR =   settings.getInt("mColor", CLOCL_COLOR);
//        TextView textView = (TextView)findViewById(R.id.fullscreen_content);
//        textView.setTextColor(CLOCL_COLOR);

        final Intent it = new Intent(this, LoginActivity.class);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                startActivity(it);
                WelcomeActivity.this.finish();
            }
        };
        timer.schedule(task, 1000 * 1); // 1秒后自动跳转
    }
}