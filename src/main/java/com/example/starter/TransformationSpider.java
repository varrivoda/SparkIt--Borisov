package com.example.starter;

import java.util.List;
import java.util.Set;

public interface TransformationSpider {
    SparkTransformation createTransformation(List<String> remainingWords, Set<String> fieldNames);
}

