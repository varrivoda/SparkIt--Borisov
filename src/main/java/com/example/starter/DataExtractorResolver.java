package com.example.starter;

import java.util.Map;

public class DataExtractorResolver {
    private Map<String, DataExtractor> extractorMap;

    public DataExtractor resolve(String pathToData){
        String fileExtension = pathToData.split("//.")[1];
        return extractorMap.get(fileExtension);
        //TODO NPE

    }

    // TODO Who will fill extractorMap?
}
