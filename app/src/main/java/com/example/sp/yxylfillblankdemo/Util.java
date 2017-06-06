package com.example.sp.yxylfillblankdemo;

import android.content.Context;
import android.text.TextPaint;
import android.util.DisplayMetrics;

/**
 * Created by sp on 17-6-6.
 */

public class Util {

    public static int convertDpToPx(Context context, int dp) {
        if (context == null)
            return 0;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * displayMetrics.density);
    }

    public static float computeStringWidth(String str, TextPaint textPaint){
        return textPaint.measureText(str);
    }
}
