package com.example.sp.yxylfillblankdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ViewTreeObserver;

import com.example.sp.yxylfillblankdemo.view.ClozeTextView;
import com.example.sp.yxylfillblankdemo.view.ClozeView;
import com.example.sp.yxylfillblankdemo.view.EmptyReplacementSpan;
import com.example.sp.yxylfillblankdemo.view.OnReplaceCompleteListener;

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
        String text = StemUtil.initClozeStem(mContent);
        clozeTextView.setText(text);
    }
}
