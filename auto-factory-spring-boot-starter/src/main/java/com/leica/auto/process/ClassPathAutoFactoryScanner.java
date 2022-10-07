package com.leica.auto.process;

import com.leica.auto.annotations.AutoFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link AutoFactory} 接口扫描器
 *
 * @author <a href="mailto:705463446@qq.com">leica</a>
 * @date 2022-10-06 22:10
 */
public class ClassPathAutoFactoryScanner extends ClassPathScanningCandidateComponentProvider {

    private final List<Class<? extends Annotation>> detectAnnotations = new ArrayList<>();

    public ClassPathAutoFactoryScanner() {
        super(false);
    }

    public void addDetectAnnotations(List<Class<? extends Annotation>> detectAnnotations) {
        for (Class<? extends Annotation> detectAnnotation : detectAnnotations) {
            addDetectAnnotation(detectAnnotation);
        }
    }

    public void addDetectAnnotation(Class<? extends Annotation> detectAnnotation) {
        this.detectAnnotations.add(detectAnnotation);
        super.addIncludeFilter(new AnnotationTypeFilter(detectAnnotation));
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }

    public List<Class<? extends Annotation>> getDetectAnnotations() {
        return detectAnnotations;
    }
}
