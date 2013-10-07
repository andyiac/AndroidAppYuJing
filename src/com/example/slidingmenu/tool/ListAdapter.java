package com.example.slidingmenu.tool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.slidingmenu.R;
import com.example.slidingmenu.fragment.RightFragment;

import java.util.HashMap;
import java.util.Map;

import static com.example.slidingmenu.R.id.message;

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


        ImageView call = (ImageView) view.findViewById(R.id.phone);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phonenumber = "10001";
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel://" +phonenumber));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);

            }
        });




        ImageView send = (ImageView) view.findViewById(message);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("smsto:10001");
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                it.putExtra("sms_body", "102");
                mContext.startActivity(it);

            }
        });



        convertView.setTag(holder);

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
