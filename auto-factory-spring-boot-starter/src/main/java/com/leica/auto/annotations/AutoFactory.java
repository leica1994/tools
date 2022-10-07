package com.leica.auto.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动工厂模式
 *
 * @author <a href="mailto:705463446@qq.com">leica</a>
 * @date 2022-10-06 20:23
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AutoFactory {

}
