package com.example.sp.yxylfillblankdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import com.example.sp.yxylfillblankdemo.view.BlankView;
import com.example.sp.yxylfillblankdemo.view.SpanReplaceableTextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements KeyboardObserver.KeyBoardVisibleChangeListener {

    private SpanReplaceableTextView mFillBlank;

    private String mContent;

    private Button mSend;

    private View mRootView, mEditLayout, mBottom;

    private ScrollView mScrollView;

    private EditText mEditText;

    private KeyboardObserver mKeyboardObserver;

    private boolean mIsKeyboardShowing = false;

    private List<String> mAnswers = new ArrayList<>();

    InputMethodManager imm ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRootView = findViewById(R.id.rootLayout);
        mFillBlank = (SpanReplaceableTextView) findViewById(R.id.tv_fill_blank);
        mEditLayout = findViewById(R.id.ll_edit);
        mSend = (Button) findViewById(R.id.btnSend);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mBottom = findViewById(R.id.tv_bottom);
        mEditText = (EditText) findViewById(R.id.editText);
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        initData();

        mContent = FileUtils.fetchFileContent(this, "html.txt");

        mKeyboardObserver = new KeyboardObserver(mRootView);
        mKeyboardObserver.setKeyBoardVisibleChangeListener(this);

        initListener();


    }

    private void initListener() {

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,TextReplaceActivity.class);
                startActivity(intent);
            }
        });

        mFillBlank.setOnBlankClickListener(new SpanReplaceableTextView.OnBlankClickListener() {
            @Override
            public void onBlankClick(BlankView view, String filledContent, int spanStart) {
                //当键盘为弹起状态时，也就是mFillBlank.getClickSpanStart()!=-1时，需要清除掉 上一个点击的空的选中状态
                if(mIsKeyboardShowing && spanStart != mFillBlank.getClickSpanStart()){
                    mFillBlank.setBlankTransparent(mFillBlank.getClickSpanStart(),true);
                    mFillBlank.setBlankTransparent(spanStart,false);
                }
                mFillBlank.setClickSpanStart(spanStart);
                if(!mIsKeyboardShowing){
                    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);
                }
                mEditText.setText(filledContent);
                mEditText.setSelection(filledContent.length());
            }
        });
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            mFillBlank.setText(mContent,mAnswers);
        }
    }

    @Override
    public void onKeyboardVisibleChange(boolean isShow, int keyboardHeight) {
        Log.i("state", "onKeyboardVisibleChange() called with: " + "isShow = [" + isShow + "], keyboardHeight = [" + keyboardHeight + "]");
        mIsKeyboardShowing = isShow;
        if(isShow){
            mBottom.setVisibility(View.GONE);
            mEditLayout.setVisibility(View.VISIBLE);
        }else {
            mBottom.setVisibility(View.VISIBLE);
            mEditLayout.setVisibility(View.GONE);
            mFillBlank.setClickSpanStart(SpanReplaceableTextView.NONE);
        }
    }

    @Override
    protected void onDestroy() {
        mKeyboardObserver.destroy();
        super.onDestroy();
    }

    private void initData(){
        mAnswers.add("门前大桥下");
        mAnswers.add("游过一群鸭");
        mAnswers.add("快来快来");
        mAnswers.add("数一数");
        mAnswers.add("二四六七八");
        mAnswers.add("门前老爷爷胡子白花花");
    }
}
