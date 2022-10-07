package com.leica.auto.process;

import com.leica.auto.annotations.AutoFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * {@link AutoFactory} 探测器
 *
 * @author <a href="mailto:705463446@qq.com">leica</a>
 * @date 2022-10-06 20:43
 */
public class AutoFactoryAnnotationDetector implements BeanFactoryPostProcessor, PriorityOrdered {

    private final List<Class<? extends Annotation>> detectAnnotations = new ArrayList<>();

    private final ClassPathAutoFactoryScanner scanner;

    private final List<Class<?>> detectFactoryInterfaceTypes = new ArrayList<>();

    public AutoFactoryAnnotationDetector() {
        this.detectAnnotations.add(AutoFactory.class);
        this.scanner = new ClassPathAutoFactoryScanner();
        this.scanner.addDetectAnnotations(detectAnnotations);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        List<String> scannerPackages = AutoConfigurationPackages.get(beanFactory);

        List<BeanDefinition> factoryInterfaceDefinitions = new ArrayList<>();
        for (String scannerPackage : scannerPackages) {
            Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(scannerPackage);
            factoryInterfaceDefinitions.addAll(candidateComponents);
        }

        try {
            for (BeanDefinition factoryInterfaceDefinition : factoryInterfaceDefinitions) {
                detectFactoryInterfaceTypes.add((Class.forName(factoryInterfaceDefinition.getBeanClassName())));
            }
        } catch (ClassNotFoundException ex) {
            // ignore
        }
        registerAutoFactoryProcessor((BeanDefinitionRegistry) beanFactory);
    }

    private void registerAutoFactoryProcessor(BeanDefinitionRegistry beanDefinitionRegistry) {
        AbstractBeanDefinition autoFactoryProcessorBeanDefinition = BeanDefinitionBuilder.genericBeanDefinition(AutoFactoryProcessor.class)
                .addPropertyValue("interfaces", this.detectFactoryInterfaceTypes)
                .getBeanDefinition();
        BeanDefinitionReaderUtils.registerWithGeneratedName(autoFactoryProcessorBeanDefinition, beanDefinitionRegistry);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    public void addDetectAnnotation(Class<? extends Annotation> annotation) {
        this.detectAnnotations.add(annotation);
        this.scanner.addIncludeFilter(new AnnotationTypeFilter(annotation));
    }

    public List<Class<? extends Annotation>> getDetectAnnotations() {
        return detectAnnotations;
    }
}
