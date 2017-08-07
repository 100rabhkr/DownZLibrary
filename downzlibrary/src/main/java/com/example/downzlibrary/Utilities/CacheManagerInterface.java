package com.example.downzlibrary.Utilities;

/**
 * Created by saurabhkumar on 06/08/17.
 */

public interface CacheManagerInterface<T> {
    public void addDataToCache(String key, T data);

    public void removeDataFromCache(String key);

    public T getDataFromCache(String key);

    public void evictUnused();
}
