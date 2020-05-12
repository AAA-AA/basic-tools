package com.github.tools.annotation;

import java.lang.annotation.*;

/**
 * @Author: renhongqiang
 * @Date: 2020/5/12 3:42 下午
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface HideAnn {
}
