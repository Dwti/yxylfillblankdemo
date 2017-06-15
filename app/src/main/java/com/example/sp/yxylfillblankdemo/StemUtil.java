package com.example.sp.yxylfillblankdemo;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by sunpeng on 2017/6/12.
 */

public class StemUtil {
    private static final String MARK_START = "<font color='#89e00d'>";
    private static final String MARK_END = "</font>";


    /**
     * 初始化填空题题干
     * @param stem  题干内容
     * @param answers 空的答案
     * @return  新的题干
     */
    public static String initFillBlankStem(@NonNull String stem, @NonNull List<String> answers){
        if(stem == null || answers == null){
            return null;
        }
        //<fill>标签 表示空中有答案，需要展示  <empty>标签表示空中没有内容，需要显示为空白
        int i = 0;
        while (stem.contains("(_)")){
            stem = replaceFirstChar(stem);
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

    /**
     * 如果空前面有首字母，加上html变色处理
     * @param source 源题干
     * @return 新的题干
     */
    public static String replaceFirstChar(String source){
        StringBuilder sb = new StringBuilder(source);
        int index = source.indexOf("(_)");
        if(index != -1){
            if(source.length() > 4 && source.startsWith(" ",index-2)){
                sb.insert(index-1,MARK_START);
                sb.insert(index+MARK_START.length(),MARK_END);
            }else if(source.length() == 4){
                sb.insert(0,MARK_START);
                sb.insert(MARK_START.length(),MARK_END);
            }
            return sb.toString();
        }else {
            return source;
        }
    }

    public static String initClozeStem(String stem){
        if(stem == null)
            return null;
        String str = stem.replaceAll("\\(_\\)", "<empty>empty</empty>");
        if(str.startsWith("<empty>"))
            str = "&zwj;" + str;                   //如果<Blank>标签为第一个字符时，taghandler解析的时候会有一个bug，导致第一个解析会跳过，然后会引起后面的图片显示也有问题
        StringBuilder sb = new StringBuilder(str);
        while(str.contains("</empty><empty>")){   //就是两个空连起来的情况，需要中间加一个空格
            int index = sb.indexOf("</empty><empty>");
            sb = sb.insert(index+8,"&nbsp");
            str = sb.toString();
        }
        return str;
    }
}
