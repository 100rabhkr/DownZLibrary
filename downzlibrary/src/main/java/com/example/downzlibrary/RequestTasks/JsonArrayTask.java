package com.example.downzlibrary.RequestTasks;

import com.example.downzlibrary.DownZ;
import com.example.downzlibrary.ListnerInterface.HttpListener;
import com.example.downzlibrary.Parameters.HeaderParams;
import com.example.downzlibrary.Parameters.RequestParams;
import com.example.downzlibrary.Response;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by saurabhkumar on 06/08/17.
 */

public class JsonArrayTask extends Task<String, Void, JSONArray> {
    private DownZ.Method method;
    private String mUrl;
    private HttpListener<JSONArray> callback;
    private boolean error = false;
    private ArrayList<RequestParams> params;
    private ArrayList<HeaderParams> headers;


    public JsonArrayTask(DownZ.Method method, String url, ArrayList<RequestParams> params, ArrayList<HeaderParams> headers, HttpListener<JSONArray> callback) {
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
    protected JSONArray doInBackground(String... urls) {
        try {

            Response response = makeRequest(mUrl, method, params, headers);
            JSONArray json = new JSONArray(response.getDataAsString());
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
    protected void onPostExecute(JSONArray data) {
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
