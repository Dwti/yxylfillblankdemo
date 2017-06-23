package com.example.sp.yxylfillblankdemo.view;

import android.graphics.Color;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;

import org.xml.sax.XMLReader;

/**
 * Created by sp on 17-6-8.
 */

public class FillBlankTagHandler implements Html.TagHandler {

    protected int start,end;

    @Override
    public void handleTag(boolean opening, String tag, final Editable output, XMLReader xmlReader) {

        //有内容的空
        if (opening && tag.toLowerCase().equals("fill")) {
            start = output.length();
        }
        if (!opening && tag.toLowerCase().equals("fill")) {
            end = output.length();
            if (start != end) {
                output.setSpan(new XForegroundColorSpan(Color.parseColor("#89e00d")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        //没有内容的空
        if (opening && tag.toLowerCase().equals("empty")) {
            start = output.length();
        }
        if (!opening && tag.toLowerCase().equals("empty")) {
            end = output.length();
            if (start != end) {
                output.setSpan(new XForegroundColorSpan(Color.TRANSPARENT), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }
}
