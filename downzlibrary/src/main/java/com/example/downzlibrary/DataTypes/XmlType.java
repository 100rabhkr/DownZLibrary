package com.example.downzlibrary.DataTypes;

import android.graphics.Bitmap;
import android.util.Xml;

import com.example.downzlibrary.DownZ;
import com.example.downzlibrary.ListnerInterface.HttpListener;
import com.example.downzlibrary.Parameters.HeaderParams;
import com.example.downzlibrary.Parameters.RequestParams;
import com.example.downzlibrary.RequestTasks.BitMapTask;
import com.example.downzlibrary.RequestTasks.XmlTask;
import com.example.downzlibrary.Utilities.CacheManagerInterface;

import org.w3c.dom.Document;

import java.util.ArrayList;

/**
 * Created by saurabhkumar on 09/08/17.
 */

public class XmlType extends Type<Document> {
    private String url;
    private HttpListener<Document> listener;
    private DownZ.Method method;
    private ArrayList<RequestParams> params;
    private ArrayList<HeaderParams> headers;
    private XmlTask mTask;
    private CacheManagerInterface<Document> mCacheManager;

    public XmlType(DownZ.Method m, String url, ArrayList<RequestParams> params, ArrayList<HeaderParams> headers) {
        this.url = url;
        this.method = m;
        this.headers = headers;
        this.params = params;
    }

    public XmlType setCallback(HttpListener<Document> listener) {
        this.listener = listener;
        this.listener.onRequest();
        Document data;
        if (mCacheManager != null) {
            data = mCacheManager.getDataFromCache(url);
            if (data != null) {
                this.listener.onResponse(data);
                return this;
            }
        }

        mTask = new XmlTask(method, url, params, headers, this.listener);
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
    public XmlType setCacheManager(CacheManagerInterface<Document> cache) {
        this.mCacheManager = cache;
        return this;
    }
}
