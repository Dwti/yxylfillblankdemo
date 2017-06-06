package com.example.sp.yxylfillblankdemo;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by sp on 17-2-24.
 */

public class FileUtils {
    public static String fetchFileContent(Context context,String filename) {
        AssetManager am = context.getAssets();
        String str = "";
        try {
            InputStream inputStream = am.open(filename);
            int length = inputStream.available();
            byte[] buffer = new byte[length];
            inputStream.read(buffer);
            str = new String(buffer, "UTF-8");
        } catch (IOException e) {
            Log.e("tag", e.getLocalizedMessage());
            e.printStackTrace();
        }

        return str;
    }
}
