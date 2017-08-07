package com.example.downzlibrary.DataTypes;

import com.example.downzlibrary.ListnerInterface.HttpListener;
import com.example.downzlibrary.Utilities.CacheManagerInterface;

/**
 * Created by saurabhkumar on 06/08/17.
 */

public abstract class Type<T> {
    public abstract Type setCacheManager(CacheManagerInterface<T> cacheManager);

    public abstract Type setCallback(HttpListener<T> callback);

    public abstract boolean cancel();
}