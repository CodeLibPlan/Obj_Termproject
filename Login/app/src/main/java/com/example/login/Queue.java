package com.example.login;

/**
 * Created by 전혜민 on 2017-11-21.
 */


public interface Queue<T> {
    public void reset();
    public int size();
    public boolean isEmpty();
    public boolean isFull();
    public boolean add(T anElement);
    public T pop();
}
