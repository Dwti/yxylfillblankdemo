package com.example.sp.yxylfillblankdemo.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.text.Html;
import android.text.Layout;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sp.yxylfillblankdemo.R;
import com.example.sp.yxylfillblankdemo.StemUtil;
import com.example.sp.yxylfillblankdemo.XForegroundColorSpan;

import java.util.TreeMap;

/**
 * Created by sunpeng on 2017/6/15.
 */

public abstract class ReplacementSpanTextView<T extends View> extends FrameLayout implements XTextView.OnDrawFinishedListener{
    protected XTextView mTextView;
    protected RelativeLayout mOverLayViewContainer;
    protected Context mContext;
    private Spanned mSpannedStr;
    private EmptyReplacementSpan[] mSpans;
    private TreeMap<EmptyReplacementSpan, T> mTreeMap;
    protected boolean mIsReplaceCompleted = false;
    private OnReplaceCompleteListener mOnReplaceCompleteListener;

    public ReplacementSpanTextView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public ReplacementSpanTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ReplacementSpanTextView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ReplacementSpanTextView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initView(Context context) {
        mContext = context;
        mTreeMap = new TreeMap<>();
        View view = LayoutInflater.from(context).inflate(R.layout.replaceable_text_view, this, true);
        mTextView = (XTextView) view.findViewById(R.id.textView);
        mOverLayViewContainer = (RelativeLayout) view.findViewById(R.id.relativeLayout);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,17);
        mTextView.setOnDrawFinishedListener(this);
    }

    public void setText(String text) {
        mIsReplaceCompleted = false;
        mOverLayViewContainer.removeAllViews();
        mTreeMap.clear();
        text = StemUtil.initClozeStem(text);
        mSpannedStr = Html.fromHtml(text, getImageGetter(), getTagHandler());
        mSpans = mSpannedStr.getSpans(0,mSpannedStr.length(),EmptyReplacementSpan.class);
        setSpanWidthAndHeight();
        mTextView.setText(mSpannedStr, TextView.BufferType.SPANNABLE);
    }

    private void setSpanWidthAndHeight() {
        for(EmptyReplacementSpan emptyReplacementSpan : mSpans){
            emptyReplacementSpan.width = 200;
            emptyReplacementSpan.height = mTextView.getLineHeight();
            emptyReplacementSpan.standardLineHeight = mTextView.getLineHeight();
        }
    }

    protected void replaceSpanWithViews(final Spanned spanned) {
        if (spanned == null) {
            return;
        }
        mOverLayViewContainer.removeAllViews();
        mTreeMap.clear();

        for (final EmptyReplacementSpan span : mSpans) {

            int start = spanned.getSpanStart(span);
            Layout layout = mTextView.getLayout();

            span.setSpanStart(start);

            int lineStart = layout.getLineForOffset(start); //span的起始行

                int topPadding = mTextView.getCompoundPaddingTop();
                float startLeftMargin = layout.getPrimaryHorizontal(start); //span的起始位置的左边距

                int descent = layout.getLineDescent(lineStart);
                int base = layout.getLineBaseline(lineStart);
                int spanTop = base + descent - span.height;
                int topMargin = spanTop + topPadding;

                int width = span.width;
                int height = span.height;

                T view = getView();
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
                params.leftMargin = (int) startLeftMargin;
                params.topMargin = topMargin;
                mOverLayViewContainer.addView(view, params);


            mTreeMap.put(span,view);
        }
        mIsReplaceCompleted = true;
        if(mOnReplaceCompleteListener != null){
            mOnReplaceCompleteListener.onReplaceComplete();
        }
    }

    public void setOnReplaceCompleteListener(OnReplaceCompleteListener onReplaceCompleteListener){
        mOnReplaceCompleteListener = onReplaceCompleteListener;
    }

    public TreeMap<EmptyReplacementSpan,T> getClozes(){
        return mTreeMap;
    }

    protected abstract T getView();

    protected Html.ImageGetter getImageGetter() {
        return new HtmlImageGetter(mTextView);
    }

    protected Html.TagHandler getTagHandler(){
        return new ClozeTagHandler();
    }

    @Override
    public void onDrawFinished() {
        replaceSpanWithViews(mSpannedStr);
    }

}
