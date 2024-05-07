package com.example.starter;

import com.example.blacklist.SparkRepository;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.reflections.Reflections;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import java.beans.Introspector;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class SparkApplicationContextInitializer implements ApplicationContextInitializer {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        registerBeans(applicationContext);

        Reflections scanner = new Reflections(
                applicationContext.getEnvironment().getProperty("path-to-data"));
        scanner.getSubTypesOf(SparkRepository.class).forEach(sparkRepositoryInterface->{
            Object proxy = Proxy.newProxyInstance(sparkRepositoryInterface.getClassLoader(),
                    new Class[]{sparkRepositoryInterface},
                    invocationHandler);
            applicationContext.getBeanFactory().registerSingleton(Introspector.decapitalize(sparkRepositoryInterface.getSimpleName()),proxy);
        });

    }

    private void registerBeans(ConfigurableApplicationContext applicationContext){
        SparkSession sparkSession = SparkSession.builder()
                .master("local[*]")
                .appName(applicationContext.getEnvironment().getProperty("spark.app-name"))
                .getOrCreate();

        JavaSparkContext sparkContext = new JavaSparkContext(sparkSession.sparkContext());

        applicationContext.getBeanFactory().registerSingleton("sparkSession", sparkSession);
        applicationContext.getBeanFactory().registerSingleton("sparkContext", sparkContext);
    }
}
