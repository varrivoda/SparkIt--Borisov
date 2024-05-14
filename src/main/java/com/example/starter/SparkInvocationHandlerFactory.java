package com.example.starter;

import com.example.blacklist.SparkRepository;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SparkInvocationHandlerFactory {
    //must have:
    //-Extracror
    //-all the finalizers
    //-all the Strategy-Appliers
    //-context for some reason

    SparkInvocationHandler create(Class <? extends SparkRepository> repoIinterface){
        ParameterizedType[] genericInterfaces = (ParameterizedType[]) repoIinterface.getGenericInterfaces();
        Class<?> modelClass = (Class<?>) genericInterfaces[0].getActualTypeArguments()[0];
        String pathToData = modelClass.getAnnotation(Source.class).value();

        Set<Field> fieldNames = Arrays.stream(modelClass.getDeclaredFields())
                .filter(field -> !field.isAnnotationPresent(Transient.class))
                .filter(field->!Collection.class.isAssignableFrom(field.getType()))
                //up here we cannot ask whether our field extends Colletion
                //so we must to take a parent and ask him, does ous field extends him
                .collect(Collectors.toSet());



    }

}
