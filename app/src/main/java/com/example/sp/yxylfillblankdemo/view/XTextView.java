package com.example.sp.yxylfillblankdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by sp on 17-3-2.
 */

public class XTextView extends AppCompatTextView {
    OnDrawFinishedListener onDrawFinishedListener;
    public XTextView(Context context) {
        super(context);
    }

    public XTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(onDrawFinishedListener != null){
            onDrawFinishedListener.onDrawFinished();
        }
        Log.i("MyTextView","onDraw called!");
    }

    public OnDrawFinishedListener getOnDrawFinishedListener() {
        return onDrawFinishedListener;
    }

    public void setOnDrawFinishedListener(OnDrawFinishedListener onDrawFinishedListener) {
        this.onDrawFinishedListener = onDrawFinishedListener;
    }

    public interface OnDrawFinishedListener{
        void onDrawFinished();
    }
}
