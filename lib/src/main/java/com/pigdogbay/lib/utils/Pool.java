package com.pigdogbay.lib.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Based on code from the Beginning Android Games 2nd edition by Zechner and Green
 * 
 *  Use this class to re-use common objects to cut down on object creation and garbage collection
 *
 * @param <T> Object type to pool
 */
public class Pool<T> {
    public interface PoolObjectFactory<T> {
        T createObject();
    }

    private final List<T> freeObjects;
    private final PoolObjectFactory<T> factory;
    private final int maxSize;

    public Pool(PoolObjectFactory<T> factory, int maxSize) {
        this.factory = factory;
        this.maxSize = maxSize;
        this.freeObjects = new ArrayList<>(maxSize);
    }

    public T newObject() {
        T object;

        if (freeObjects.size() == 0)
            object = factory.createObject();
        else
            object = freeObjects.remove(freeObjects.size() - 1);

        return object;
    }

    public void free(T object) {
        if (freeObjects.size() < maxSize)
            freeObjects.add(object);
    }
}
