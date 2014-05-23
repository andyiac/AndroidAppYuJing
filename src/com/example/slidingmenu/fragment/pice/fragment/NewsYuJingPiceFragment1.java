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

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.example.slidingmenu.R;
import com.example.slidingmenu.fragment.pice.fragment.PullDownView.OnPullDownListener;
import com.example.slidingmenu.tool.Util;
import com.example.slidingmenu.view.NewsWebView;
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

@SuppressLint("HandlerLeak")
public class NewsYuJingPiceFragment1 extends Fragment implements
		OnPullDownListener, OnItemClickListener {
	protected static final String TAG = "NewsYuJingPiceFragment1";
	/** Handler What加载数据完毕 **/
	private static final int WHAT_DID_LOAD_DATA = 0;
	/** Handler What更新数据完毕 **/
	private static final int WHAT_DID_REFRESH = 1;
	/** Handler What更多数据完毕 **/
	private static final int WHAT_DID_MORE = 2;
    private static final boolean D = false;
    Document doc;
	Document next;
	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WHAT_DID_LOAD_DATA: {
				Toast.makeText(NewsYuJingPiceFragment1.this.getActivity(),
						"" + mListView.getAdapter().getCount(),
						Toast.LENGTH_LONG).show();
				// 诉它数据加载完毕;
				break;
			}
			case WHAT_DID_REFRESH: {

				mAdapter.notifyDataSetChanged();
				// 告诉它更新完毕
				break;
			}

			case WHAT_DID_MORE: {
				String body = (String) msg.obj;
				try {
					Log.i(TAG, "body=" + body);
					next = Jsoup.parse(new URL(body), 5000);
					Log.i(TAG, "body2=" + body);
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				Elements es = next.select("span.newslist a");

				for (Element e : es) {
					Map<String, String> map = new HashMap<String, String>();
					String a = e.getElementsByTag("a").text();
					Log.i(TAG, "a=" + a);
					map.put("title", e.getElementsByTag("a").text());
					// 不显示链接地址
					map.put("href", "http://www.hebeinu.edu.cn/"
							+ e.getElementsByTag("a").attr("href"));
					list.add(map);
				}
				mAdapter.notifyDataSetChanged();

				break;
			}
			}

		}

	};
	int page = 1;
	View view;
    List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	private PullDownView mPullDownView;
	private ListView mListView;;
	private SimpleAdapter mAdapter;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.news_yujing_pice_frag_1, null);
		init();
		getMyView();

		return view;
	}

	private void getMyView() {

		mPullDownView = (PullDownView) view
				.findViewById(R.id.lv_news_yujing_bfxy_frag1);

		mPullDownView.setOnPullDownListener(this);

		mListView = mPullDownView.getListView();

		mListView.setOnItemClickListener(this);
		mAdapter = new SimpleAdapter(
				NewsYuJingPiceFragment1.this.getActivity(), list,
				R.layout.news_list_item, new String[] { "title" }, new int[] { android.R.id.text1});

		mListView.setAdapter(mAdapter);

		// 设置可以自动获取更多 滑到最后一个自动获取 改成false将禁用自动获取更多
		mPullDownView.enableAutoFetchMore(true, 1);
		// 隐藏 并禁用尾部
		mPullDownView.setHideFooter();
		// 显示并启用自动获取更多
		mPullDownView.setShowFooter();
		// 隐藏并且禁用头部刷新
		mPullDownView.setHideHeader();
		// 显示并且可以使用头部刷新
		mPullDownView.setShowHeader();

	}

	private void init() {
		if (Util.isNetworkAvailable(NewsYuJingPiceFragment1.this.getActivity())) {
			load();
		} else {
			Toast.makeText(NewsYuJingPiceFragment1.this.getActivity(),
					"网络状态不可用！\n请先设置网络", Toast.LENGTH_LONG).show();
		}

	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	protected void load() {

		try {
			doc = Jsoup.parse(new URL("http://www.hebeinu.edu.cn/xxxw.asp"),
					5000);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		Elements es = doc.select("span.newslist a");
		for (Element e : es) {
			Map<String, String> map = new HashMap<String, String>();
			String a = e.getElementsByTag("a").text();
			map.put("title", a);
			// 不显示链接地址
			map.put("href",
					"http://www.hebeinu.edu.cn/"
							+ e.getElementsByTag("a").attr("href"));
			list.add(map);
		}

	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> map = (HashMap<String, Object>) parent
				.getItemAtPosition(position);
		Intent intent = new Intent();
		intent.setClass(NewsYuJingPiceFragment1.this.getActivity(),
				NewsWebView.class);
		intent.putExtra("newsid", map.get("href").toString());
		startActivity(intent);
		if(D) Toast.makeText(this.getActivity(), "啊，你点中我了 " + position,
				Toast.LENGTH_SHORT).show();
	}

	/** 刷新事件接口 这里要注意的是获取更多完 要关闭 刷新的进度条RefreshComplete() **/
	@Override
	public void onRefresh() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				init();
				try {
					doc = Jsoup.parse(new URL(
							"http://www.hebeinu.edu.cn/xxxw.asp"), 5000);
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				list.clear();
				Elements es = doc.select("span.newslist a");
				for (Element e : es) {
					Map<String, String> map = new HashMap<String, String>();
					String a = e.getElementsByTag("a").text();
					map.put("title", a);
					// 不显示链接地址
					map.put("href", "http://www.hebeinu.edu.cn/"
							+ e.getElementsByTag("a").attr("href"));
					list.add(map);
				}
				// getMyView();
				/** 关闭 刷新完毕 ***/
				mPullDownView.RefreshComplete();// 这个事线程安全的 可看源代码

				Message msg = mUIHandler.obtainMessage(WHAT_DID_REFRESH);
				msg.obj = "After refresh " + System.currentTimeMillis();
				msg.sendToTarget();
			}
		}).start();

	}

	/** 刷新事件接口 这里要注意的是获取更多完 要关闭 更多的进度条 notifyDidMore() **/
	@Override
	public void onMore() {
		new Thread(new Runnable() {

			@Override
			public void run() {

				// Toast.makeText(NewsYuJingPiceFragment1.this.getActivity(),
				// "加载更多", Toast.LENGTH_SHORT).show();
				// Log.i(TAG, "+++++++++++=======");
				// Elements mores = doc.select("DIV.jt");
				String id = null;
				// for (Element more : mores) {
				// id = "http://www.hebeinu.edu.cn/"
				// + more.getElementsByTag("a").get(1).attr("href");
				// Log.i(TAG, "id=" + id);
				// }
				page++;
				id = "http://www.hebeinu.edu.cn/xxxw.asp?page=" + page;
				// http://www.hebeinu.edu.cn/xxxw.asp?page=2
				Log.i(TAG, "1111111111111");
				Log.i(TAG, "id=" + id);
				// Document next = null;

				// 告诉它获取更多完毕 这个事线程安全的 可看源代码

				mPullDownView.notifyDidMore();
				Message msg = mUIHandler.obtainMessage(WHAT_DID_MORE);
				msg.obj = id;
				msg.sendToTarget();
			}
		}).start();
	}

}
