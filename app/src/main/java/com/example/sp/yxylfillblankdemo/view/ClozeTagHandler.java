package com.example.sp.yxylfillblankdemo.view;

import android.text.Editable;
import android.text.Html;
import android.text.Spannable;

import org.xml.sax.XMLReader;

/**
 * Created by sunpeng on 2017/6/15.
 */

public class ClozeTagHandler implements Html.TagHandler {
    protected int start,end;
    private static final String TAG = "empty";
    @Override
    public void handleTag(boolean opening, String tag, final Editable output, XMLReader xmlReader) {

        if (opening && tag.toLowerCase().equals(TAG)) {
            start = output.length();
        }
        if (!opening && tag.toLowerCase().equals(TAG)) {
            end = output.length();
            if (start != end) {
                output.setSpan(new EmptyReplacementSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }
}
