package com.example.blacklist;

import java.util.List;

public interface CriminalRepository extends SparkRepository<Criminal>{
    List<Criminal> findByNumberBetween(int min, int max);
}
