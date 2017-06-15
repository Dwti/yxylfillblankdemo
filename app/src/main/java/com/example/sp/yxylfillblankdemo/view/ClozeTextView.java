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
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.example.sp.yxylfillblankdemo.R;

/**
 * Created by sunpeng on 2017/6/15.
 */

public class ClozeTextView extends ReplacementSpanTextView<ClozeView> {

    private OnClozeClickListener mOnClozeClickListener;

    private ClozeView mLastClickClozeView ;

    public ClozeTextView(@NonNull Context context) {
        super(context);
    }

    public ClozeTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ClozeTextView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ClozeTextView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected ClozeView getView() {
        ClozeView clozeView = new ClozeView(getContext());
        clozeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ClozeView cv = (ClozeView) v;
                if(mLastClickClozeView == cv){
                    return;
                }
                if(cv.getTextPosition() == ClozeView.TextPosition.CENTER){
                    cv.performTranslateAnimation(ClozeView.TextPosition.LEFT);
                }
                if(mLastClickClozeView != null){
                    mLastClickClozeView.performTranslateAnimation(ClozeView.TextPosition.CENTER);
                }
                mLastClickClozeView = cv;
                if(mOnClozeClickListener != null){
                    mOnClozeClickListener.onClozeClick(cv,cv.getTextNumber() - 1);
                }
            }
        });
        return clozeView;
    }

    public void setOnClozeClickListener(OnClozeClickListener listener){
        mOnClozeClickListener = listener;
    }

    public interface OnClozeClickListener{
        void onClozeClick(ClozeView view,int position);
    }
}
