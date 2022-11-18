package ru.javawebinar.topjava.util;

public class Util {

    public static <T extends Comparable<T>> boolean isBeetweenHalfOpen(T value, T start, T end){
        return (start==null||value.compareTo(start)>=0)&&(end==null||value.compareTo(end)<0);
    }
}
