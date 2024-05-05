package com.example.blacklist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.hadoop.io.retry.Idempotent;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Criminal {
    private long id;
    private String name;
    private int number;

}
