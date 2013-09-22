package com.example.slidingmenu.tool;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.slidingmenu.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 13-9-21.
 */
public class ListAdapter extends BaseAdapter {


    private LayoutInflater layoutInflater;
    private View.OnClickListener onClickListener;
    private Context mContext;
    private String[] stringArr;
    private String[] phoneArr;
    private Map<String, String> map = new HashMap<String, String>();
    private Map<String, String> map2 = new HashMap<String, String>();
    boolean isShow = false;

    public ListAdapter(Context context, String[] arr, String[] phoneArr, View.OnClickListener listener, Map<String, String> map, Map<String, String> map2) {
        layoutInflater = LayoutInflater.from(context);
        this.onClickListener = listener;
        this.mContext = context;
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
//        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listitem, null);
            View view = layoutInflater.inflate(R.layout.addview, null);
            final LinearLayout tv = (LinearLayout) view.findViewById(R.id.add);


            final LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.linear);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isShow == false) {
                        layout.removeView(tv);
                        layout.addView(tv);
                        isShow = true;

                    } else if (isShow == true) {
                        layout.removeView(tv);
                        isShow = false;
                    }


                }
            });


            holder = new ViewHolder();

            holder.nameTextView = (TextView) convertView.findViewById(R.id.text_website_name);
            holder.phoneTextView = (TextView) convertView.findViewById(R.id.text_website_phone);


            convertView.setTag(holder);
//        }
        /*else {
            holder = (ViewHolder) convertView.getTag();
        }*/
        holder.nameTextView.setText(map.get(stringArr[position]));
        holder.phoneTextView.setText(phoneArr[position]);
        int idx = position - 1;
        char previewChar = idx >= 0 ? stringArr[idx].charAt(0) : ' ';
        char currentChar = stringArr[position].charAt(0);

        return convertView;
    }

    public final class ViewHolder {

        public TextView nameTextView;
        private TextView phoneTextView;

    }
}
