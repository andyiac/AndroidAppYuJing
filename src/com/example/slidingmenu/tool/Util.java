package com.example.slidingmenu.tool;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.slidingmenu.R;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by andyiac on 13-10-8.
 */
public  class Util {


    /**
     * 判断网络状态是否可用
     * @param activity
     * @return
     */
    public static boolean isNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
	 * 格式化时间
	 *
	 * @ param msgDate
	 * @return
	 */
	public static String timeFormat(int time) {
		System.out.println(time);

		long Time = new Long(time);
		long dateTime = (Time * 1000);
		Date date = new Date(dateTime);
		SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
		return format.format(date);
	}
	public static void myToast( Activity activity, String data, int images) {
		LayoutInflater inflater	= activity.getLayoutInflater();
		View layout = inflater.inflate(R.layout.toast, null);

		ImageView icon = (ImageView) layout.findViewById(R.id.toast_icon);
		TextView text = (TextView) layout.findViewById(R.id.toast_text);

		icon.setImageResource(images);
		text.setText(data);

		Toast toast = new Toast(activity);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	public static ContentValues hashMapToContentValues(HashMap<String, Object> map) {

		ContentValues values = new ContentValues();
		try {
			Constructor<ContentValues> c = ContentValues.class.getDeclaredConstructor(HashMap.class);
			c.setAccessible(true);
			values = c.newInstance(map);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return values;
	}
}
