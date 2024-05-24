package com.example.starter.unsafe;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class WordsMatcher {
    public static String findAndRemoveMatchingPiecesIfExists(Set<String> options, List<String> pieces) {
        //тут алгоритм
        StringBuilder match = new StringBuilder(pieces.remove(0));
        List<String> remainingOptions = options.stream()
                .filter(option->option.toLowerCase().startsWith(match.toString().toLowerCase()))
                .collect(Collectors.toList());
        //фильтр оставил нам "name" и "nameOfGrandma"

        if(remainingOptions.isEmpty()) return "";

        while (remainingOptions.size()>1){
            match.append(pieces.remove(0));
            remainingOptions.removeIf(
                    option->!option.toLowerCase().startsWith(
                            match.toString().toLowerCase()
                    )
            );
        }

        //теперь допустим, что у нас есть только nameOfGrandma
        //но на данном этапе только name - надо прождолжать матчить,пока equals полностью не совпадет
        while(remainingOptions.get(0).equalsIgnoreCase(match.toString())){
            match.append(pieces.remove(0));
        }

        return match.toString();
    }
}
