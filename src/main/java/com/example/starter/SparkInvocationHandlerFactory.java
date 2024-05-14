package com.example.starter;

import com.example.blacklist.SparkRepository;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class SparkInvocationHandlerFactory {
    //must have:
    //-Extracror
    //-all the finalizers
    //-all the Strategy-Appliers
    //-context for some reason

    private DataExtractorResolver dataExtractorResolver;
    private Map<String, TransformationSpider> spiderMap;

    private Map<Method, List<SparkTransformation>> transformationChain = new HashMap<>();
    private Map<Method, Finalizer> finalizerMap = new HashMap<>();

    SparkInvocationHandler create(Class <? extends SparkRepository> repoIinterface){
        Class<?> modelClass = getModelClass(repoIinterface);
        Set<String> fieldNames = getFieldNames(modelClass);


    }

    private Set<String> getFieldNames(Class<?> modelClass) {
        return Arrays.stream(modelClass.getDeclaredFields())
                .filter(field -> !field.isAnnotationPresent(Transient.class))
                .filter(field -> !Collection.class.isAssignableFrom(field.getType()))
                .map(Field::getName)
                //up here we cannot ask whether our field extends Colletion
                //so we must to take a parent and ask him, does ous field extends him
                .collect(Collectors.toSet());
    }

    private Class<?> getModelClass(Class<? extends SparkRepository> repoIinterface) {
        ParameterizedType[] genericInterfaces = (ParameterizedType[]) repoIinterface.getGenericInterfaces();
        Class<?> modelClass = (Class<?>) genericInterfaces[0].getActualTypeArguments()[0];
        String pathToData = modelClass.getAnnotation(Source.class).value();
        return modelClass;
    }

}
