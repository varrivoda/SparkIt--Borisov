package com.example.starter.unsafe;

import com.example.blacklist.Source;
import com.example.blacklist.SparkRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SparkInvocationHandlerFactory {
    //must have:
    //-Extractors
    //-all the finalizers
    //-all the Strategy-Appliers
    //-context for some reason
    private final DataExtractorResolver dataExtractorResolver;
    private final Map<String, TransformationSpider> spiderMap;
    private final Map<Method, Finalizer> finalizerMap;

    @Setter
    private ConfigurableApplicationContext realContext;
    //? private SparkInvocationHandlerImpl sparkInvocationHandlerImpl;

    SparkInvocationHandler create(Class <? extends SparkRepository> repoInterface){
        Class<?> modelClass = getModelClass(repoInterface);
        Set<String> fieldNames = getFieldNames(modelClass);
        String pathToData = modelClass.getAnnotation(Source.class).value();
        DataExtractor dataExtractor = dataExtractorResolver.resolve(pathToData);

        Map<Method, List<SparkTransformation>> transformationChain = new HashMap<>();
        Map<Method, Finalizer> method2Finalizer = new HashMap<>();

        Method[] methods = repoInterface.getMethods();
        for (Method method : methods) {
            TransformationSpider currentSpider =null;
            List<SparkTransformation> transformations = new ArrayList<>();
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

            // now let's do with the finalizer
            //it is "collect" by default
            // but if there ifonly one remaining word... it exactly must be a name of finalizer
            String finalizerName = "collect";
            if(methodWords.size()==1){
                finalizerName=methodWords.get(0);
            }
            transformationChain.put(method, transformations);
            method2Finalizer.put(method, finalizerMap.get(finalizerName));

        }

        return sparkInvocationHandlerImpl.builder()
                .modelClass(modelClass)
                .pathToData(pathToData)
                .finalizerMap(method2Finalizer)
                .transformationChain(transformationChain)
                .extractor(dataExtractor)
                .context(realContext)
                .build();

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
        return modelClass;
    }

}
