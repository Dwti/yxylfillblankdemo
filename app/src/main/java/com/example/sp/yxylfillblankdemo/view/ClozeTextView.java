package com.example.sp.yxylfillblankdemo.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by sunpeng on 2017/6/15.
 */

public class ClozeTextView extends ReplacementSpanTextView<ClozeView> implements OnReplaceCompleteListener {

    private OnClozeClickListener mOnClozeClickListener;

    private ClozeView mSelectedClozeView;

    public ClozeTextView(@NonNull Context context) {
        super(context);
    }

    public ClozeTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClozeTextView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ClozeTextView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        setOnReplaceCompleteListener(this);
    }

    @Override
    protected ClozeView getView() {
        ClozeView clozeView = new ClozeView(getContext());
        clozeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ClozeView cv = (ClozeView) v;
                if(mSelectedClozeView == cv){
                    return;
                }
                if(cv.getTextPosition() == ClozeView.TextPosition.CENTER){
                    cv.performTranslateAnimation(ClozeView.TextPosition.LEFT);
                }
                if(mSelectedClozeView != null){
                    mSelectedClozeView.performTranslateAnimation(ClozeView.TextPosition.CENTER);
                }
                mSelectedClozeView = cv;
                if(mOnClozeClickListener != null){
                    mOnClozeClickListener.onClozeClick(cv,cv.getTextNumber() - 1);
                }
            }
        });
        return clozeView;
    }

    public void resetSelected(){
        if(mSelectedClozeView != null && mSelectedClozeView.getTextPosition() == ClozeView.TextPosition.LEFT){
            mSelectedClozeView.performTranslateAnimation(ClozeView.TextPosition.CENTER);
            mSelectedClozeView = null;
        }
    }

    public void setOnClozeClickListener(OnClozeClickListener listener){
        mOnClozeClickListener = listener;
    }

    @Override
    public void onReplaceComplete() {
        List<ClozeView> views = getReplaceViews();
        if(views == null || views.size() ==0){
            return;
        }
        for(int i =0; i< views.size(); i++){
            views.get(i).setTextNumber(i+1);
        }
        mSelectedClozeView = views.get(0);
        post(new Runnable() {
            @Override
            public void run() {
                mSelectedClozeView.performTranslateAnimation(ClozeView.TextPosition.LEFT);
            }
        });
    }

    public ClozeView getSelectedClozeView(){
        return mSelectedClozeView;
    }

    public interface OnClozeClickListener{
        void onClozeClick(ClozeView view,int position);
    }
}
