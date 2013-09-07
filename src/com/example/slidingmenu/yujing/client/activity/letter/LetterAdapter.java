package com.example.slidingmenu.yujing.client.activity.letter;

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
import com.example.slidingmenu.yujing.client.database.table.PrivateLetter;
import com.example.slidingmenu.yujing.client.utils.Utils;

public class LetterAdapter extends ResourceCursorAdapter{
	
	public LetterAdapter(Context context, int layout, Cursor c,
			boolean autoRequery) {
		super(context, layout, c, false);
	}
	
	@Override
	public ContentValues getItem(int position) {
		ContentValues values = new ContentValues();
		Cursor c = (Cursor) super.getItem(position);
		values.put(PrivateLetter._id, c.getInt(c.getColumnIndex(PrivateLetter._id)));
		values.put(PrivateLetter.PrivateLetterID, c.getLong(c.getColumnIndex(PrivateLetter.PrivateLetterID)));
		values.put(PrivateLetter.name, c.getString(c.getColumnIndex(PrivateLetter.name)));
		values.put(PrivateLetter.time, c.getInt(c.getColumnIndex(PrivateLetter.time)));
		values.put(PrivateLetter.photo, c.getString(c.getColumnIndex(PrivateLetter.photo)));
		values.put(PrivateLetter.PrivateLetterUID, c.getLong(c.getColumnIndex(PrivateLetter.PrivateLetterUID)));
		values.put(PrivateLetter.isSend, c.getInt(c.getColumnIndex(PrivateLetter.isSend)) == 0 ? false : true);
		return values;
	}

	public void refresh() {
		this.getCursor().requery();
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		 final ContactListItemCache cache = (ContactListItemCache) view.getTag();
		 cursor.copyStringToBuffer(cursor.getColumnIndex(PrivateLetter.name), cache.nameBuffer);
		 cursor.copyStringToBuffer(cursor.getColumnIndex(PrivateLetter.time), cache.timeBuffer);
		 cursor.copyStringToBuffer(cursor.getColumnIndex(PrivateLetter.content), cache.contentBuffer);
		 boolean bool = cursor.getInt(cursor.getColumnIndex(PrivateLetter.isSend)) == 0 ? true : false;
		 int size = cache.nameBuffer.sizeCopied;
		 int contentSize = cache.contentBuffer.sizeCopied;
		 cache.nameView.setText(cache.nameBuffer.data, 0, size);
		 cache.dateView.setText(Utils.timeFormat(cursor.getInt(cursor.getColumnIndex(PrivateLetter.time))));
		 cache.contentView.setText(cache.contentBuffer.data, 0, contentSize);
		 if(bool) {
			 cache.sender.setImageResource(R.drawable.log_incoming);
		 } else {
			 cache.sender.setImageResource(R.drawable.log_outgoing);
		 }
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View view = super.newView(context, cursor, parent);
		ContactListItemCache cache = new ContactListItemCache();
		cache.nameView = (TextView) view.findViewById(R.id.letter_name);
		cache.contentView = (TextView) view.findViewById(R.id.letter_content);
		cache.thumbView = (ImageView) view.findViewById(R.id.letter_thumb);
		cache.dateView = (TextView) view.findViewById(R.id.letter_date);
		cache.sender = (ImageView) view.findViewById(R.id.reciver_sender);
		view.setTag(cache);
        return view;
	}
	
	final static class ContactListItemCache {
        public TextView nameView;
        public TextView contentView;
        public TextView dateView;
        public ImageView thumbView;
        public ImageView sender;
        public CharArrayBuffer nameBuffer = new CharArrayBuffer(128);
        public CharArrayBuffer timeBuffer = new CharArrayBuffer(128);
        public CharArrayBuffer contentBuffer = new CharArrayBuffer(128);
	}


}
