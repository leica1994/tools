package com.leica.auto.factory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author <a href="mailto:705463446@qq.com">leica</a>
 * @date 2022-10-06 20:33
 */
public class AutoFactory {

    private AutoFactoryCache factoryCache;

    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public void initialize(Map<Class<?>, List<Object>> factories) {
        if (initialized.compareAndSet(false, true)) {
            this.factoryCache = new AutoFactoryCache(factories);
        }
    }

    public <T> List<T> find(Class<T> factoryInterface) {
        if (!initialized.get()) {
            throw new IllegalStateException("factoryCache is uninitialized , please call #initialize method first!");
        }
        return factoryCache.find(factoryInterface);
    }

    public <E extends Enum<E>, T> Map<E, T> findStrategyMap(Class<E> factoryEnum, Class<T> factoryInterface) {
        if (!initialized.get()) {
            throw new IllegalStateException("factoryCache is uninitialized , please call #initialize method first!");
        }
        return factoryCache.strategy(factoryEnum, factoryInterface);
    }

    public static AutoFactory getInstance() {
        return Singleton.INSTANCE;
    }

    private static class Singleton {
        private static final AutoFactory INSTANCE = new AutoFactory();
    }

    private AutoFactory() {
    }
}
