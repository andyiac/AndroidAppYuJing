/*
 * Copyright (C) 2012 yueyueniao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.slidingmenu.fragment.pice.fragment;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.slidingmenu.R;

import java.util.ArrayList;


public class SensorEmergencyPiceFragment extends Fragment {

    private SensorManager sensorManager;
    private Vibrator vibrator;

    private static final String TAG = "TestSensorActivity";
    private static final int SENSOR_SHAKE = 10;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.page2, null);
        TextView textView = (TextView)view.findViewById(R.id.tv_page2);
        textView.setText("摇一摇发送短信广播突发事件");
        textView.setTextSize(20);
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
        sensorManager = (SensorManager) this.getActivity().getSystemService(Context.SENSOR_SERVICE);
        vibrator = (Vibrator) this.getActivity().getSystemService(Context.VIBRATOR_SERVICE);

	}

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (sensorManager != null) {// 注册监听器
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
            // 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率
        }
    }

    @Override
     public void onStop() {
        super.onStop();
        if (sensorManager != null) {// 取消监听器
            sensorManager.unregisterListener(sensorEventListener);
        }
    }

    /**
     * 重力感应监听
     */
    private SensorEventListener sensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            // 传感器信息改变时执行该方法
            float[] values = event.values;
            float x = values[0]; // x轴方向的重力加速度，向右为正
            float y = values[1]; // y轴方向的重力加速度，向前为正
            float z = values[2]; // z轴方向的重力加速度，向上为正
            Log.i(TAG, "x轴方向的重力加速度" + x + "；y轴方向的重力加速度" + y + "；z轴方向的重力加速度" + z);
            // 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
            int medumValue = 19;// 三星 i9250怎么晃都不会超过20，没办法，只设置19了 （此值越小，越容易触发事件）
            if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue) {
                vibrator.vibrate(200);
                Message msg = new Message();
                msg.what = SENSOR_SHAKE;
                handler.sendMessage(msg);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    /**
     * 动作执行
     */
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SENSOR_SHAKE:
                    /**
                     * 调用Android系统发送短信功能
                     */
//                    Uri uri = Uri.parse("smsto:10001");
//                    Intent intent = new Intent(Intent.ACTION_SENDTO,uri);
//                    intent.putExtra("sms_body", "108");
//                    startActivity(intent);

                    /**
                     * 后台发送短信
                     */
                    SmsManager smsManager = SmsManager.getDefault();
                    ArrayList<String> texts = smsManager.divideMessage("108");//拆分短信,短信字数太多了的时候要分几次发
                    for(String text : texts){
                        smsManager.sendTextMessage("10001", null, text, null, null);//发送短信,mobile是对方手机号
                    }

                    Toast.makeText(SensorEmergencyPiceFragment.this.getActivity(), "广播突发事件成功", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "广播突发事件成功");

                    break;
            }
        }

    };

}

