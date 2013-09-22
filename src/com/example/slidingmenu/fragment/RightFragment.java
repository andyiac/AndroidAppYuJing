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
package com.example.slidingmenu.fragment;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.widget.*;
import android.widget.ListAdapter;
import com.example.slidingmenu.R;
import com.example.slidingmenu.tool.*;
import com.example.slidingmenu.view.RightCharacterListView;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.*;

public class RightFragment extends Fragment implements ListView.OnScrollListener,
        AdapterView.OnItemClickListener, android.view.View.OnClickListener {


    private RightCharacterListView letterListView;
    private Handler handler;
    private DisapearThread disapearThread;
    private int scrollState;
    private ListAdapter listAdapter;
    private ListView listMain;
    private TextView txtOverlay;
    private TextView add;

    private View view;

    private LinearLayout layout;

    private WindowManager windowManager;
    private String[] stringArr = {"阿武", "阿布", "阿武", "阿布","贝贝", "贝贝","成成","成成", "王辉","王辉",
            "张明", "张伟宁", "张川", "张宁", "张萍","张明", "张伟宁", "张川", "张宁", "张萍"};

    private String[] telephone = {"15369317720", "15369317750", "15869331457", "15230389135", "15369347750", "15369317720", "15369317750", "15869331457", "15230389135", "15369347750","15369317720", "15369317750", "15869331457", "15230389135", "15369347750", "15369317720", "15369317750", "15869331457", "15230389135", "15369347750"};

    private String[] stringArr3 = new String[0];
    private ArrayList arrayList = new ArrayList();
    private ArrayList arrayList2 = new ArrayList();
    private ArrayList arrayList3 = new ArrayList();
    private Map<String, String> map = new HashMap<String, String>();
    private Map<String, String> map2 = new HashMap<String, String>();


    public String converterToPinYin(String chinese) {
        String pinyinString = "";
        char[] charArray = chinese.toCharArray();
        // 根据需要定制输出格式，我用默认的即可
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        try {
            // 遍历数组，ASC码大于128进行转换
            for (int i = 0; i < charArray.length; i++) {
                if (charArray[i] > 128) {
                    // charAt(0)取出首字母
                    PinyinHelper PinyinHelper;
                    pinyinString += net.sourceforge.pinyin4j.PinyinHelper.toHanyuPinyinStringArray(
                            charArray[i], defaultFormat)[0].charAt(0);
                } else {
                    pinyinString += charArray[i];
                }
            }
            return pinyinString;
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onClick(View view) {


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        String personalName = map.get(stringArr[position]);

    }


    public class LetterListViewListener implements
            RightCharacterListView.OnTouchingLetterChangedListener {

        @Override
        public void onTouchingLetterChanged(final String s) {

            Toast.makeText(getActivity().getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            int num = 0;
            for (int i = 0; i < stringArr.length; i++) {
                if ("a".equals(s)) {
                    num = 0;
                } else if (character2ASCII(stringArr[i].substring(0, 1)) < (character2ASCII(s) + 32)) {
                    num += 1;
                }

            }

            listMain.setSelectionFromTop(num, 0);
            // .setSelection(num);

        }

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.right, null);
        listMain = (ListView) view.findViewById(R.id.listView);
        letterListView = (RightCharacterListView) view.findViewById(R.id.rightCharacterListView);

        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        for (int i = 0; i < stringArr.length; i++) {
            String pinyin = converterToPinYin(stringArr[i]);
            arrayList.add(pinyin);
            if (!arrayList2.contains(pinyin.substring(0, 1))) {
                arrayList2.add(pinyin.substring(0, 1));
            }
            map.put(pinyin, stringArr[i]);
            map2.put(stringArr[i], telephone[i]);
            Log.d("log22", stringArr[i]);
        }
        stringArr = (String[]) arrayList.toArray(stringArr);

        arrayList3.add("#");
        for (int i = 0; i < arrayList2.size(); i++) {
            String string = (String) arrayList2.get(i);
            arrayList3.add(string.toUpperCase());
        }

        stringArr3 = (String[]) arrayList3.toArray(stringArr3);// 得到右侧英文字母列表


        String[] b = {"#", "A", "B", "C", "D", "E", "F", "G", "H", "S", "W",
                "X", "Z"};
        letterListView.setB(stringArr3);

        letterListView
                .setOnTouchingLetterChangedListener(new LetterListViewListener());

        handler = new Handler();



        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
       /* windowManager = (WindowManager)getActivity().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(txtOverlay, lp);*/
        // 初始化ListAdapter
        listAdapter = new com.example.slidingmenu.tool.ListAdapter(getActivity().getApplicationContext(), stringArr, telephone, this, map, map2);

        listMain.setOnItemClickListener(this);
        listMain.setOnScrollListener(this);
        listMain.setAdapter(listAdapter);
        disapearThread = new DisapearThread();
    }


    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        this.scrollState = scrollState;
        if (scrollState == ListView.OnScrollListener.SCROLL_STATE_IDLE) {
            handler.removeCallbacks(disapearThread);
            // 提示延迟1.5s再消失
            boolean bool = handler.postDelayed(disapearThread, 1500);
        } else {
//            txtOverlay.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
//        txtOverlay
//                .setText(String.valueOf(stringArr[firstVisibleItem].charAt(0)));

    }

    private class DisapearThread implements Runnable {
        public void run() {
            // 避免在1.5s内，用户再次拖动时提示框又执行隐藏命令。
            if (scrollState == ListView.OnScrollListener.SCROLL_STATE_IDLE) {
//                txtOverlay.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 把单个英文字母或者字符串转换成数字ASCII码
     *
     * @param input
     * @return
     */
    public static int character2ASCII(String input) {
        char[] temp = input.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (char each : temp) {
            builder.append((int) each);
        }
        String result = builder.toString();
        return Integer.parseInt(result);
    }

}