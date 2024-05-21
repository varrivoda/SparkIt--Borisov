package com.example.starter;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class FilterTransformationSpider implements TransformationSpider {
    private Map<String, FilterTransformation> transformationMap;

    @Override
    public SparkTransformation createTransformation(List<String> remainingWords, Set<String> fieldNames) {
        //Firstly, need to unchain "nameOfGrandma" from the whole method name
        String fieldName = WordsMatcher.findAndRemoveMatchingPiecesIfExists(fieldNames, remainingWords);
        //OK, now remainingWords doesn't have "nameOfGramdma"

        //Now we must select needed filter, for example "Contains" etc
        String filterName = WordsMatcher.findAndRemoveMatchingPiecesIfExists(transformationMap.keySet(), remainingWords);
        return transformationMap.get(filterName);
    }
}
