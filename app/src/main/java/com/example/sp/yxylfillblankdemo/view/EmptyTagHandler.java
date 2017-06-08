package com.example.sp.yxylfillblankdemo.view;

import android.graphics.Color;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;

import org.xml.sax.XMLReader;

/**
 * Created by sp on 17-6-8.
 */

public class EmptyTagHandler implements Html.TagHandler {

    protected int start,end;

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {

        if (opening && tag.toLowerCase().equals(getTag())) {
            start = output.length();
        }
        if (!opening && tag.toLowerCase().equals(getTag())) {
            end = output.length();
            if (start != end) {
                ForegroundColorSpan fcs = new ForegroundColorSpan(Color.TRANSPARENT);
                output.setSpan(fcs, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    protected String getTag(){
        return "empty";
    }
}
