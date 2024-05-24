package com.example.starter.unsafe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data

@ConfigurationProperties(prefix="spark")
public class SparkPropsHolder {
    private String appName;
    private String packagesToScan;
}


