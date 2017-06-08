package com.example.sp.yxylfillblankdemo.view;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Layout;
import android.text.Spanned;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.sp.yxylfillblankdemo.R;
import com.example.sp.yxylfillblankdemo.Util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by sp on 17-2-23.
 */

public abstract class SpanReplaceableTextView<T extends View> extends FrameLayout {
    protected MyTextView mTextView;
    protected RelativeLayout mRelativeLayout;
    protected Context mContext;
    protected Spanned mSpannedStr;
    protected EmptySpan[] mEmptySpans;
    private LinkedHashMap<EmptySpan, T> mLinkedHashMap;
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
        mTextView.setTextSize(15);
        mTextView.setTextColor(Color.BLACK);
        mRelativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);
        mTextView.setOnDrawFinishedListener(new TextViewOnDrawFinishedListener());
    }

    public void setText(String text) {
        setText(text, null);
    }

    public void setText(final String text, final List<String> textToFill) {
        post(new Runnable() {
            @Override
            public void run() {
                initText(text, textToFill);
            }
        });
    }

    public void initText(String text, List<String> textToFill) {
        mSpannedStr = Html.fromHtml(text, getImageGetter(), getTagHandler());
        initSpanWidthAndHeight(mSpannedStr, textToFill);
        mTextView.setLineSpacing(Util.convertDpToPx(getContext(), 2), 1);
        mTextView.setText(mSpannedStr, TextView.BufferType.SPANNABLE);
    }

    protected void initSpanWidthAndHeight(Spanned span, List<String> textToFill) {
        mEmptySpans = span.getSpans(0, mSpannedStr.length(), getTagHandler().getSpanType());
        int width = getSpanWidth(textToFill);
        for (int i = 0; i < mEmptySpans.length; i++) {
            mEmptySpans[i].standardLineHeight = mTextView.getLineHeight();
            mEmptySpans[i].textWidth = width;
        }
    }

    private int getSpanWidth(List<String> listString) {
        if (true)
            return Util.convertDpToPx(getContext(), 60);
        int width = 0;
        int minTextWidth = getMinSpanWidth();
        if (listString == null || listString.isEmpty())
            return minTextWidth;
        for (String str : listString) {
            int textWidth = (int) Util.computeStringWidth(str, mTextView.getPaint());
            width = Math.max(textWidth, width);
        }
        width = Math.max(width, minTextWidth);
        if (width > minTextWidth)
            width += (int) Util.computeStringWidth(getContext().getString(R.string.single_Chinese_character), mTextView.getPaint());
        if (mTextView.getWidth() != 0)
            width = Math.min(mTextView.getWidth(), width);
        return width;
    }

    protected void replaceSpanWithView(Spanned spannedStr) {
        if (spannedStr == null) {
            return;
        }
        for (EmptySpan emptySpan : mEmptySpans) {

            int start = spannedStr.getSpanStart(emptySpan);
            Layout layout = mTextView.getLayout();
            int line = layout.getLineForOffset(start);
            int topPadding = mTextView.getCompoundPaddingTop();
            int leftMargin = (int) layout.getPrimaryHorizontal(start);
            int descent = layout.getLineDescent(line);
            int base = layout.getLineBaseline(line);
            int spanTop = base + descent - emptySpan.height();
            int topMargin = spanTop + topPadding;

            T view = mLinkedHashMap.get(emptySpan);
            if (view == null) {
                view = getView();
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(emptySpan.width(), emptySpan.height());
                params.leftMargin = leftMargin;
                params.topMargin = topMargin;
                mRelativeLayout.addView(view, params);
                mLinkedHashMap.put(emptySpan, view);
            } else {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
                params.leftMargin = leftMargin;
                params.topMargin = topMargin;
                view.setLayoutParams(params);
            }
        }
        mIsReplaceCompleted = true;
    }

    public List<T> getReplacement() {
        List<T> list = new ArrayList<>();
        Set<Map.Entry<EmptySpan, T>> entries = mLinkedHashMap.entrySet();
        Iterator<Map.Entry<EmptySpan, T>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<EmptySpan, T> entry = iterator.next();
            list.add(entry.getValue());
        }
        return list;
    }

    public boolean isReplaceCompleted() {
        return mIsReplaceCompleted;
    }

    public void removeAllReplacementView() {
        if (mRelativeLayout.getChildCount() > 0) {
            mRelativeLayout.removeAllViews();
            mLinkedHashMap.clear();
        }
    }

    public TextPaint getPaint(){
        return mTextView.getPaint();
    }

    public int getSpanWidth(int index){
        return mEmptySpans[index].textWidth;
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

    protected int getMinSpanWidth() {
        return (int) Util.computeStringWidth(getContext().getString(R.string.four_Chinese_characters), mTextView.getPaint());
    }

    protected Html.ImageGetter getImageGetter() {
        return new HtmlImageGetter(mContext, mTextView);
    }

    protected abstract T getView();

    protected abstract ReplaceTagHandler getTagHandler();

    private class TextViewOnDrawFinishedListener implements MyTextView.OnDrawFinishedListener {

        @Override
        public void onDrawFinished() {
            replaceSpanWithView(mSpannedStr);
        }
    }
}
