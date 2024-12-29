package com.learn.hungbv.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Schedule {
    long fixedRate() default 0; // Thời gian lặp lại (mili giây)
    long time() default 0; // Số lần lặp lại (số lần)
}

