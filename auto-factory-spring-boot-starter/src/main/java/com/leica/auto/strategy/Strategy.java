package com.leica.auto.strategy;

/**
 * 策略模式接口
 *
 * @author <a href="mailto:705463446@qq.com">leica</a>
 * @date 2022-10-06 22:29
 */
public interface Strategy {

    /**
     * 策略枚举
     *
     * @return 策略枚举
     */
    Enum<?> getStrategy();
}
