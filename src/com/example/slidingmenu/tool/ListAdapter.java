package com.example.slidingmenu.tool;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.slidingmenu.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 13-9-21.
 */
public class ListAdapter extends BaseAdapter {


    private LayoutInflater layoutInflater;
    private View.OnClickListener onClickListener;
    private String[] stringArr;
    private String[] phoneArr;
    private Map<String, String> map = new HashMap<String, String>();
    private Map<String, String> map2 = new HashMap<String, String>();

    public ListAdapter(Context context, String[] arr, String[] phoneArr, View.OnClickListener listener, Map<String, String> map, Map<String, String> map2) {
        layoutInflater = LayoutInflater.from(context);
        this.onClickListener = listener;
        stringArr = arr;
        this.map = map;
        this.map2 = map2;
        this.phoneArr = phoneArr;
        Log.d("log22", map2 + "");
    }

    public int getCount() {
        return stringArr == null ? 0 : stringArr.length;
    }

    public Object getItem(int position) {

        Log.d("position", position + "--->" + stringArr[position]);


        if (stringArr != null) {
            String string = map.get(stringArr[position]);
            return string;
        }
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listitem, null);
            final LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.linear);


            holder = new ViewHolder();
//            holder.firstCharHintTextView = (TextView) convertView
//                    .findViewById(R.id.text_first_char_hint);
            holder.nameTextView = (TextView) convertView.findViewById(R.id.text_website_name);
            holder.phoneTextView = (TextView) convertView.findViewById(R.id.text_website_phone);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.nameTextView.setText(map.get(stringArr[position]));
        holder.phoneTextView.setText(phoneArr[position]);
        int idx = position - 1;
        char previewChar = idx >= 0 ? stringArr[idx].charAt(0) : ' ';
        char currentChar = stringArr[position].charAt(0);
        if (currentChar != previewChar) {
//            holder.firstCharHintTextView.setVisibility(View.VISIBLE);
//            holder.firstCharHintTextView.setText(String.valueOf(currentChar));
        } else {
            //实例化一个CurrentView后，会被多次赋值并且只有最后一次赋值的position是正确
//            holder.firstCharHintTextView.setVisibility(View.GONE);
        }
        return convertView;
    }

    public final class ViewHolder {
        public TextView firstCharHintTextView;
        public TextView nameTextView;
        private TextView phoneTextView;

    }
}
