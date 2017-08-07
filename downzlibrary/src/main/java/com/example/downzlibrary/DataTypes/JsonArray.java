package com.example.downzlibrary.DataTypes;

import com.example.downzlibrary.DownZ;
import com.example.downzlibrary.ListnerInterface.HttpListener;
import com.example.downzlibrary.Parameters.HeaderParams;
import com.example.downzlibrary.Parameters.RequestParams;
import com.example.downzlibrary.RequestTasks.JsonArrayTask;
import com.example.downzlibrary.Utilities.CacheManagerInterface;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by saurabhkumar on 06/08/17.
 */

public class JsonArray extends Type<JSONArray> {

    private String url;
    private HttpListener<JSONArray> mListener;
    private DownZ.Method method;
    private ArrayList<RequestParams> params;
    private ArrayList<HeaderParams> headers;
    private JsonArrayTask mTask;
    private CacheManagerInterface<JSONArray> mCacheManager;

    /**
     * Constructor to load json datatyes
     *
     * @param url
     * @param params
     * @param headers
     */
    public JsonArray(DownZ.Method m, String url, ArrayList<RequestParams> params, ArrayList<HeaderParams> headers) {
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
    public JsonArray setCallback(HttpListener<JSONArray> listener) {
        this.mListener = listener;
        this.mListener.onRequest();
        JSONArray data;
        if (mCacheManager != null) {
            data = mCacheManager.getDataFromCache(url);
            if (data != null) {
                mListener.onResponse(data);
                return this;
            }
        }

        mTask = new JsonArrayTask(method, url, params, headers, mListener);
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
                mListener.onCancel();
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
    public JsonArray setCacheManager(CacheManagerInterface<JSONArray> cache) {
        this.mCacheManager = cache;
        return this;
    }
//// TODO: 06/08/17 add comments 

}
