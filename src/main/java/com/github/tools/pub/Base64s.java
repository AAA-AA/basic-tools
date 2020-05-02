package com.github.tools.pub;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @Author: renhongqiang
 * @Date: 2020/5/2 9:30 下午
 **/
public final class Base64s {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private Base64s () {}


    public static String encodeToString(byte[] src) {
        return src.length == 0 ? "" : new String(encode(src), DEFAULT_CHARSET);
    }

    public static byte[] encode(byte[] src) {
        return src.length == 0 ? src : Base64.getEncoder().encode(src);
    }
}
