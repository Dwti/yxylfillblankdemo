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

public class MyEditText extends LinearLayout {
    InputMethodManager imm ;
    public MyEditText(Context context) {
        super(context);
        init(context);
    }

    public MyEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.shadow_edit_text,this,true);
        View topView = view.findViewById(R.id.viewTop);

        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        topView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);
            }
        });
    }
}
