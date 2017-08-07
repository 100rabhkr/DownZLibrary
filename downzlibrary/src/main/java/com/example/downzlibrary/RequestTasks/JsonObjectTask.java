package com.example.downzlibrary.RequestTasks;

import com.example.downzlibrary.DownZ;
import com.example.downzlibrary.ListnerInterface.HttpListener;
import com.example.downzlibrary.Parameters.HeaderParams;
import com.example.downzlibrary.Parameters.RequestParams;
import com.example.downzlibrary.Response;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by saurabhkumar on 06/08/17.
 */

public class JsonObjectTask extends Task<String, Void, JSONObject> {
    private DownZ.Method method;
    private String mUrl;
    private HttpListener<JSONObject> callback;
    private boolean error = false;
    private ArrayList<RequestParams> params;
    private ArrayList<HeaderParams> headers;

    public JsonObjectTask(DownZ.Method method, String url, ArrayList<RequestParams> params, ArrayList<HeaderParams> headers, HttpListener<JSONObject> callback) {
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
    protected JSONObject doInBackground(String... urls) {
        try {
            Response response = makeRequest(mUrl, method, params, headers);
            JSONObject json = new JSONObject(response.getDataAsString());
            if (this.mCacheManager != null) {
                if (this.mCacheManager.getDataFromCache(mUrl) == null)
                    this.mCacheManager.addDataToCache(mUrl, json);
            }
            return json;

        } catch (Exception e) {
            e.printStackTrace();
            error = true;
        }

        return null;
    }

    @Override
    protected void onPostExecute(JSONObject data) {
        super.onPostExecute(data);
        if (!error)
            this.callback.onResponse(data);
        else
            this.callback.onError();
    }

    /**
     * Sometimes users may cancel at almost end, so lets remove if data is in cache
     */
    @Override
    protected void onCancelled() {
        super.onCancelled();
        if (this.mCacheManager != null) {
            this.mCacheManager.removeDataFromCache(mUrl);
        }
    }
}
