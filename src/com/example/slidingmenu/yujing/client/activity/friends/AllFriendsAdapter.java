package com.example.slidingmenu.yujing.client.activity.friends;

import android.content.ContentValues;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.example.slidingmenu.R;
import com.example.slidingmenu.yujing.client.database.table.Friend;


public class AllFriendsAdapter extends ResourceCursorAdapter{
	

	public AllFriendsAdapter(Context context, int layout, Cursor c,
			boolean autoRequery) {
		super(context, layout, c, false);	
	}
	
	public void refresh() {
		this.getCursor().requery();
	}
	
	@Override
	public ContentValues getItem(int position) {
		ContentValues values = new ContentValues();
		Cursor c = (Cursor) super.getItem(position);
		values.put(Friend._id, c.getInt(c.getColumnIndex(Friend._id)));
		values.put(Friend.ID, c.getString(c.getColumnIndex(Friend.ID)));
		values.put(Friend.name, c.getString(c.getColumnIndex(Friend.name)));
		values.put(Friend.mobile, c.getString(c.getColumnIndex(Friend.mobile)));
		values.put(Friend.sex, c.getString(c.getColumnIndex(Friend.sex)));
		values.put(Friend.photo, c.getString(c.getColumnIndex(Friend.photo)));
		values.put(Friend.UID, c.getString(c.getColumnIndex(Friend.UID)));
		values.put(Friend.address, c.getString(c.getColumnIndex(Friend.address)));
		values.put(Friend.state, c.getString(c.getColumnIndex(Friend.state)));
		return values;
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		 final ContactListItemCache cache = (ContactListItemCache) view.getTag();
		 cursor.copyStringToBuffer(cursor.getColumnIndex(Friend.name), cache.nameBuffer);
		 cursor.copyStringToBuffer(cursor.getColumnIndex(Friend.mobile), cache.numberBuffer);
		 int size = cache.nameBuffer.sizeCopied;
		 int numberSize = cache.numberBuffer.sizeCopied;
		 cache.nameView.setText(cache.nameBuffer.data, 0, size);
		 cache.numberView.setText(cache.numberBuffer.data, 0, numberSize);
		 if(cursor.getInt(cursor.getColumnIndex(Friend.state)) == Friend.ON_LINE){
			 cache.nameView.setText(cache.nameView.getText() + "(在线)");
		 } else {
			 cache.nameView.setText(cache.nameBuffer.data, 0, size);
		 }
		 cache.numberView.setTextColor(R.color.gray);
				
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View view = super.newView(context, cursor, parent);
		ContactListItemCache cache = new ContactListItemCache();
		cache.nameView = (TextView) view.findViewById(R.id.friends_name);
		cache.numberView = (TextView) view.findViewById(R.id.friends_status);
		cache.thunbView = (ImageView) view.findViewById(R.id.friends_thumb);
		view.setTag(cache);
        return view;
	}
	
	
	final static class ContactListItemCache {
	        public TextView nameView;
	        public TextView numberView;
	        public ImageView thunbView;
	        public CharArrayBuffer nameBuffer = new CharArrayBuffer(128);
	        public CharArrayBuffer numberBuffer = new CharArrayBuffer(128);
	}


}
