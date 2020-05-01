package com.github.tools.pub;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: renhongqiang
 * @Date: 2020/5/1 9:49 下午
 **/
public final class Strings {
    private static Pattern LINE_PATTERN = Pattern.compile("_(\\w)");

    private Strings() {}


    public static String humpToLine(String str) {
        return str.replaceAll("[A-Z]", "_$0").toLowerCase();
    }

    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = LINE_PATTERN.matcher(str);
        StringBuffer bu = new StringBuffer();

        while(matcher.find()) {
            matcher.appendReplacement(bu, matcher.group(1).toUpperCase());
        }

        matcher.appendTail(bu);
        return bu.toString();
    }
}
