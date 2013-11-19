package com.example.slidingmenu.activity;

import com.example.slidingmenu.R;
import com.example.slidingmenu.database.AttendanceHelper;
import com.example.slidingmenu.database.table.Myclass;
import com.example.slidingmenu.entity.MyConstant;
import com.example.slidingmenu.fragment.pice.fragment.AttendancePiceFragment2;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class DialogClassActivity extends Activity
{
	private int id;//
	TextView view;
	Button sureBtn,exitBtn;
	EditText classname;
	AttendanceHelper attendHelper;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		//设置布局文件
		setContentView(R.layout.dialog_class);
		init();
	}
	private void init() {
		attendHelper = new AttendanceHelper(this);
		Bundle bundle=getIntent().getExtras();
		
		id=bundle.getInt(MyConstant.KEY_1);
		Log.e("mytag","deleteclass===="+ id);
		
		view = (TextView)findViewById(R.id.dialog_change_name);
		view.setText("修改班级名称");
		classname = (EditText)findViewById(R.id.classname);
		String name = Myclass.getClassName(attendHelper, id);
		classname.setText(name);
		
		sureBtn = (Button)findViewById(R.id.sureBtn);
		exitBtn = (Button)findViewById(R.id.exitBtn);
		sureBtn.setOnClickListener(new SureListener());
		exitBtn.setOnClickListener(new ExitListener());
		
	}
	class SureListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String cname = classname.getText().toString();
	 		Myclass.updateClass(attendHelper, cname, id);
	 		Intent intent = new Intent(DialogClassActivity.this,AttendancePiceFragment2.class);
	 		setResult(0, intent);
            DialogClassActivity.this.finish();
		}
		
	}
	
	class ExitListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			DialogClassActivity.this.finish(); 
		}
		
	}
}
	
