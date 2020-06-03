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

    public synchronized static String cleanSpecialChar(String str) {
        //过滤掉非字母，数字，中文的符号
        str = str.replaceAll("[^(a-zA-Z0-9\\u4e00-\\u9fa5)]", "");
        return str;
    }

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

    public static String substring(String str, int start, int end) {
        if (str == null) {
            return null;
        } else {
            if (end < 0) {
                end += str.length();
            }

            if (start < 0) {
                start += str.length();
            }

            if (end > str.length()) {
                end = str.length();
            }

            if (start > end) {
                return "";
            } else {
                if (start < 0) {
                    start = 0;
                }

                if (end < 0) {
                    end = 0;
                }

                return str.substring(start, end);
            }
        }
    }

    public static String left(String str, int len) {
        if (str == null) {
            return null;
        } else if (len < 0) {
            return "";
        } else {
            return str.length() <= len ? str : str.substring(0, len);
        }
    }

    public static String right(String str, int len) {
        if (str == null) {
            return null;
        } else if (len < 0) {
            return "";
        } else {
            return str.length() <= len ? str : str.substring(str.length() - len);
        }
    }

    public static String mid(String str, int pos, int len) {
        if (str == null) {
            return null;
        } else if (len >= 0 && pos <= str.length()) {
            if (pos < 0) {
                pos = 0;
            }

            return str.length() <= pos + len ? str.substring(pos) : str.substring(pos, pos + len);
        } else {
            return "";
        }
    }

}
