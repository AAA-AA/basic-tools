package com.github.tools.pub;

/**
 * 条件判断工具类
 */
public final class Conditions {
    private Conditions() {}

    /**
     * 用于比较输入源是否在给定的条件源
     * @param src
     * @param conditions
     * @return
     */
    public static boolean in(Object src, Object... conditions) {
        if (src == null || conditions == null || conditions.length == 0) {
            throw new IllegalArgumentException("src or conditions can't be null!");
        }
        for (Object condition : conditions) {
            if (src.equals(condition)) {
                return true;
            }
        }
        return false;
    }

    public static boolean notIn(Object src, Object... conditions) {
        return !in(src, conditions);
    }


}
