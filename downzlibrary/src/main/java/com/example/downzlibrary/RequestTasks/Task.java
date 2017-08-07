package com.example.downzlibrary.RequestTasks;

import android.net.Uri;
import android.os.AsyncTask;

import com.example.downzlibrary.DownZ;
import com.example.downzlibrary.Parameters.HeaderParams;
import com.example.downzlibrary.Parameters.RequestParams;
import com.example.downzlibrary.Response;
import com.example.downzlibrary.Utilities.CacheManagerInterface;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by saurabhkumar on 06/08/17.
 */

public abstract class Task<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    static final int READ_TIMEOUT = 10000;
    static final int TIMEOUT = 15000;
    final String TAG = getClass().getSimpleName();
    protected CacheManagerInterface<Result> mCacheManager;
    HttpURLConnection conn;

    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    protected Response makeRequest(String url, DownZ.Method m, ArrayList<RequestParams> params, ArrayList<HeaderParams> headers) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.

        URL mUrl = new URL(url);
        conn = (HttpURLConnection) mUrl.openConnection();
        conn.setReadTimeout(READ_TIMEOUT);
        conn.setConnectTimeout(TIMEOUT);
         /*  time in milliseconds */

        switch (m) {
            case GET:
                conn.setRequestMethod("GET");
                break;

            case POST:
                conn.setRequestMethod("POST");
                break;

            case PUT:
                conn.setRequestMethod("PUT");
                break;

            case DELETE:
                conn.setRequestMethod("DELETE");
                break;
        }


        conn.setDoInput(true);
        conn.setDoOutput(m != DownZ.Method.GET);


        //write headers if any
        if (headers.size() > 0) {
            for (HeaderParams header : headers) {
                conn.setRequestProperty(header.getKey(), header.getValue());
            }
        }


        Uri.Builder builder = new Uri.Builder();

        //write request parameters
        if (params.size() > 0) {
            for (RequestParams itm : params) {
                builder.appendQueryParameter(itm.getKey(), itm.getValue());
            }

            String query = builder.build().getEncodedQuery();

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
        }


        conn.connect();


        int response = conn.getResponseCode();
        is = conn.getInputStream();

        Response resp = new Response();
        resp.setCode(response);
        resp.setData(is);
        return resp;
    }


    public void setmCachemanager(CacheManagerInterface<Result> cachemanager) {
        this.mCacheManager = cachemanager;
    }


}
