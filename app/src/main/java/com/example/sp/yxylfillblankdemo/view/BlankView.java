package com.example.sp.yxylfillblankdemo.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.example.sp.yxylfillblankdemo.R;

/**
 * Created by sp on 17-6-8.
 */

public class BlankView extends LinearLayout {
    private View mTopView;
    public BlankView(Context context) {
        super(context);
        init(context);
    }

    public BlankView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BlankView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BlankView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.shadow_edit_text,this,true);
        mTopView = view.findViewById(R.id.viewTop);
    }

    public void setTransparent(boolean transparent){
        mTopView.setSelected(!transparent);
    }
}
