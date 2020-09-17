package com.wpp.devtools.domain.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @program: devtools-server
 * @description:
 * @author: wpp
 * @create: 2020-08-14
 **/
@Retention(RUNTIME)
@Target(METHOD)
public @interface AccessLimit {

    int seconds();
    int maxCount();
}