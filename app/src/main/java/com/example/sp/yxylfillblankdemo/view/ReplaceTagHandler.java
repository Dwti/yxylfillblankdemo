package com.example.sp.yxylfillblankdemo.view;

import android.text.Editable;
import android.text.Html;
import android.text.Spannable;

import org.xml.sax.XMLReader;

/**
 * Created by sp on 17-2-24.
 */

public abstract class ReplaceTagHandler implements Html.TagHandler {
    protected int start,end;
    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {

        if (opening && tag.toLowerCase().equals(getTag())) {
            start = output.length();
        }
        if (!opening && tag.toLowerCase().equals(getTag())) {
            end = output.length();
            if (start != end) {
                EmptySpan es;
                try {
                    es = getSpanType().newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    return;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return;
                }
                output.setSpan(es, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    protected abstract Class<? extends EmptySpan> getSpanType();

    protected abstract String getTag();
}
