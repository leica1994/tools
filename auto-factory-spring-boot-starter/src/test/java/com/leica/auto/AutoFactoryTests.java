package com.leica.auto;

import com.leica.auto.factory.AutoFactory;
import com.leica.auto.strategy.Strategy;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Map;

/**
 * 自动工厂测试
 *
 * @author <a href="mailto:705463446@qq.com">leica</a>
 * @date 2022-10-07 19:59
 */
@SpringBootApplication
public class AutoFactoryTests {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(AutoFactoryTests.class)
                .web(WebApplicationType.NONE)
                .run(args);

        List<Animal> animals = AutoFactory.getInstance().find(Animal.class);
        animals.forEach(Animal::eat);

        Map<AnimalType, Animal> map = AutoFactory.getInstance().findStrategyMap(AnimalType.class, Animal.class);
        map.forEach((k, v) -> System.out.println("枚举类型:" + k + ", animal: " + v));

        context.close();
    }

    @Bean
    public Dog dog() {
        return new Dog();
    }

    @Bean
    public Cat cat() {
        return new Cat();
    }

    @com.leica.auto.annotations.AutoFactory
    public interface Animal extends Strategy {
        void eat();
    }

    public enum AnimalType {
        DOG,
        CAT
    }

    public static class Dog implements Animal {
        @Override
        public void eat() {
            System.out.println("dog eat shit!");
        }

        @Override
        public Enum<?> getStrategy() {
            return AnimalType.DOG;
        }
    }

    public static class Cat implements Animal {

        @Override
        public void eat() {
            System.out.println("cat eat fish!");
        }

        @Override
        public Enum<?> getStrategy() {
            return AnimalType.CAT;
        }
    }

}
