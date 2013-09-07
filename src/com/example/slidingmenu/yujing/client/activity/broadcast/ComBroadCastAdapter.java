package com.example.slidingmenu.yujing.client.activity.broadcast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.slidingmenu.R;
import com.example.slidingmenu.yujing.client.utils.Utils;

public class ComBroadCastAdapter extends BaseAdapter{
	
	private List<? extends Map<String, ?>> comData;
	private Context context;
	
	public ComBroadCastAdapter(Context context, List<? extends Map<String, ?>> comData) {
		this.comData = new ArrayList<HashMap<String, Object>>();
		this.comData = comData;
		this.context = context;
	}

	@Override
	public int getCount() {
		return this.comData.size();
	}

	@Override
	public Object getItem(int position) {
		return this.comData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ListItemCache cache = new ListItemCache();
		
		if(convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView  = layoutInflater.inflate(R.layout.broadcast_layout_item, null);
			cache.contentView = (TextView) convertView.findViewById(R.id.broadcast_content);
			cache.nameView = (TextView) convertView.findViewById(R.id.broadcast_name);
			cache.dateView = (TextView) convertView.findViewById(R.id.broadcast_date);
			cache.thumbView = (ImageView) convertView.findViewById(R.id.broadcast_thumb);
			convertView.setTag(cache);
		} else {
			cache = (ListItemCache) convertView.getTag();
		}
		
		HashMap<String, Object> item = (HashMap<String, Object>) getItem(position);
		cache.contentView.setText(item.get("Topic_Com_Content").toString());
		cache.dateView.setText(Utils.timeFormat(Integer.parseInt(item.get("Topic_Com_Time").toString())));
		cache.nameView.setText(item.get("Topic_Com_From").toString());
		
		return convertView;
	}

	final static class ListItemCache {
        public TextView nameView;
        public TextView contentView;
        public TextView dateView;
        public ImageView thumbView;
}
}
