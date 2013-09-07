package com.example.slidingmenu.yujing.client.activity.broadcast;

import java.util.HashMap;

import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.example.slidingmenu.R;
import com.example.slidingmenu.yujing.client.database.table.Topic;
import com.example.slidingmenu.yujing.client.utils.Utils;


public class BroadCastAdapter extends ResourceCursorAdapter{
	
	public BroadCastAdapter(Context context, int layout, Cursor c,
			boolean autoRequery) {
		super(context, layout, c, false);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		 final ContactListItemCache cache = (ContactListItemCache) view.getTag();
		 cursor.copyStringToBuffer(cursor.getColumnIndex(Topic.name), cache.nameBuffer);
		 cursor.copyStringToBuffer(cursor.getColumnIndex(Topic.content), cache.numberBuffer);
		 int size = cache.nameBuffer.sizeCopied;
		 int numberSize = cache.numberBuffer.sizeCopied;
		 cache.nameView.setText(cache.nameBuffer.data, 0, size);
		 cache.contentView.setText(cache.numberBuffer.data, 0, numberSize);
		 cache.dateView.setText(Utils.timeFormat(cursor.getInt(cursor.getColumnIndex(Topic.time))));
	}
	
	public void refresh() {
		this.getCursor().requery();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public HashMap getItem(int position) {
		Cursor c = (Cursor) super.getItem(position);
		HashMap map = new HashMap();
		map.put(Topic._id, c.getInt(c.getColumnIndex(Topic._id)));
		map.put(Topic.ID, c.getLong(c.getColumnIndex(Topic.ID)));
		map.put(Topic.content, c.getString(c.getColumnIndex(Topic.content)));
		map.put(Topic.name, c.getString(c.getColumnIndex(Topic.name)));
		map.put(Topic.time, c.getInt(c.getColumnIndex(Topic.time)));
		map.put(Topic.photo, c.getString(c.getColumnIndex(Topic.photo)));
		return map;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View view = super.newView(context, cursor, parent);
		ContactListItemCache cache = new ContactListItemCache();
		cache.nameView = (TextView) view.findViewById(R.id.broadcast_name);
		cache.contentView = (TextView) view.findViewById(R.id.broadcast_content);
		cache.thumbView = (ImageView) view.findViewById(R.id.broadcast_thumb);
		cache.dateView = (TextView) view.findViewById(R.id.broadcast_date);
		view.setTag(cache);
        return view;
	}
	
	final static class ContactListItemCache {
	        public TextView nameView;
	        public TextView contentView;
	        public TextView dateView;
	        public ImageView thumbView;
	        public CharArrayBuffer nameBuffer = new CharArrayBuffer(128);
	        public CharArrayBuffer numberBuffer = new CharArrayBuffer(128);
	}

}
