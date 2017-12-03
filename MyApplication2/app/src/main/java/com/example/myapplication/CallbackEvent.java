package com.example.myapplication;

/**
 * Created by 전혜민 on 2017-11-21.
 */

public interface CallbackEvent<T> {
    public void run(T input);
}