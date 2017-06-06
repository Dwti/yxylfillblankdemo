package com.example.sp.yxylfillblankdemo.view;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.text.style.DynamicDrawableSpan;

/**
 * Created by cailei on 05/01/2017.
 */

public abstract class EmptySpan extends DynamicDrawableSpan {
    public int standardLineHeight;
    public int textWidth;

    @Override
    public Drawable getDrawable() {
        Drawable d = new Drawable() {
            @Override
            public void draw(Canvas canvas) {
                Paint p = new Paint();
                p.setColor(color());
                canvas.drawRect(0,0,width(),height(),p);
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter colorFilter) {

            }

            @Override
            public int getOpacity() {
                return PixelFormat.OPAQUE;
            }
        };
        d.setBounds(0, 0, width(), height());
        return d;
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        if (fm != null) {
            if (height() > standardLineHeight) {
                int orgDescent = fm.descent;
                int extraSpace = height() - (fm.descent - fm.ascent);
                fm.descent = extraSpace / 2 + orgDescent;
                fm.bottom = fm.bottom + extraSpace / 2;
                fm.ascent = -height() + fm.descent;
                fm.top = fm.top - extraSpace / 2;
            }
        }
        return width();
    }

    protected abstract int width();
    protected abstract int height();
    protected abstract int color();
}
