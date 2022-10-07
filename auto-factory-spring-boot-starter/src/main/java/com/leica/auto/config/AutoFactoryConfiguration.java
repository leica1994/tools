package com.leica.auto.config;

import com.leica.auto.process.AutoFactoryAnnotationDetector;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动工厂 配置类
 *
 * @author <a href="mailto:705463446@qq.com">leica</a>
 * @date 2022-10-06 20:16
 */
@ConditionalOnClass(ApplicationContext.class)
@Configuration(proxyBeanMethods = false)
public class AutoFactoryConfiguration {

    @Bean
    public AutoFactoryAnnotationDetector autoFactoryAnnotationDetector() {
        return new AutoFactoryAnnotationDetector();
    }
}
