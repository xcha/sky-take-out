package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)// 注解所修饰的对象范围
@Retention(RetentionPolicy.RUNTIME)// 注解所存活的时间
public @interface AutoFill {// 自动填充注解
    OperationType value();// 操作类型
}
