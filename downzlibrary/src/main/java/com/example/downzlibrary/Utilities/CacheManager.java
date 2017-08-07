package com.example.downzlibrary.Utilities;

import android.graphics.Bitmap;
import android.os.SystemClock;
import android.util.LruCache;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by saurabhkumar on 06/08/17.
 */

public class CacheManager<T> extends LruCache<String, T> implements CacheManagerInterface<T> {

    private final int timeout = 10 * 1000 * 60;

    //timestamp to remove unused items after 10 minutes
    HashMap<String, Long> cacheHitTimestamp = new HashMap<>();

    /**
     * Constructor
     *
     * @param cacheSize size of the cache in bytes
     */

    public CacheManager(int cacheSize) {
        super(cacheSize);
    }

    @Override
    protected int sizeOf(String key, T data) {
        // The cache size will be measured in kilobytes rather than
        // number of items.
        int bytesCount;
        if (data instanceof Bitmap) {
            bytesCount = ((Bitmap) data).getByteCount();
        } else if (data instanceof JSONObject) {
            bytesCount = ((JSONObject) data).toString().getBytes().length;
        } else {
            bytesCount = ((JSONArray) data).toString().getBytes().length;
        }

        return bytesCount / 1024;

    }

    /**
     * Adds data to memory cache
     *
     * @param key  key to identify cache resource
     * @param data Data to be stored in cache
     */
    @Override
    public void addDataToCache(String key, T data) {
        if (getDataFromCache(key) == null) {
            synchronized (this) {
                put(key, data);
                cacheHitTimestamp.put(key, SystemClock.uptimeMillis()); //count to 0 when added
            }
        }
    }


    /**
     * Removes data from cache
     *
     * @param key identifier to resource in cache
     */
    @Override
    public void removeDataFromCache(String key) {
        if (getDataFromCache(key) != null) {
            synchronized (this) {
                remove(key);
            }
        }
    }

    /**
     * Gets resource from cache
     *
     * @param key identifier
     * @return resource
     */
    @Override
    public T getDataFromCache(String key) {
        synchronized (this) {
            cacheHitTimestamp.put(key, SystemClock.uptimeMillis());
            evictUnused();
        }
        return get(key);
    }

    /**
     * Removes items that are not used
     */
    @Override
    public void evictUnused() {
        Map<String, T> items = snapshot();
        for (String key : items.keySet()) {
            long cacheTime = cacheHitTimestamp.get(key);
            if (cacheTime + timeout < SystemClock.uptimeMillis()) {
                remove(key);
            }

        }
    }


}
