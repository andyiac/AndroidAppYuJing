package com.example.slidingmenu.yujing.client.activity.friends;

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
import com.example.slidingmenu.yujing.client.database.table.Friend;


public class AllPeopleAdapter extends BaseAdapter{

	private Context mContext;
	private List<? extends Map<String, ?>> mData;
	
	public AllPeopleAdapter(Context context, 
			List<? extends Map<String, ?>> data) {
		mContext = context;
		mData = data;
	}
	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = new ViewHolder();
		if(convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.friends_layout_item, null);
			holder.thumb = (ImageView) convertView.findViewById(R.id.friends_thumb);
			holder.name = (TextView) convertView.findViewById(R.id.friends_name);
			holder.status = (TextView) convertView.findViewById(R.id.friends_status);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		HashMap<String, Object> item = (HashMap<String, Object>) getItem(position);
		holder.name.setText(item.get(Friend.name).toString());
		holder.status.setText(item.get(Friend.ID).toString());
		
		return convertView;
	}

	static class ViewHolder{
		ImageView thumb;
		TextView name;
		TextView status;
	}
	
}
