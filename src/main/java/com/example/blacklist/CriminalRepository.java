package com.example.blacklist;

import java.util.List;

public interface CriminalRepository extends SparkRepository<Criminal>{
    List<Criminal> findByNumberBetween(int min, int max);

    List<Criminal> findByNameOfБабушкаContainsAndAgeLessThanOrderByAgeAndNameSave(
            String name, String nameContains, int ageLessThan);
}
