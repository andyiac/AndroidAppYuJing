package com.example.slidingmenu.yujing.client.activity.loginsignin;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.slidingmenu.R;
import com.example.slidingmenu.yujing.client.network.HttpClient;
import com.example.slidingmenu.yujing.client.network.Request;
import com.example.slidingmenu.yujing.client.network.mode.LoginoutResponseParam;
import com.example.slidingmenu.yujing.client.network.mode.RequestParam;
import com.example.slidingmenu.yujing.client.network.mode.ResponseParam;


public class SignProfileActivity extends Activity{
	
	private static final String TAG = "SignProfileActivity";
	private static final boolean D = true;
	
	
	private EditText mobile, name, password;
	private TextView sex;
	private ImageView photo;
	private Button signin;
	
	private Spinner spinner;
	private ArrayAdapter<String> addrAdapter; 
	
	private int picWhich = 0;
	private int signSex = 0;
	private int signAddress = 0;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signin_profile_layout);
		
		mobile = (EditText) findViewById(R.id.sign_phone);
		name = (EditText) findViewById(R.id.sign_name);
		password = (EditText) findViewById(R.id.sign_password);
		
		mobile.setText(((TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number().toString());
		
		sex = (TextView) findViewById(R.id.sign_sex);
		sex.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				signSex = signProfile(R.array.pick_sex, R.string.sign_sex, sex);
			}
		});
		
		spinner = (Spinner) findViewById(R.id.sign_address);
		addrAdapter = new ArrayAdapter<String>(this, 
				R.layout.spinner_layout_item, 
				R.id.text1,
				getResources().getStringArray(R.array.pick_address));
		spinner.setAdapter(addrAdapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				signAddress = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
				
			}
		});
		
		photo = (ImageView) findViewById(R.id.sign_thumb);
		photo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
		signin = (Button) findViewById(R.id.signin);
		signin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(IsEmptyEditText()) {
					return;
				}
				setRequestParam();
			}
		});
		
	}

	private void setRequestParam() {
		
		String[] sex = getResources().getStringArray(R.array.pick_sex);
		String[] addr = getResources().getStringArray(R.array.pick_address);
		String[] signinParam = new String[] {
				mobile.getText().toString(),
				name.getText().toString(),
				password.getText().toString(),
				mobile.getText().toString(),
				"google",
				sex[signSex],
				addr[signAddress]
		};
		
		RequestParam rs = new RequestParam();

		//===========================逻辑有问题？
//		rs.setUserName("");
//		rs.setPassword("");
		
		
		rs.setUserName(name.getText().toString());
		rs.setPassword(password.getText().toString());
		
		
		rs.setRequestType(RequestParam.SIGNIN);
		rs.setRandomKey("1234");
		rs.setParams(signinParam);
		//封装的没问题
		for (int j = 0; j < signinParam.length; j++) {
			Log.i(TAG,"----------sign 中封装的参数--->"+signinParam[j]);
		}
		
		new SigninTask().execute(rs);
	}
	
	private boolean IsEmptyEditText() {
		if(TextUtils.isEmpty(mobile.getText().toString())) {
			mobile.setError("phone number can not be empty");
			return true;
		}
		if(TextUtils.isEmpty(name.getText().toString())) {
			name.setError("name can not be empty");
			return true;
		}
		if(TextUtils.isEmpty(password.getText().toString())) {
			password.setError("password can not be empty");
			return true;
		}
		return false;
	}
	
	protected int signPhoto() {
		return picWhich;
	}
	
	private int signProfile(int array, int title, final TextView show) {
		final String[] ssex = getResources().getStringArray(array);
		Builder builder = new Builder(SignProfileActivity.this);
		builder.setTitle(title);
		builder.setCancelable(false);
		builder.setSingleChoiceItems(array, 1, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				picWhich = which;
			}
		});
		builder.setPositiveButton(R.string.comfirm, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				show.setText(ssex[signSex]);
			}
			
		});
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				show.setText(ssex[1]);
			}
		});
		builder.create().show();
		return picWhich;
		
	}
	
	
	public class SigninTask extends AsyncTask<RequestParam, Integer, Integer>{

		ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(SignProfileActivity.this, "", getText(R.string.waiting));
			super.onPreExecute();
		}
		@Override
		protected Integer doInBackground(RequestParam... params) {
			
			if(!HttpClient.isConnect(SignProfileActivity.this)) {
				return -1;
			}
			
			RequestParam requestParam = params[0];
			
			if(D) Log.i(TAG,"----------params[0]----->>>"+params[0]);
			if(D) Log.i(TAG,"----------requestParam.getJSON()----->>>"+requestParam.getJSON());
			
			String res = Request.request(requestParam.getJSON());
			
			if(D) Log.i(TAG,"---------第一个参数getJSON()"+res);
			
			if ("".equals(res)) {
				return -1;
			}
			try {
				ResponseParam response = new ResponseParam(res);
				System.out.println("返回参数："+"response.getResult()"
				+response.getResult()+"-----------"+response.toString());
				
				if (response.getResult() != LoginoutResponseParam.RESULT_SUCCESS) {
					return -1;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return 0;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			dialog.dismiss();
			if(result == 0) {
				Toast.makeText(SignProfileActivity.this, "signin succ", Toast.LENGTH_SHORT).show();
				finish();
			} else {
				Toast.makeText(SignProfileActivity.this, "signin fail", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
}








