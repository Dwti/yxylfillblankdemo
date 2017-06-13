package com.example.sp.yxylfillblankdemo;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by sunpeng on 2017/6/12.
 */

public class StemUtil {

    public static String init(@NonNull String stem, @NonNull List<String> answers){
        //<fill>标签 表示空中有答案，需要展示  <empty>标签表示空中没有内容，需要显示为空白
        int i = 0;
        while (stem.contains("(_)")){
            if(TextUtils.isEmpty(answers.get(i))){
                stem = stem.replaceFirst("\\(_\\)", "<empty>oooooooooooo</empty>");
            }else {
                stem = stem.replaceFirst("\\(_\\)", "<fill>"+answers.get(i)+"</fill>");
            }
            i++;
        }
        //如果自定义标签标签为第一个字符时，taghandler解析的时候会有一个bug，导致第一个解析会跳过，然后会引起后面的图片显示也有问题
        if(stem.startsWith("<empty>") || stem.startsWith("<fill>")){
            stem = "&zwj" + stem;
        }

        StringBuilder sb = new StringBuilder(stem);
        while(stem.contains("</empty><empty>")){   //就是两个空连起来的情况，需要中间加一个空格
            int index = sb.indexOf("</empty><empty>");
            sb = sb.insert(index+8,"&nbsp");
            stem = sb.toString();
        }

        while(stem.contains("</fill><fill>")){
            int index = sb.indexOf("</fill><fill>");
            sb = sb.insert(index+7,"&nbsp");
            stem = sb.toString();
        }


        return stem;
    }
}
