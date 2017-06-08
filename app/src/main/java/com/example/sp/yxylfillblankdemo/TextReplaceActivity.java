package com.example.sp.yxylfillblankdemo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Layout;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sp.yxylfillblankdemo.view.EmptySpan;
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
//                replaceSpanWithView(mSpanned);
                replaceSpanWithViewEx(mSpanned);
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

    protected void replaceSpanWithView(Spanned spannedStr) {
        if (spannedStr == null) {
            return;
        }
        for (ForegroundColorSpan emptySpan : mSpans) {

            int start = spannedStr.getSpanStart(emptySpan);
            Layout layout = mTextView.getLayout();

            int lineStart = layout.getLineForOffset(start);

            int topPadding = mTextView.getCompoundPaddingTop();
            int leftMargin = (int) layout.getPrimaryHorizontal(start);

            int descent = layout.getLineDescent(lineStart);
            int base = layout.getLineBaseline(lineStart);
            int spanTop = base + descent - mTextView.getLineHeight();
            int topMargin = spanTop + topPadding;


            View view = mLinkedHashMap.get(emptySpan);
            if (view == null) {
                view = new View(this);
                view.setBackgroundColor(Color.GREEN);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);
                params.leftMargin = leftMargin;
                params.topMargin = topMargin;
                mMaskView.addView(view, params);
                mLinkedHashMap.put(emptySpan, view);
            } else {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
                params.leftMargin = leftMargin;
                params.topMargin = topMargin;
                view.setLayoutParams(params);
            }
        }
    }



    protected void replaceSpanWithViewEx(Spanned spannedStr) {

//        mMaskView.removeAllViews();
//        mHashMap.clear();
        if (spannedStr == null) {
            return;
        }
        for (ForegroundColorSpan emptySpan : mSpans) {

            int start = spannedStr.getSpanStart(emptySpan);
            int end = spannedStr.getSpanEnd(emptySpan);
            Layout layout = mTextView.getLayout();

            int lineStart = layout.getLineForOffset(start);
            int lineEnd = layout.getLineForOffset(end);

            List<View> viewList = mHashMap.get(emptySpan);
            if (viewList == null || viewList.size() == 0) {
                viewList = new ArrayList<>();
                int currLineStart = lineStart;

                do {

                    int topPadding = mTextView.getCompoundPaddingTop();
                    int startLeftMargin = (int) layout.getPrimaryHorizontal(currLineStart == lineStart? start:0);
                    int endLeftMargin = (int) layout.getPrimaryHorizontal(end);


                    int descent = layout.getLineDescent(currLineStart);
                    int base = layout.getLineBaseline(currLineStart);
                    int spanTop = base + descent - mTextView.getLineHeight();
                    int topMargin = spanTop + topPadding;

                    float lineWidth = layout.getLineWidth(currLineStart);
                    int width;

                    if(lineStart == lineEnd){
                        width = endLeftMargin - startLeftMargin;
                    }else {
                        if(currLineStart == lineStart){
                            width = (int) (lineWidth - startLeftMargin);
                        }else if(currLineStart == lineEnd){
                            width = endLeftMargin;
                        }else {
                            width = (int) lineWidth;
                        }
                    }


                    MyEditText view = new MyEditText(this);
//                    view.setBackgroundColor(Color.GREEN);
//                    view.setPadding(0,0,0,0);
//                    view.setBackground(getResources().getDrawable(R.drawable.fill_empty_bg));
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, mTextView.getLineHeight());
                    params.leftMargin = startLeftMargin;
                    params.topMargin = topMargin;
                    mMaskView.addView(view, params);

                    viewList.add(view);

                    currLineStart ++;

                }while (currLineStart != lineEnd + 1);

                mHashMap.put(emptySpan,viewList);

            } else {
                    int currLineStart = lineStart;

                    do {

                        int topPadding = mTextView.getCompoundPaddingTop();
                        int leftMargin = (int) layout.getPrimaryHorizontal(currLineStart == lineStart? start:0);


                        int descent = layout.getLineDescent(currLineStart);
                        int base = layout.getLineBaseline(currLineStart);
                        int spanTop = base + descent - mTextView.getLineHeight();
                        int topMargin = spanTop + topPadding;

                        View view = viewList.get(currLineStart - lineStart);
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        params.leftMargin = leftMargin;
                        params.topMargin = topMargin;
                        view.setLayoutParams(params);

                        currLineStart ++;

                    }while (currLineStart != lineEnd + 1);
            }
        }
    }
}
