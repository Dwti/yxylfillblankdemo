package com.example.sp.yxylfillblankdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ScrollView;

import com.example.sp.yxylfillblankdemo.view.FillBlankTextView;

public class MainActivity extends Activity {

    private FillBlankTextView mFillBlank;

    private String mContent;

    private View mEditLayout, mBottom;

    private ScrollView mScrollView;

    private EditText mEditText;

    private int mBottomHeight;

    private boolean mKeyBoardVisible = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFillBlank = (FillBlankTextView) findViewById(R.id.tv_fill_blank);
        mEditLayout = findViewById(R.id.ll_edit);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mBottom = findViewById(R.id.tv_bottom);
        mEditText = (EditText) findViewById(R.id.editText);

        mContent = FileUtils.fetchFileContent(this, "html.txt");
        mFillBlank.setText(mContent);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            mBottomHeight = mScrollView.getBottom();
            getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
        }
    }

    //监听键盘的弹出收起,并且在键盘弹起时，ScrollView滑动到指定位置
    ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            if(mScrollView.getBottom() < mBottomHeight && !mKeyBoardVisible){
                mBottom.setVisibility(View.GONE);
                mEditLayout.setVisibility(View.VISIBLE);
                mEditText.requestFocus();
                mScrollView.requestLayout();
                mKeyBoardVisible = true;
            }else if(mScrollView.getBottom() >= mBottomHeight && mKeyBoardVisible){
                mBottom.setVisibility(View.VISIBLE);
                mEditLayout.setVisibility(View.GONE);
                mKeyBoardVisible = false;
            }
        }
    };
}
