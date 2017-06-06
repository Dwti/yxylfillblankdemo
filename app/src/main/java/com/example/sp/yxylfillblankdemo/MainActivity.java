package com.example.sp.yxylfillblankdemo;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.sp.yxylfillblankdemo.view.FillBlankTextView;

public class MainActivity extends Activity {

    private FillBlankTextView mFillBlank;

    private String mContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFillBlank = (FillBlankTextView) findViewById(R.id.tv_fill_blank);
        mContent = FileUtils.fetchFileContent(this, "html.txt");
        mFillBlank.setText(mContent);
    }
}
