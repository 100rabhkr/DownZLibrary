package com.example.downzlibrary;

import android.content.Context;

import com.example.downzlibrary.DataTypes.BitMap;
import com.example.downzlibrary.DataTypes.JsonArray;
import com.example.downzlibrary.DataTypes.JsonObject;
import com.example.downzlibrary.Parameters.HeaderParams;
import com.example.downzlibrary.Parameters.RequestParams;

import java.util.ArrayList;

/**
 * Created by saurabhkumar on 06/08/17.
 */

public class DownZ {
    private static DownZ instance = null;
    private Context context;
    private String url;
    private Method method;
    private ArrayList<RequestParams> params = new ArrayList<>();
    private ArrayList<HeaderParams> headers = new ArrayList<>();

    /**
     * Constructor
     *
     * @param c it is the context
     */
    public DownZ(Context c) {
        this.context = c;
    }

    public static DownZ from(Context c) {
        return getInstance(c);
    }

    /**
     * Returns singleton instance for network call
     *
     * @param context it is the context of activity
     */
    public static DownZ getInstance(Context context) {
        if (context == null)
            throw new NullPointerException("Error");


        synchronized (DownZ.class) {
            if (instance == null)
                instance = new DownZ(context);
        }

        return instance;
    }

    /**
     * Assigns Url to be loaded
     *
     * @param m, url
     * @return instance
     */
    public DownZ load(Method m, String url) {
        this.url = url;
        this.method = m;
        return this;
    }

    /**
     * Sets json datatype for request
     *
     * @return Json Type
     */
    public JsonObject asJsonObject() {
        return new JsonObject(method, url, params, headers);
    }

    /**
     * Sets json datatype for request
     *
     * @return Json Array Type
     */
    public JsonArray asJsonArray() {
        return new JsonArray(method, url, params, headers);
    }

    /**
     * Sets bitmap type for request
     *
     * @return Bitmap Type
     */

    public BitMap asBitmap() {
        return new BitMap(method, url, params, headers);
    }

    /**
     * Sets request body parameters
     *
     * @param key   Parameter key
     * @param value Parameter value
     * @return DownZ instance
     */

    public DownZ setRequestParameter(String key, String value) {
        RequestParams pram = new RequestParams();
        pram.setKey(key);
        pram.setValue(value);
        this.params.add(pram);
        return this;
    }

    /**
     * Sets request header parameters
     *
     * @param key   Parameter key
     * @param value Parameter value
     * @return DownZ instance
     */

    public DownZ setHeaderParameter(String key, String value) {
        HeaderParams pram = new HeaderParams();
        pram.setKey(key);
        pram.setValue(value);
        this.headers.add(pram);
        return this;
    }


    public static enum Method {
        GET,
        POST,
        PUT,
        DELETE
    }


}
