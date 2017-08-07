package com.example.downzlibrary.DataTypes;

import android.graphics.Bitmap;

import com.example.downzlibrary.DownZ;
import com.example.downzlibrary.ListnerInterface.HttpListener;
import com.example.downzlibrary.Parameters.HeaderParams;
import com.example.downzlibrary.Parameters.RequestParams;
import com.example.downzlibrary.RequestTasks.BitMapTask;
import com.example.downzlibrary.Utilities.CacheManagerInterface;

import java.util.ArrayList;

/**
 * Created by saurabhkumar on 06/08/17.
 */

public class BitMap extends Type<Bitmap> {
    private String url;
    private HttpListener<Bitmap> listener;
    private DownZ.Method method;
    private ArrayList<RequestParams> params;
    private ArrayList<HeaderParams> headers;
    private BitMapTask mTask;
    private CacheManagerInterface<Bitmap> mCacheManager;

    /**
     * Constructor to load json datatypes
     */
    public BitMap(DownZ.Method m, String url, ArrayList<RequestParams> params, ArrayList<HeaderParams> headers) {
        this.url = url;
        this.method = m;
        this.headers = headers;
        this.params = params;
    }

    /**
     * Sets future callback after Http response is received
     *
     * @param listener
     */
    public BitMap setCallback(HttpListener<Bitmap> listener) {
        this.listener = listener;
        this.listener.onRequest();
        Bitmap data;
        if (mCacheManager != null) {
            data = mCacheManager.getDataFromCache(url);
            if (data != null) {
                this.listener.onResponse(data);
                return this;
            }
        }

        mTask = new BitMapTask(method, url, params, headers, this.listener);
        mTask.setmCachemanager(mCacheManager);
        mTask.execute();
        return this;
    }

    /**
     * Cancels the current request
     *
     * @return True if cancelled
     */
    public boolean cancel() {
        if (mTask != null) {
            mTask.cancel(true);
            if (mTask.isCancelled()) {
                listener.onCancel();
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    /**
     * Lets depend on abstraction
     * Sets CacheManager for this
     *
     * @param cache
     * @return JsonObjectType
     */
    public BitMap setCacheManager(CacheManagerInterface<Bitmap> cache) {
        this.mCacheManager = cache;
        return this;
    }


}
