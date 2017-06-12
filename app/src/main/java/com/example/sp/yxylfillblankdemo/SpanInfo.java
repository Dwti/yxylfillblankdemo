package com.example.sp.yxylfillblankdemo;

import android.support.annotation.NonNull;
import android.text.style.ForegroundColorSpan;

/**
 * Created by sunpeng on 2017/6/12.
 */

public class SpanInfo implements Comparable<SpanInfo>{
    private ForegroundColorSpan mSpan;
    private int mStart;
    private int mEnd;
    private String mContent;

    public SpanInfo(ForegroundColorSpan span, String content,int start, int end) {
        this.mSpan = span;
        mContent = content;
        this.mStart = start;
        this.mEnd = end;
    }

    public ForegroundColorSpan getSpan(){
        return mSpan;
    }

    public void setSpan(ForegroundColorSpan span){
        mSpan = span;
    }

    public int getStart(){
        return mStart;
    }

    public void setStart(int start){
        mStart = start;
    }

    public int getEnd(){
        return mEnd;
    }

    public void setEnd(int end){
        mEnd = end;
    }

    public String getContent(){
        return mContent;
    }

    public void setContent(String content){
        mContent = content;
    }

    @Override
    public int compareTo(@NonNull SpanInfo o) {
        return mStart - o.mStart;
    }
}
