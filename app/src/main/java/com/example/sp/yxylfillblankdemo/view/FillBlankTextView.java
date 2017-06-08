package com.example.sp.yxylfillblankdemo.view;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;


import com.example.sp.yxylfillblankdemo.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sp on 17-2-24.
 */

public class FillBlankTextView extends SpanReplaceableTextView<EditText> implements TextWatcher,View.OnClickListener {

    private FilledContentChangeListener filledContentChangeListener;

    private List<String> mData = new ArrayList<>();

    private EditText mCurrentClickEditText ;

    private OnEditTextClickListener mOnEditTextClickListener;

    public FillBlankTextView(Context context) {
        super(context);
    }

    public FillBlankTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FillBlankTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected EditText getView() {
        EditText editText = new EditText(getContext());
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
        editText.setTextColor(Color.BLACK);
        editText.setSingleLine();
        editText.setPadding(10,0,10,0);
        editText.setGravity(Gravity.CENTER);
        editText.addTextChangedListener(this);
        editText.setOnClickListener(this);
        return editText;
    }

    @Override
    protected ReplaceTagHandler getTagHandler() {
        return new FillBlankTagHandler();
    }

    private void initFilledContent(List<String> data) {
        if (data == null || data.size() == 0) {
            return;
        }
        if (data == null || data.size() == 0)
            return;
        List<EditText> editTexts = getReplacement();
        for (int i = 0; i < editTexts.size(); i++) {
            if (data.size() == i)
                break;
            editTexts.get(i).setText(data.get(i));
        }
    }

    public void setFilledContent(final List<String> data) {
        if(data != null)
            mData = data;
        if (mIsReplaceCompleted)
            initFilledContent(data);
        else {
            post(new Runnable() {
                @Override
                public void run() {
                    setFilledContent(data);
                }
            });
        }
    }

    public List<String> getFilledContent() {
        if(!mIsReplaceCompleted)
            return mData;
        List<String> list = new ArrayList<>();
        List<EditText> editTexts = getReplacement();
        for (EditText et : editTexts) {
            list.add(et.getText().toString());
        }
        return list;
    }

    public void setEditable(final boolean editable) {
        if(mIsReplaceCompleted){
            setEditTextEditable(getReplacement(),editable);
        }else {
            post(new Runnable() {
                @Override
                public void run() {
                    setEditable(editable);
                }
            });
        }

    }

    private void setEditTextEditable(List<EditText> editTexts, boolean editable){
        for (EditText et : editTexts) {
            et.setEnabled(editable);
            et.setFocusable(editable);
            et.setFocusableInTouchMode(editable);
        }
    }

    public FilledContentChangeListener getFilledContentChangeListener() {
        return filledContentChangeListener;
    }

    public void setFilledContentChangeListener(FilledContentChangeListener filledContentChangeListener) {
        this.filledContentChangeListener = filledContentChangeListener;
    }

    @Override
    protected int getSpanWidth(List<String> listString) {
        return Util.convertDpToPx(getContext(),60);
    }

    @Override
    protected void replaceSpanWithView(Spanned spannedStr) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(filledContentChangeListener != null){
            filledContentChangeListener.filledContentChanged(getFilledContent());
        }
    }

    @Override
    public void onClick(View v) {
        if(mOnEditTextClickListener != null){
            mOnEditTextClickListener.onEditTextClick((EditText) v);
        }
    }

    public void setOnEditTextClickListener(OnEditTextClickListener onEditTextClickListener) {
        this.mOnEditTextClickListener = onEditTextClickListener;
    }

    public interface OnEditTextClickListener{
        void onEditTextClick(EditText editText);
    }
}
