package com.example.starter.unsafe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderedBag<T> {
    private List<T> list;

    public OrderedBag(T[] array) {
        this.list = new ArrayList<>(List.of(array));
    }


    public T takeAndRemove() {
        return list.remove(0);
    }

    public int size(){
        return list.size();
    }
}
