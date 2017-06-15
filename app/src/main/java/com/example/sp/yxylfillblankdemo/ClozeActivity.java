package com.example.sp.yxylfillblankdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Layout;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sp.yxylfillblankdemo.view.ClozeTextView;
import com.example.sp.yxylfillblankdemo.view.ClozeView;
import com.example.sp.yxylfillblankdemo.view.EmptyReplacementSpan;
import com.example.sp.yxylfillblankdemo.view.EmptyTagHandler;
import com.example.sp.yxylfillblankdemo.view.HtmlImageGetter;
import com.example.sp.yxylfillblankdemo.view.BlankView;
import com.example.sp.yxylfillblankdemo.view.OnReplaceCompleteListener;
import com.example.sp.yxylfillblankdemo.view.XTextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by sp on 17-6-8.
 */

public class ClozeActivity extends Activity {

    private ClozeTextView clozeTextView;
    private String mContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_replace);
        clozeTextView = (ClozeTextView) findViewById(R.id.cloze_text_view);
        mContent = FileUtils.fetchFileContent(this, "html.txt");
        clozeTextView.setOnReplaceCompleteListener(new OnReplaceCompleteListener() {
            @Override
            public void onReplaceComplete() {
                TreeMap<EmptyReplacementSpan,ClozeView> treeMap = clozeTextView.getClozes();
                int i = 1;
                for(Map.Entry<EmptyReplacementSpan,ClozeView> entry:treeMap.entrySet()){
                    entry.getValue().setTextNumber(i);
                    i++;
                }
            }
        });
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            clozeTextView.setText(mContent);
        }
    }
}
