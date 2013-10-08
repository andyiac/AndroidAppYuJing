package com.example.slidingmenu.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.example.slidingmenu.R;
import java.util.StringTokenizer;

import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * 描述:
 * 在Android中获取网页里表单中的数据
 */
public class NewsWebView extends Activity {
    private WebView mWebView;
    private String date =null;
    private String email = null;
    private String username = null;
    private String sex = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.news_web_view);

        Intent intent = getIntent();
//        intent.getExtra("newsid","http://www.hebeinu.edu.cn/shownews.asp?newsid=1822");
        String newsid = intent.getStringExtra("newsid");
        init(newsid);
    }
    private void init(String newsid){
        mWebView=(WebView) findViewById(R.id.webView);
        Button btnBack = (Button)findViewById(R.id.showLeft);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewsWebView.this.finish();
            }
        });
        initWebViewSettings();
//        mWebView.loadUrl("file:///android_asset/form.html");
//        mWebView.loadUrl("http://www.hebeinu.edu.cn/shownews.asp?newsid=1822");
        mWebView.loadUrl(newsid);
        //注意addJavascriptInterface方法中第二参数
        //它表示我们的java对象javaClass的别名.
        //这样Javascript就可以通过该别名来调用Android中的方法
        //即Javascript代码中的:window.testform.send(date+"|"+email+"|"+name+"|"+sex);
        //send是方法名
        //testform是别名
        mWebView.addJavascriptInterface(new Object() {
            public void send(String userInfo) {
                StringTokenizer userInfoStringTokenizer = new StringTokenizer(userInfo, "|");
                date = userInfoStringTokenizer.nextToken();
                email = userInfoStringTokenizer.nextToken();
                username = userInfoStringTokenizer.nextToken();
                sex = userInfoStringTokenizer.nextToken();
                System.out.println("userInfoStringTokenizer="+userInfoStringTokenizer.toString());
                System.out.println("date=" + date);
                System.out.println("email=" + email);
                System.out.println("username=" + username);
                System.out.println("sex=" + sex);
            };
        }, "testform");


    }
    private void initWebViewSettings(){
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setPluginsEnabled(true);
        mWebView.requestFocus();
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
    }
}