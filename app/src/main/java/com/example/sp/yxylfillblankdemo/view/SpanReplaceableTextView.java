package com.example.sp.yxylfillblankdemo.view;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Layout;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.sp.yxylfillblankdemo.R;
import com.example.sp.yxylfillblankdemo.SpanInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by sp on 17-2-23.
 */

public class SpanReplaceableTextView extends FrameLayout {
    protected XTextView mTextView;
    protected RelativeLayout mOverLayViewContainer;
    protected Context mContext;
    private Spanned mSpannedStr;
    private ForegroundColorSpan[] mSpans;
    private TreeMap<SpanInfo, List<BlankView>> mHashMap;
    protected boolean mIsReplaceCompleted = false;
    private OnBlankClickListener mOnBlankClickListener;
    private int mClickSpanStart = NONE;     //为了记录点击的是哪一个span，这样的话，弹起或者收起键盘重绘的时候，就能根据这个span的起始位置，去设置覆盖span的view的选中或者非选中状态
    public static final int NONE = -1; //点击的位置为空，既没有点击的span(比如键盘收起的时候，需要清除所有的span的选中状态)

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
        mHashMap = new TreeMap<>();
        View view = LayoutInflater.from(context).inflate(R.layout.replaceable_text_view, this, true);
        mTextView = (XTextView) view.findViewById(R.id.textView);
        mOverLayViewContainer = (RelativeLayout) view.findViewById(R.id.relativeLayout);
        mTextView.setOnDrawFinishedListener(new TextViewOnDrawFinishedListener());
    }

    public void setText(String text) {
        mSpannedStr = Html.fromHtml(text, getImageGetter(), getTagHandler());
        mTextView.setText(mSpannedStr, TextView.BufferType.SPANNABLE);
        mSpans = mSpannedStr.getSpans(0,mSpannedStr.length(),ForegroundColorSpan.class);
    }

    public int getClickSpanStart(){
        return mClickSpanStart;
    }

    public void setClickSpanStart(int start){
        mClickSpanStart = start;
    }

    public TreeMap<SpanInfo,List<BlankView>> getBlanks(){
        return mHashMap;
    }

    public int getCurrentEditBlankPosition(){
        boolean hasFind = false;
        int index = 0;
        for(SpanInfo s : mHashMap.keySet()){
            if(s.getStart() == mClickSpanStart){
                hasFind = true;
                break;
            }
            index++;
        }
        return hasFind?index:-1;
    }

    public List<String> getFilledContent(){
        List list = new ArrayList();
        for(SpanInfo info: mHashMap.keySet()){
            list.add(info.getContent());
        }
        return list;
    }
    protected void replaceSpanWithViews(final Spanned spanned) {
        if (spanned == null) {
            return;
        }
        mOverLayViewContainer.removeAllViews();
        mHashMap.clear();
        for (final ForegroundColorSpan span : mSpans) {

            final int start = spanned.getSpanStart(span);
            final int end = spanned.getSpanEnd(span);

            final String content;
            if(span.getForegroundColor() == Color.TRANSPARENT){
                content = "";
            }else {
                content = spanned.subSequence(start,end).toString();
            }
            SpanInfo spanInfo = new SpanInfo(span,content,start,end);

            Layout layout = mTextView.getLayout();

            int lineStart = layout.getLineForOffset(start); //span的起始行
            int lineEnd = layout.getLineForOffset(end);     //span的结束行


            List<BlankView> viewList = new ArrayList<>();
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

                    final BlankView view = getView();

                    if(start == mClickSpanStart){
                        view.setTransparent(false);
                    }

                    view.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(mOnBlankClickListener != null){
                                mOnBlankClickListener.onBlankClick(view,content,start);
                            }
                        }
                    });

                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, mTextView.getLineHeight());
                    params.leftMargin = (int) startLeftMargin;
                    params.topMargin = topMargin;
                    mOverLayViewContainer.addView(view, params);

                    viewList.add(view);

                    currLine ++;

                }while (currLine <= lineEnd);

                mHashMap.put(spanInfo,viewList);
        }
        mIsReplaceCompleted = true;
//        int count = 0;
//        Set<ForegroundColorSpan> set  = mHashMap.keySet();
//        Iterator<ForegroundColorSpan> iterator = set.iterator();
//        while (iterator.hasNext()){
//            List<View> views = mHashMap.get(iterator.next());
//            count += views.size();
//        }
//        Log.e("count",count+"");
    }

    public void setBlankTransparent(int spanStart, boolean transparent){
        for(SpanInfo e : mHashMap.keySet()){
            if(spanStart == e.getStart()){
                List<BlankView> views = mHashMap.get(e);
                for(BlankView v : views){
                    v.setTransparent(transparent);
                }
                break;
            }
        }
    }

    public void setOnBlankClickListener(OnBlankClickListener listener){
        mOnBlankClickListener = listener;
    }
    public boolean isReplaceCompleted() {
        return mIsReplaceCompleted;
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

    protected BlankView getView(){
        return new BlankView(mContext);
    }

    protected Html.TagHandler getTagHandler(){
        return new EmptyTagHandler();
    }



    public interface OnBlankClickListener {
        void onBlankClick(BlankView view,String filledContent,int spanStart);
    }

    private class TextViewOnDrawFinishedListener implements XTextView.OnDrawFinishedListener {

        @Override
        public void onDrawFinished() {
            replaceSpanWithViews(mSpannedStr);
        }
    }
}
