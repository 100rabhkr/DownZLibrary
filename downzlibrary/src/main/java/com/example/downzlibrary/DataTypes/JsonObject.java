package com.example.downzlibrary.DataTypes;

import com.example.downzlibrary.DownZ;
import com.example.downzlibrary.ListnerInterface.HttpListener;
import com.example.downzlibrary.Parameters.HeaderParams;
import com.example.downzlibrary.Parameters.RequestParams;
import com.example.downzlibrary.RequestTasks.JsonObjectTask;
import com.example.downzlibrary.Utilities.CacheManagerInterface;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by saurabhkumar on 06/08/17.
 */

public class JsonObject extends Type<JSONObject> {
    private String url;
    private HttpListener<JSONObject> mListener;
    private DownZ.Method method;
    private ArrayList<RequestParams> params;
    private ArrayList<HeaderParams> headers;
    private JsonObjectTask mTask;
    private CacheManagerInterface<JSONObject> mCacheManager;

    /**
     * Constructor to load json datatyes
     */
    public JsonObject(DownZ.Method m, String url, ArrayList<RequestParams> params, ArrayList<HeaderParams> headers) {
        this.url = url;
        this.method = m;
        this.headers = headers;
        this.params = params;
    }

    /**
     * Sets future callback
     */

    public JsonObject setCallback(HttpListener<JSONObject> listener) {
        this.mListener = listener;
        mListener.onRequest();
        JSONObject data;
        if (mCacheManager != null) {
            data = mCacheManager.getDataFromCache(url);
            if (data != null) {
                mListener.onResponse(data);
                return this;
            }
        }

        mTask = new JsonObjectTask(method, url, params, headers, mListener);
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
                mListener.onCancel();
                return true;
            } else {
                return false;
            }
        }

        return false;
    }


    public JsonObject setCacheManager(CacheManagerInterface<JSONObject> cache) {
        this.mCacheManager = cache;
        return this;
    }


}
