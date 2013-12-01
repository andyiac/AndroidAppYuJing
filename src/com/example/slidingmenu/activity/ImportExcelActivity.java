package com.example.slidingmenu.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import com.example.slidingmenu.R;
import com.example.slidingmenu.database.AttendanceHelper;
import com.example.slidingmenu.database.table.Student;
import com.example.slidingmenu.entity.MyConstant;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ImportExcelActivity extends Activity {
	static final int FILE_PATH = 0;
	public String CONTACTS_RES = " "; 
	public static final String END_TAG = "导入完毕！！请退出本应用~~"; 
	Workbook book;
	Sheet sheet;
	TextView tv;
	TextView tip;
	EditText et;
	Button btn;
	Button btnSearch;
	ProgressBar bar;
	Handler handler;
	AttendanceHelper attendhelper;
	int added = 0;
	public String id;
	public String studentNo;
	public String studentName;
	public String studentNum;
	public String score;
	public int index;

	HashMap<String,String> map = new HashMap<String, String>();
	HashMap<String,String> map_new = new HashMap<String, String>();
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.import_excel);
       
        Bundle bundle = getIntent().getExtras();
		index = bundle.getInt(MyConstant.KEY_1);
       
		initView();
        handler = new Handler()
        {
        	public void handleMessage(Message msg)
        	{
        		String content = (String) msg.obj;
        	
        		tv.setText(content);
        		bar.setProgress(added);
        		if (content.equals(END_TAG))
        		{
        			bar.setVisibility(View.INVISIBLE);
        			tip.setVisibility(View.INVISIBLE);
        			tv.append("\n本次导入记录，" + String.valueOf(added)+"条");
        		}
        	}
        };
    }
	
	public void initView()
	{
		tv = (TextView) findViewById(R.id.tv);
        tip = (TextView) findViewById(R.id.tip);
        bar = (ProgressBar) findViewById(R.id.bar);
        et = (EditText) findViewById(R.id.fileName);
        btn = (Button) findViewById(R.id.btn);
        btnSearch = (Button) findViewById(R.id.search);
        tip.setVisibility(View.INVISIBLE);
        bar.setVisibility(View.INVISIBLE);
        btn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				CONTACTS_RES = et.getText().toString();
				if (initXls())
				{
					bar.setMax(sheet.getRows());
					tip.setVisibility(View.VISIBLE);
					bar.setVisibility(View.VISIBLE);
					Thread t = new Thread(new Runnable()
					{
						@Override
						public void run()
						{
							add();
						}
					});
					t.start();
				}
			}
		});
        btnSearch.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				Intent i = new Intent(ImportExcelActivity.this,FileSearchActivity.class);
				startActivityForResult(i,FILE_PATH);
			}
		});
	}
	
	
	public boolean initXls()
	{
		String path = CONTACTS_RES;
    	try
		{
    		File file = new File(path);
    		String fileName = file.getName();
    		if (fileName.endsWith(".xls"))
    		{
    			book  =  Workbook.getWorkbook(file);
    			sheet  =  book.getSheet( 0 );			
    			return true;
    		}
    		else
    		{
    			Toast.makeText(this, "请选择Excel文件", Toast.LENGTH_LONG).show();
    		}
		}
		catch (BiffException e)
		{
			e.printStackTrace();
		}
		catch (FileNotFoundException e)
		{
			Toast.makeText(this, "文件名不正确，请确认", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
        return false;           
	}
	
	
	public void add()
	{
		try    
        {
        	//得到ContentResolver对象
			attendhelper = new AttendanceHelper(this);
           
            int rows = sheet.getRows();
            
            for (int i = 1; i < rows; i++)
            {
            	studentNo = sheet.getCell(0, i).getContents();
            	studentName = sheet.getCell(1,i).getContents();
            	studentNum = sheet.getCell(2, i).getContents();  
            	score  = sheet.getCell(3, i).getContents();   

            	
            	/*if (map_new.containsKey(displayName))
            	{
            		continue;
            	}
            	else
            	{
            		map_new.put(displayName, "");
            	}*/
            	Log.e("mytag","importExcel========start");
            	if (studentNo != "" )
            	{
            		Log.e("mytag","importExcel========a");
            		if (map.containsKey(studentNo))
            		{
            			Log.e("mytag","importExcel========b");
            			continue;
            		}
            		Log.e("mytag","importExcel========c");
            		Student.insertStudent(attendhelper, index, studentNo, studentName, studentNum, score, "未出勤");
            		Log.e("mytag","importExcel========d");
            		
            		Message msg =  new Message();
            		msg.obj = studentNo;
            		msg.arg1 = i;
            		handler.sendMessage(msg);
            		added ++;
            	}
            }
            book.close();
            Message msg =  new Message();
			msg.obj = END_TAG;
			handler.sendMessage(msg);
        }  
        catch  (Exception e) 
        {     
            e.printStackTrace();  
        }      
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (resultCode)
		{
		case RESULT_OK:
			Bundle bundle = data.getExtras();
			String filePath = bundle.getString("filePath");
			et.setText(filePath);
			break;
		default:
			break;
		}
		
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		   if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			   Intent intent = new Intent(ImportExcelActivity.this,
						  Addstudents.class);
						  setResult(0, intent);
						  this.finish();
		       return true;
		   }
		   return super.onKeyDown(keyCode, event);
		}
	
}

