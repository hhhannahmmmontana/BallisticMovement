package com.volodya.ballisticmovement.types;

import java.io.Serializable;

public class Pair<T, E> implements Serializable {
    public T first;
    public E second;
    public Pair(T first, E second) {
        this.first = first;
        this.second = second;
    }
    public javafx.util.Pair<T, E> toJavaFXPair() {
        return new javafx.util.Pair<>(first, second);
    }
}
