package com.example.starter;

import lombok.Builder;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@Builder
public class SparkInvocationHandlerImpl implements SparkInvocationHandler {

    private Class<?> modelClass;
    private String pathToData;
    private DataExtractor extractor;
    private Map<Method, List<SparkTransformation>> transformationChain;
    private Map<Method, Finalizer> finalizerMap;
    private ConfigurableApplicationContext context;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Dataset<Row> dataset = extractor.readData(pathToData, context);
        List<SparkTransformation> sparkTransformations = transformationChain.get(method);
        for (SparkTransformation transformation:sparkTransformations){
            transformation.transform(dataset);
        }

        Finalizer finalizer = finalizerMap.get(method);
        return finalizer.doAction(dataset);
    }
}
