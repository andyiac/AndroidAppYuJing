package com.example.slidingmenu.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.example.slidingmenu.R;
import com.example.slidingmenu.tool.FileAdapter;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class FileSearchActivity extends ListActivity {
		  private List<String> items=null;
		  private List<String> paths=null;
		  private String rootPath="/";
		  private ListView mList;
		  
		@Override
		protected void onCreate(Bundle savedInstanceState)
		{
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.file_list);
		
			mList = (ListView)findViewById(android.R.id.list);
			mList.setOnItemLongClickListener(new OnItemLongClickListener()
			{
				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3)
				{
					File file=new File(paths.get(position));
					 if (!file.canRead())
					 {
						 Toast.makeText(getBaseContext(), "权限不够！", Toast.LENGTH_SHORT).show();
						 return true;
					 }
					returnFilePath(file);
					return false;
				}
			});
			getFileDir(rootPath);
			Toast.makeText(this, "长按文件夹选择路径", Toast.LENGTH_LONG).show();
		}
		
		private void getFileDir(String filePath)
		{
			setTitle(filePath);
		    items=new ArrayList<String>();
		    paths=new ArrayList<String>();
		    File f=new File(filePath);  
		    File[] files=f.listFiles();
		    
		    if(!filePath.equals(rootPath))
		    {
		      items.add("backRoot");
		      paths.add(rootPath);
		      items.add("back");
		      paths.add(f.getParent());
		    }
		    
		    for(int i=0;i<files.length;i++)
		    {
		      File file=files[i];
		      items.add(file.getName());
		      paths.add(file.getPath());
		    }
		    setListAdapter(new FileAdapter(this,items,paths));
		}
		
		protected void onListItemClick(ListView l,View v,
	            int position,long id)
		{
			 File file=new File(paths.get(position));
			 if (!file.canRead())
			 {
				 Toast.makeText(this, "权限不够！", Toast.LENGTH_SHORT).show();
				 return;
			 }
			 
			 if (file.isDirectory())
		      {
		        getFileDir(paths.get(position));
		      }
		      else
		      {
		        returnFilePath(file);
		      }
		}
		
		private void returnFilePath(File file)
		{
			String filePath =  file.getPath();
			Intent i = getIntent();
			Bundle bundle = new Bundle();
			bundle.putString("filePath",filePath);
			i.putExtras(bundle);
			FileSearchActivity.this.setResult(RESULT_OK, i);
			finish();
		}
	}

