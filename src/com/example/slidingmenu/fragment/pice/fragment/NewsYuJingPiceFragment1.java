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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.slidingmenu.R;
import com.example.slidingmenu.tool.Util;
import com.example.slidingmenu.view.NewsWebView;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NewsYuJingPiceFragment1 extends Fragment {

    Document doc;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_yujing_pice_frag_1, null);
        if(Util.isNetworkAvailable(NewsYuJingPiceFragment1.this.getActivity())){
            load(view);
        }else {
            Toast.makeText(NewsYuJingPiceFragment1.this.getActivity(), "网络状态不可用！\n请先设置网络", Toast.LENGTH_LONG).show();
        }


        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    protected void load(View view) {

        try {
            doc = Jsoup.parse(new URL("http://www.hebeinu.edu.cn/xxxw.asp"), 5000);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Elements es = doc.select("span.newslist a");
        //html body div#wrapper div#boxout div#center  div#proDownload ul li a
        //span.newslist a
        for (Element e : es) {
            Map<String, String> map = new HashMap<String, String>();
            String a = e.getElementsByTag("a").text();
            map.put("title", e.getElementsByTag("a").text());
            //不显示链接地址
            map.put("href", "http://www.hebeinu.edu.cn/"
                    + e.getElementsByTag("a").attr("href"));
            list.add(map);
        }

        ListView listView = (ListView) view.findViewById(R.id.lv_news_yujing_bfxy_frag1);
        listView.setAdapter(new SimpleAdapter(this.getActivity(), list, android.R.layout.simple_list_item_2,
                new String[]{"title", "href"}, new int[]{
                android.R.id.text1, android.R.id.text2
        }));
        listView.setOnItemClickListener(onItemClickListener);

    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            HashMap<String, Object> map = (HashMap<String, Object>) adapterView.getItemAtPosition(i);
            Intent intent = new Intent();
            intent.setClass(NewsYuJingPiceFragment1.this.getActivity(), NewsWebView.class);
            intent.putExtra("newsid", map.get("href").toString());
            startActivity(intent);
        }
    };


}

