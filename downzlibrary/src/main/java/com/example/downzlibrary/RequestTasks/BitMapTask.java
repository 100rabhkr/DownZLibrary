package com.example.downzlibrary.RequestTasks;

import android.graphics.Bitmap;

import com.example.downzlibrary.DownZ;
import com.example.downzlibrary.ListnerInterface.HttpListener;
import com.example.downzlibrary.Parameters.HeaderParams;
import com.example.downzlibrary.Parameters.RequestParams;
import com.example.downzlibrary.Response;

import java.util.ArrayList;

/**
 * Created by saurabhkumar on 06/08/17.
 */

public class BitMapTask extends Task<String, Void, Bitmap> {
    private DownZ.Method method;
    private String mUrl;
    private HttpListener<Bitmap> callback;
    private boolean error = false;
    private ArrayList<RequestParams> params;
    private ArrayList<HeaderParams> headers;

    public BitMapTask(DownZ.Method method, String url, ArrayList<RequestParams> params, ArrayList<HeaderParams> headers, HttpListener<Bitmap> callback) {
        this.mUrl = url;
        this.method = method;
        this.callback = callback;
        this.params = params;
        this.headers = headers;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        try {
            Response response = makeRequest(mUrl, method, params, headers);
            Bitmap bitmap = response.getAsBitmap();
            if (this.mCacheManager != null) {
                if (this.mCacheManager.getDataFromCache(mUrl) == null)
                    this.mCacheManager.addDataToCache(mUrl, bitmap);
            }
            return bitmap;

        } catch (Exception e) {
            e.printStackTrace();
            error = true;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Bitmap data) {
        super.onPostExecute(data);
        if (!error)
            this.callback.onResponse(data);
        else
            this.callback.onError();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if (this.mCacheManager != null) {
            this.mCacheManager.removeDataFromCache(mUrl);
        }

    }

}
