package org.androidtown.palette_sliding;

/**
 * Created by chm31 on 2017-11-26.
 */

public interface Queue<T> {
    public void reset();
    public int size();
    public boolean isEmpty();
    public boolean isFull();
    public boolean add(T anElement);
    public T pop();
}
