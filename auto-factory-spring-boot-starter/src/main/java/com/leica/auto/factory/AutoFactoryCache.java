package com.leica.auto.factory;

import com.leica.auto.strategy.Strategy;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 自动工厂缓存
 *
 * @author <a href="mailto:705463446@qq.com">leica</a>
 * @date 2022-10-06 23:30
 */
public class AutoFactoryCache {

    private final Map<Class<?>, List<?>> factoryCache = new ConcurrentHashMap<>();

    private final Map<Class<?>, Map<Enum<?>, Object>> strategyFactoryCache = new ConcurrentHashMap<>();

    public AutoFactoryCache(Map<Class<?>, List<Object>> factories) {
        factoryCache.putAll(factories);
        initStrategyFactoryCache();
    }

    private void initStrategyFactoryCache() {
        for (Map.Entry<Class<?>, List<?>> entry : factoryCache.entrySet()) {
            entry.getValue()
                    .stream()
                    .filter(Strategy.class::isInstance)
                    .map(Strategy.class::cast)
                    .filter(strategy -> Objects.nonNull(strategy.getStrategy()))
                    .forEach(entity -> addStrategyFactoryCache(entry.getKey(), entity));
        }
    }

    private void addStrategyFactoryCache(Class<?> interfaceClass, Strategy strategy) {
        strategyFactoryCache.computeIfAbsent(interfaceClass, key -> new HashMap<>());
        strategyFactoryCache.get(interfaceClass).put(strategy.getStrategy(), strategy);
    }

    public <T> List<T> find(Class<T> factoryInterface) {
        return factoryCache.getOrDefault(factoryInterface, new ArrayList<>())
                .stream()
                .map(factoryInterface::cast)
                .collect(Collectors.toList());
    }

    public <E extends Enum<E>, T> Map<E, T> strategy(Class<E> factoryEnumClass, Class<T> factoryInterfaceClass) {
        return Optional.ofNullable(strategyFactoryCache.get(factoryInterfaceClass))
                .orElseGet(HashMap::new)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> factoryEnumClass.cast(entry.getKey()),
                        entry -> factoryInterfaceClass.cast(entry.getValue()),
                        (o1, o2) -> o1,
                        HashMap::new));
    }
}
