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

import com.example.sp.yxylfillblankdemo.view.EmptyTagHandler;
import com.example.sp.yxylfillblankdemo.view.HtmlImageGetter;
import com.example.sp.yxylfillblankdemo.view.MyEditText;
import com.example.sp.yxylfillblankdemo.view.MyTextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by sp on 17-6-8.
 */

public class TextReplaceActivity extends Activity {

    private MyTextView mTextView;

    private RelativeLayout mMaskView;

    private LinkedHashMap<ForegroundColorSpan, View> mLinkedHashMap = new LinkedHashMap<>();

    private LinkedHashMap<ForegroundColorSpan,List<View>> mHashMap = new LinkedHashMap<>();

    private ForegroundColorSpan[] mSpans;

    String mContent;

    Spanned mSpanned;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_replace);
        mTextView = (MyTextView) findViewById(R.id.textView);
        mMaskView = (RelativeLayout) findViewById(R.id.relativeLayout);

        mContent = FileUtils.fetchFileContent(this, "html.txt");

        mTextView.setOnDrawFinishedListener(new MyTextView.OnDrawFinishedListener() {
            @Override
            public void onDrawFinished() {
                replaceSpanWithViews(mSpanned);
            }
        });

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            HtmlImageGetter imageGetter = new HtmlImageGetter(mTextView.getContext(), mTextView);
            mSpanned = Html.fromHtml(mContent, imageGetter, new EmptyTagHandler());
            mTextView.setText(mSpanned, TextView.BufferType.SPANNABLE);
            mSpans = mSpanned.getSpans(0,mSpanned.length(),ForegroundColorSpan.class);
        }
    }

    protected void replaceSpanWithViews(Spanned spannedStr) {

        if (spannedStr == null) {
            return;
        }
        for (ForegroundColorSpan emptySpan : mSpans) {

            int start = spannedStr.getSpanStart(emptySpan);
            int end = spannedStr.getSpanEnd(emptySpan);

            Log.e("span",spannedStr.subSequence(start,end).toString());
            Layout layout = mTextView.getLayout();

            int lineStart = layout.getLineForOffset(start); //span的起始行
            int lineEnd = layout.getLineForOffset(end);     //span的结束行


            List<View> viewList = mHashMap.get(emptySpan);
            if (viewList == null || viewList.size() == 0) {
                viewList = new ArrayList<>();
                int currLine = lineStart;

                do {

                    int topPadding = mTextView.getCompoundPaddingTop();
                    float startLeftMargin = layout.getPrimaryHorizontal(currLine == lineStart? start:0); //span的起始位置的左边距
                    float endLeftMargin = layout.getPrimaryHorizontal(end);           //span结束位置的左边距


                    int descent = layout.getLineDescent(currLine);
                    int base = layout.getLineBaseline(currLine);
                    int spanTop = base + descent - mTextView.getLineHeight();
                    int topMargin = spanTop + topPadding;

                    float lineWidth = layout.getLineWidth(currLine);
                    int width;

                    if(lineStart == lineEnd){
                        width = (int) (endLeftMargin - startLeftMargin);
                    }else {
                        if(currLine == lineStart){
                            width = (int) (lineWidth - startLeftMargin);
                        }else if(currLine == lineEnd){
                            width = (int) endLeftMargin;
                        }else {
                            width = (int) lineWidth;
                        }
                    }

                    MyEditText view = new MyEditText(this);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, mTextView.getLineHeight());
                    params.leftMargin = (int) startLeftMargin;
                    params.topMargin = topMargin;
                    mMaskView.addView(view, params);

                    viewList.add(view);

                    currLine ++;

                }while (currLine <= lineEnd);

                mHashMap.put(emptySpan,viewList);

            } else {
                    int currLine = lineStart;

                    do {

                        int topPadding = mTextView.getCompoundPaddingTop();
                        float startLeftMargin = layout.getPrimaryHorizontal(currLine == lineStart? start:0);
                        float endLeftMargin = layout.getPrimaryHorizontal(end);


                        int descent = layout.getLineDescent(currLine);
                        int base = layout.getLineBaseline(currLine);
                        int spanTop = base + descent - mTextView.getLineHeight();
                        int topMargin = spanTop + topPadding;

                        float lineWidth = layout.getLineWidth(currLine);
                        int width;

                        if(lineStart == lineEnd){
                            width = (int) (endLeftMargin - startLeftMargin);
                        }else {
                            if(currLine == lineStart){
                                width = (int) (lineWidth - startLeftMargin);
                            }else if(currLine == lineEnd){
                                width = (int) endLeftMargin;
                            }else {
                                width = (int) lineWidth;
                            }
                        }

                        View view = viewList.get(currLine - lineStart);
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        params.width = width;
                        params.leftMargin = (int) startLeftMargin;
                        params.topMargin = topMargin;
                        view.setLayoutParams(params);

                        currLine ++;

                    }while (currLine <= lineEnd);
            }
        }
//        int count = 0;
//        Set<ForegroundColorSpan> set  = mHashMap.keySet();
//        Iterator<ForegroundColorSpan> iterator = set.iterator();
//        while (iterator.hasNext()){
//            List<View> views = mHashMap.get(iterator.next());
//            count += views.size();
//        }
//        Log.e("count",count+"");
    }
}
