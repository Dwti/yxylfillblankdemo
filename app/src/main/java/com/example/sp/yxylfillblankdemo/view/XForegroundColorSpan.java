package com.example.sp.yxylfillblankdemo.view;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.support.annotation.ColorInt;
import android.text.style.ForegroundColorSpan;

/**
 * 新的ForegroundColorSpan类，因为题干里面通过 html设置的首字母变色也是通过ForegroundColorSpan来处理的
 * 所以为了防止在替换空寻找ForegroundColorSpan时把首字母的也算进来，所以用一个类去继承ForegroundColorSpan，找的时候用XForegroundColorSpan去找
 * 这样ForegroundColorSpan就不会被算进来
 * Created by sunpeng on 2017/6/14.
 */

@SuppressLint("ParcelCreator")
public class XForegroundColorSpan extends ForegroundColorSpan {
    public XForegroundColorSpan(@ColorInt int color) {
        super(color);
    }

    public XForegroundColorSpan(Parcel src) {
        super(src);
    }
}
