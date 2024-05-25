package com.example.starter.unsafe;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;

import java.util.List;

public class BetweenFilter implements FilterTransformation {
    @Override
    public void transform(Dataset<Row> dataset, List<String> fieldNames, List<Object> args) {
        dataset.filter(functions.col(fieldNames.get(0)).between(args.remove(0), args.remove(0)));
        // с листами не очень, надо написать кастомную коллекцию, чтобы не делать ремув()
    }
}
