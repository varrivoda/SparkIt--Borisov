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
    private Map<Method, Finalizer> finalizerMap = new HashMap<>();

    private Map<Method, List<SparkTransformation>> transformationChain = new HashMap<>();

    SparkInvocationHandler create(Class <? extends SparkRepository> repoIinterface){
        Class<?> modelClass = getModelClass(repoIinterface);
        Set<String> fieldNames = getFieldNames(modelClass);

        Method[] methods = repoIinterface.getMethods();
        for (Method method : methods) {
            TransformationSpider currentSpider =null;

            List<SparkTransformation> transformations = new ArrayList<>();
            Map<Method, Finalizer> method2Finalizer = new HashMap<>();

            List<String> methodWords = new ArrayList<>(
                    Arrays.asList(
                            method.getName().split("(?=\\p{Upper})")));

            while(methodWords.size()>1){
                String strategyName = WordsMatcher.findAndRemoveMatchingPiecesIfExists(
                        spiderMap.keySet(), methodWords);
                // This static method finds members of some list inside of another list.
                // It returns this found match and makes the romoves from second list found members.
                // we will use itin many places, but here..
                // Here it is finding strategyNames inside of repository method name,
                // And leaving in second list what's left after finding and removing strategy name/

                if(!strategyName.isEmpty()) {
                    currentSpider = spiderMap.get(strategyName);
                }
                transformations.add(currentSpider.createTransformation(methodWords));
                //1:58:20
            }
            transformationChain.put(method, transformations);

            // now let's do with the finalizer
            //it is "collect" by default
            // but if there ifonly one remaining word... it exactly must be a name of finalizer
            String finalizerName = "collect";
            if(methodWords.size()==1){
                finalizerName=methodWords.get(0);
            }
            method2Finalizer.put(method, finalizerMap.get(finalizerName));


        }

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
