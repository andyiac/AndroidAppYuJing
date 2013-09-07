
package com.example.slidingmenu.yujing.client.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.content.ContentValues;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.slidingmenu.R;


public class Utils {
	
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
