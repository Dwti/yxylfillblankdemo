package com.example.sp.yxylfillblankdemo.view;

import android.graphics.Color;

/**
 * Created by sp on 17-3-3.
 */

public class FillBlankEmptySpan extends EmptySpan {

    @Override
    protected int width() {
        return textWidth;
    }

    @Override
    protected int height() {
        return standardLineHeight;
    }

    @Override
    protected int color() {
        return Color.GREEN;
    }
}
