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

        mContent = FileUtils.fetchFileContent(this, "html1.txt");

        mKeyboardObserver = new KeyboardObserver(mRootView);
        mKeyboardObserver.setKeyBoardVisibleChangeListener(this);

        initListener();


    }

    private void initListener() {

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //找出当前输入的span的位置
                int currPos = mFillBlank.getCurrentEditBlankPosition();
                //更新答案的内容
                mAnswers.set(currPos,mEditText.getText().toString());
                //重新初始化题干
                String stem = StemUtil.init(mContent,mAnswers);
                //重绘
                mFillBlank.setText(stem);

                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);
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
            String stem = StemUtil.init(mContent,mAnswers);
            mFillBlank.setText(stem);
        }
    }

    @Override
    public void onKeyboardVisibleChange(boolean isShow, int keyboardHeight) {
        Log.i("state", "onKeyboardVisibleChange() called with: " + "isShow = [" + isShow + "], keyboardHeight = [" + keyboardHeight + "]");
        mIsKeyboardShowing = isShow;
        if(isShow){
            mBottom.setVisibility(View.GONE);
            mEditLayout.setVisibility(View.VISIBLE);
            mEditText.requestFocus();
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
        mAnswers.add("blank party no matter how many character in here");
        mAnswers.add("");
        mAnswers.add("you will see");
        mAnswers.add("blank party no matter how many charactor in here");
        mAnswers.add("look into my eyes");
        mAnswers.add("");
    }
}
