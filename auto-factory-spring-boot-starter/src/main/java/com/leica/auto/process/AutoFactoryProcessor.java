package com.leica.auto.process;

import com.leica.auto.factory.AutoFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.*;

/**
 * {@link com.leica.auto.annotations.AutoFactory} 处理器
 *
 * @author <a href="mailto:705463446@qq.com">leica</a>
 * @date 2022-10-06 21:07
 */
public class AutoFactoryProcessor implements BeanPostProcessor, SmartInitializingSingleton {

    private List<Class<?>> interfaces = new ArrayList<>();

    private final Map<Class<?>, List<Object>> factoryCache = new HashMap<>();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> factoryInterface = factoryInterface(bean);
        if (Objects.nonNull(factoryInterface)) {
            doRegisterFactory(factoryInterface, bean);
        }
        return null;
    }

    private void doRegisterFactory(Class<?> factoryInterface, Object bean) {
        factoryCache.computeIfAbsent(factoryInterface, k -> new ArrayList<>());
        factoryCache.get(factoryInterface).add(bean);
    }

    public Class<?> factoryInterface(Object bean) {
        for (Class<?> anInterface : interfaces) {
            if (anInterface.isAssignableFrom(bean.getClass())) {
                return anInterface;
            }
        }
        return null;
    }

    public List<Class<?>> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(List<Class<?>> interfaces) {
        this.interfaces = interfaces;
    }

    @Override
    public void afterSingletonsInstantiated() {
        AutoFactory.getInstance().initialize(this.factoryCache);
        factoryCache.clear();
        interfaces.clear();
    }
}
