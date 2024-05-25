package com.example.starter.unsafe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DataExtractorResolver {
    @Autowired
    private Map<String, DataExtractor> extractorMap;

    public DataExtractor resolve(String pathToData){
        String fileExtension = pathToData.split("//.")[1];
        return extractorMap.get(fileExtension);
        //TODO NPE

    }

    // TODO Who will fill extractorMap?
}
