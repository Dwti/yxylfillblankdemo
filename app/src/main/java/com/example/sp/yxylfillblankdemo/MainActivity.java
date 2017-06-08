package com.example.sp.yxylfillblankdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import com.example.sp.yxylfillblankdemo.view.FillBlankTextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements KeyboardObserver.KeyBoardVisibleChangeListener {

    private FillBlankTextView mFillBlank;

    private String mContent;

    private Button mSend;

    private View mRootView, mEditLayout, mBottom;

    private ScrollView mScrollView;

    private EditText mEditText;

    private EditText mCurrentClickEditText;

    private KeyboardObserver mKeyboardObserver;

    private List<String> mAnswers = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRootView = findViewById(R.id.rootLayout);
        mFillBlank = (FillBlankTextView) findViewById(R.id.tv_fill_blank);
        mEditLayout = findViewById(R.id.ll_edit);
        mSend = (Button) findViewById(R.id.btnSend);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mBottom = findViewById(R.id.tv_bottom);
        mEditText = (EditText) findViewById(R.id.editText);

        initData();

        mContent = FileUtils.fetchFileContent(this, "html.txt");
        mFillBlank.setText(mContent,mAnswers);

        mKeyboardObserver = new KeyboardObserver(mRootView);
        mKeyboardObserver.setKeyBoardVisibleChangeListener(this);

        initListener();


    }

    private void initListener() {
        mFillBlank.setOnEditTextClickListener(new FillBlankTextView.OnEditTextClickListener() {
            @Override
            public void onEditTextClick(EditText editText) {
                mCurrentClickEditText = editText;
            }
        });

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<EditText> editTexts = mFillBlank.getReplacement();
                int index = editTexts.indexOf(mCurrentClickEditText);
                Log.i("index",index+"");
                String content = mEditText.getText().toString();
//                int spanWidth = mFillBlank.getSpanWidth(index);
                int spanWidth = mFillBlank.getSpanWidth(0);
                float contentWidth = Util.computeStringWidth(content,mFillBlank.getPaint());
                StringBuilder sb = new StringBuilder(mContent);
                if(contentWidth > spanWidth){
                    int index1 = sb.indexOf("<blank>"+index+"</blank>");
                    int len = String.valueOf("<blank>"+index+"</blank>").length();
                    //TODO 此处index+1有问题,后面的也是index=1；
                    float diff = contentWidth - spanWidth;
                    int count = (int) Math.ceil(diff / spanWidth);
                    String append = "&nbsp<blank>"+(-1)+"</blank>";
                    for(int i = 0;i < count; i++){
                        append += append;
                    }
                    mContent = sb.insert(index1+len,append).toString();
                    mFillBlank.setText(mContent);
                }
            }
        });

        mBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,TextReplaceActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onKeyboardVisibleChange(boolean isShow, int keyboardHeight) {
        Log.i("state", "onKeyboardVisibleChange() called with: " + "isShow = [" + isShow + "], keyboardHeight = [" + keyboardHeight + "]");
        if(isShow){
            mBottom.setVisibility(View.GONE);
            mEditLayout.setVisibility(View.VISIBLE);
        }else {
            mBottom.setVisibility(View.VISIBLE);
            mEditLayout.setVisibility(View.GONE);
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
