package com.example.starter.unsafe;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.context.ConfigurableApplicationContext;

public interface DataExtractor {
    Dataset<Row> readData(String pathToData, ConfigurableApplicationContext context);
}
