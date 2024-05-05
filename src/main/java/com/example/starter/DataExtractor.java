package com.example.starter;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public interface DataExtractor {
    Dataset<Row> readData(String pathToData);
}
