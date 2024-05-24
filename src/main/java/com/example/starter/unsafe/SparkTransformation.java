package com.example.starter.unsafe;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public interface SparkTransformation {
    void transform(Dataset<Row> dataset);
}

