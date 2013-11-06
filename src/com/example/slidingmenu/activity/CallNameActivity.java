package com.example.slidingmenu.activity;


import java.util.ArrayList;
import java.util.List;
import com.example.slidingmenu.R;
import com.example.slidingmenu.database.AttendanceHelper;
import com.example.slidingmenu.database.table.Student;
import com.example.slidingmenu.entity.DateManager;
import com.example.slidingmenu.entity.MyConstant;
import com.example.slidingmenu.entity.MyData;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class CallNameActivity extends Activity {
	
	
	private int index;
	private ListView list;  
	AttendanceHelper attendhelper;
	Cursor cursor;
	SimpleCursorAdapter mAdapter=null;
	String[] from;
	int[] to;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		/*requestWindowFeature(Window.FEATURE_NO_TITLE);*/
		setContentView(R.layout.call_name);
		//初始化		
		init();
	}	
	
	/**
	 * 初始化
	 */
	private void init()
	{
		Bundle bundle=getIntent().getExtras();
		index=bundle.getInt(MyConstant.KEY_1);
		Log.e("mytag", "CallNameActivity_position====="+ index);
		/*cursor = attendenHelper.getStudents(index);*/
		initBluetooth();
		initListView();
		initBtn();
	}
	
	/**
	 * 初始化列表
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void initListView()
	{   
		attendhelper = new AttendanceHelper(this);
		list = (ListView) findViewById(R.id.list_name);
		
		cursor = Student.getStudentName(attendhelper, index);
		Log.e("mytag", "Student====="+cursor.getColumnName(1).toString());
		
		from = new String[] {"sname","attendance","grade"};
		to = new int[]{R.id.name,R.id.text,R.id.score};
		mAdapter = new SimpleCursorAdapter(this,R.layout.search_name,cursor,from,to,2);
		
		list.setAdapter(mAdapter);
	}
	
	/**
	 * 初始化按钮
	 */
	private void initBtn()
	{
		Button btn = (Button)findViewById(R.id.btn_add);
		Button back = (Button)findViewById(R.id.btn_call_name);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				closeBluetooth();
				CallNameActivity.this.finish();
				
			}
		});
		/*btn.setBackgroundResource(R.drawable.btn_style_blue);
		btn.getBackground().setAlpha(200);*/
		
		//监听按钮
		btn.setOnClickListener(new OnClickListener() 
		{
            public void onClick(View v) 
            {
            	startBluetooth();
            }
        });			
	}
	
	@Override
	protected void onDestroy() //不用的时候关闭蓝牙
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		closeBluetooth();
	}
	
	//---------------------------------蓝牙---------------------------------//
	
	/* 蓝牙适配器 */
	private BluetoothAdapter bluetoothAdapter = null;	
	/* 搜索到的蓝牙设备列表 */
	private List<BluetoothDevice> devicesList = new ArrayList<BluetoothDevice>();//保存搜索到的蓝牙设备列表
	/* 蓝牙接收器 */
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() 
	{  
		@Override  
		public void onReceive(Context context, Intent intent) 
		{  
			String action = intent.getAction();   
			//接收器_搜索到设备时调用 
			if (BluetoothDevice.ACTION_FOUND.equals(action)) 
			{				
				/* 从intent中取得搜索结果数据 */
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				/* 将结果添加到列表中 */
				devicesList.add(device);
				Log.e("mytag","device===="+ device);
				Log.e("mytag","search===" + devicesList);
			}		
			else if
			//接收器_搜索完成时调用
			(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
			{
				setProgressBarIndeterminateVisibility(false);//停止转圈
				setTitle("搜索完成");
				getResult();
			} 			
		} 
	}; 	

	/**
	 * 获得搜索列表
	 * @return
	 */
 	private List<BluetoothDevice> getList()//得到临时列表
 	{
 		return devicesList;
 	}
 	
 	
 	
 	private void getResult() {
 		Log.e("mytag", "getList().size()===" + cursor.getCount());
 		 for(int i=0;i<cursor.getCount();i++)//循环当前班级的学生链表
 		{  
  		   Cursor c = Student.getStudentGrade(attendhelper, index, i);
 			Student.updateStudentGrade(attendhelper, index, i, c.getInt(3)-1);
 			for(int j=0;j<getList().size();j++)//设置每个学生都未出勤，每个学生都减一分
 			{   Log.e("mytag", "devicesListgetList().get(j).getName()===" +getList().get(j).getName().toString());
 				if(cursor.getString(3).equals(getList().get(j).getName().toString()))//判断第I个学生的名字是否和临时链表里第J个学生名字一样
 				{
 					Student.updateStudentAttend(attendhelper, index, i, "出勤");//确定出勤
 					Student.updateStudentGrade(attendhelper, index, i, c.getInt(3)+1);
 				}
 			}
 		}
 		
 		
 	}
 	
 	
 	
 	/**
 	 * 初始化蓝牙设备
 	 */
	private void initBluetooth()
	{
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();//注册蓝牙适配器
		if (bluetoothAdapter == null)
		{   
			Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();//不支持蓝牙的设备 
			this.finish();   
			return;   
		}		
		bluetoothAdapter.enable();
	    // Register for broadcasts when a device is discovered 发现设备时监听过滤   
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);   
		registerReceiver(mReceiver, filter);   
		//Register for broadcasts when discovery has finished //搜索结束时监听过滤过滤  
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);   
		registerReceiver(mReceiver, filter);  
	}
	
	/**
	 * 开始搜索
	 */
	private void startBluetooth()
 	{   
 		setProgressBarIndeterminateVisibility(true); 
 		setTitle("正在搜索..."); 
		/* 清空蓝牙设备列表*/
		for(int i=0;i<devicesList.size();i++)
		{
			devicesList.remove(i);
		} 
		Log.e("mytag","start===" + devicesList);
		devicesList.clear();
		Log.e("mytag","then==" + devicesList);
 		bluetoothAdapter.startDiscovery();
 	} 
	
 	/**
 	 * 关闭蓝牙
 	 */
 	private void closeBluetooth()
	{
		if (bluetoothAdapter != null) 
		{   
			bluetoothAdapter.cancelDiscovery();    
			bluetoothAdapter.disable();
		} 
		unregisterReceiver(mReceiver); 
}



 /*class SearchBaseAdapter extends BaseAdapter 
{
	private LayoutInflater mInflater;
	private Context context;
	private int index;
	
    public SearchBaseAdapter(Context context,int index)
    {
        this.context = context;
        mInflater = LayoutInflater.from(this.context);
        this.index = index;
    }
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return MyData.getInstance().getClassList().get(index).getStudentList().size();
		return cursor.getCount();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return MyData.getInstance().getClassList().get(index).getStudentList().get(position);
		return cursor.getPosition();
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		ViewHolder holder;
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.search_name, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.attendance = (TextView) convertView.findViewById(R.id.text);
            holder.score = (TextView) convertView.findViewById(R.id.score);
            holder.btn = (Button) convertView.findViewById(R.id.btn);
            convertView.setTag(holder);
        }
        else
        {
        	holder = (ViewHolder)convertView.getTag();
        }
        
        holder.name.setText(MyData.getInstance().getClassList().get(index).getStudentList().get(position).getName());
        holder.name.setText(cursor.getColumnName(2));
        holder.attendance.setText("未出勤");
        holder.score.setText(cursor.getColumnName(4));
       
        if(MyData.getInstance().getClassList().get(index).getStudentList().get(position).getAttendance())
        {
        	 holder.attendance.setText("出勤");
        }
        else
        {
        	 holder.attendance.setText("未出勤");
        }        
        holder.score.setText(MyData.getInstance().getClassList().get(index).getStudentList().get(position).getScore()+"");
        holder.btn.setOnClickListener(new OnClickListener()
        {			
			@Override
			public void onClick(View v)
			{
				//发送消息
				sms(position);
			}
		});         
		return convertView;
	}
	
	private void sms(int position)
	{
		Uri uri = Uri.parse("smsto:"+MyData.getInstance().getClassList().get(index).getStudentList().get(position).getNum());  
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);  
		intent.putExtra("sms_body", "快点来上课吧！");  
		context.startActivity(intent); 
	}
	
	private class ViewHolder
	{
		 TextView name;
		 TextView attendance;
		 TextView score;
		 Button btn;
	}
}*/
}

