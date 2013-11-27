package com.example.slidingmenu.view;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.slidingmenu.R;

/**
 * 描述: 在Android中获取网页里表单中的数据
 */
public class NewsWebView extends Activity {
	private static final String TAG = "NewsWebView";
	Document doc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.news_web_view);

		Intent intent = getIntent();
		// intent.getExtra("newsid","http://www.hebeinu.edu.cn/shownews.asp?newsid=1822");
		String newsid = intent.getStringExtra("newsid");
		init(newsid);
	}

	@SuppressWarnings("null")
	private void init(String newsid) {
		// mWebView=(WebView) findViewById(R.id.webView);
		Button btnBack = (Button) findViewById(R.id.showLeft);
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				NewsWebView.this.finish();
			}
		});

		try {
			doc = Jsoup.parse(new URL(newsid), 5000);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Log.i(TAG, newsid);
		String name = null;
		Elements links = doc.select("span.xwbt");
		for (Element link : links) {
			Log.i(TAG, "===========");
			name = link.text().toString().trim();
		}
		Log.i(TAG, "name=" + name);
		StringBuilder s = new StringBuilder();
		String temp = null;
		Element time = doc.select("p").first();
		temp = time.text().toString().trim();
		Log.i(TAG, "time=" + temp);
		int i = temp.length() - 7;
		String time2 = temp.substring(0, i);
		Log.i(TAG, "time2=" + time2);

		
		
		Elements bodys = doc.select("TD#showbgcolor.lightblack p");
		for (Element body : bodys) {
			temp = body.text().toString().trim();
			Log.i(TAG, "a=" + temp);
			Log.i(TAG, "----------");
			s.append(temp).append("\n");
		}
		Log.i(TAG, "s=" + s);

	}

}