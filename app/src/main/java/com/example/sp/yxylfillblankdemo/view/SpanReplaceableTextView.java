package com.example.sp.yxylfillblankdemo.view;

import android.content.Context;
import android.text.Html;
import android.text.Layout;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.sp.yxylfillblankdemo.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by sp on 17-2-23.
 */

public class SpanReplaceableTextView extends FrameLayout {
    protected MyTextView mTextView;
    protected RelativeLayout mMaskView;
    protected Context mContext;
    protected Spanned mSpannedStr;
    protected ForegroundColorSpan[] mSpans;
    protected LinkedHashMap<ForegroundColorSpan, List<View>> mLinkedHashMap;
    protected boolean mIsReplaceCompleted = false;

    public SpanReplaceableTextView(Context context) {
        super(context);
        initView(context);
    }

    public SpanReplaceableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SpanReplaceableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        mLinkedHashMap = new LinkedHashMap<>();
        View view = LayoutInflater.from(context).inflate(R.layout.replaceable_text_view, this, true);
        mTextView = (MyTextView) view.findViewById(R.id.textView);
        mMaskView = (RelativeLayout) view.findViewById(R.id.relativeLayout);
        mTextView.setOnDrawFinishedListener(new TextViewOnDrawFinishedListener());
    }

    public void setText(String text) {
        setText(text, null);
    }

    public void setText(String text, List<String> textToFill) {
        mSpannedStr = Html.fromHtml(text, getImageGetter(), getTagHandler());
        mTextView.setText(mSpannedStr, TextView.BufferType.SPANNABLE);
        mSpans = mSpannedStr.getSpans(0,mSpannedStr.length(),ForegroundColorSpan.class);
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


            List<View> viewList = mLinkedHashMap.get(emptySpan);
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

                    View view = getView();
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, mTextView.getLineHeight());
                    params.leftMargin = (int) startLeftMargin;
                    params.topMargin = topMargin;
                    mMaskView.addView(view, params);

                    viewList.add(view);

                    currLine ++;

                }while (currLine <= lineEnd);

                mLinkedHashMap.put(emptySpan,viewList);

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

                    boolean isNew = false;
                    View view = null;
                    RelativeLayout.LayoutParams params;
                    //TODO 计算有问题
                    //因为布局的问题，可能会出现同一个span的lineEnd不一致的问题，如果不一致，某些情况下会导致currLine - lineStart = viewList.size()
                    if(viewList != null && currLine - lineStart < viewList.size()){
                        view = viewList.get(currLine - lineStart);
                    }
                    if(view == null){
                        view = getView();
                        isNew = true;
                        params = new RelativeLayout.LayoutParams(width, mTextView.getLineHeight());
                        break;
                    }else {
                        params = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        params.width = width;
                        params.height = mTextView.getLineHeight();
                    }

                    params.leftMargin = (int) startLeftMargin;
                    params.topMargin = topMargin;
                    view.setLayoutParams(params);

                    //这种情况说明上面走的是view为null的情况，需要重新添加进去
                    if(isNew){
                        viewList.add(view);
                        mMaskView.addView(view);
                    }
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

    public boolean isReplaceCompleted() {
        return mIsReplaceCompleted;
    }

    public void removeAllReplacementView() {
        if (mMaskView.getChildCount() > 0) {
            mMaskView.removeAllViews();
            mLinkedHashMap.clear();
        }
    }

    public float getTextSize() {
        return mTextView.getTextSize();
    }

    public void setTextSize(float size) {
        mTextView.setTextSize(size);
    }

    public void setTextSize(int unit, float size) {
        mTextView.setTextSize(unit, size);
    }

    public void setTextColor(int color) {
        mTextView.setTextColor(color);
    }

    protected Html.ImageGetter getImageGetter() {
        return new HtmlImageGetter(mContext, mTextView);
    }

    protected View getView(){
        return new MyEditText(mContext);
    }

    protected Html.TagHandler getTagHandler(){
        return new EmptyTagHandler();
    }

    private class TextViewOnDrawFinishedListener implements MyTextView.OnDrawFinishedListener {

        @Override
        public void onDrawFinished() {
            replaceSpanWithViews(mSpannedStr);
        }
    }
}
