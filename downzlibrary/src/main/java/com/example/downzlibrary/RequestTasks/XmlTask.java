package com.example.downzlibrary.RequestTasks;

import android.util.Xml;

import com.example.downzlibrary.DownZ;
import com.example.downzlibrary.ListnerInterface.HttpListener;
import com.example.downzlibrary.Parameters.HeaderParams;
import com.example.downzlibrary.Parameters.RequestParams;
import com.example.downzlibrary.Response;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by saurabhkumar on 09/08/17.
 */

public class XmlTask extends Task<String, Void, Document> {
    private DownZ.Method method;
    private String mUrl;
    private HttpListener<Document> callback;
    private boolean error = false;
    private ArrayList<RequestParams> params;
    private ArrayList<HeaderParams> headers;

    public XmlTask(DownZ.Method method, String url, ArrayList<RequestParams> params, ArrayList<HeaderParams> headers, HttpListener<Document> callback) {
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
    protected Document doInBackground(String... urls) {
        try {
            Response response = makeRequest(mUrl, method, params, headers);
            String xmlfile = response.getDataAsString();
            Document doc = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(
                            new InputSource(
                                    new StringReader(xmlfile)
                            )
                    );

            if (this.mCacheManager != null) {
                if (this.mCacheManager.getDataFromCache(mUrl) == null)
                    this.mCacheManager.addDataToCache(mUrl, doc);
            }
            return doc;

        } catch (Exception e) {
            e.printStackTrace();
            error = true;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Document data) {
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
