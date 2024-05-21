package com.example.starter;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public interface FilterTransformation extends SparkTransformation {
    Dataset<Row> transform(Dataset<Row> dataset);
}
