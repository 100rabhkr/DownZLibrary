package com.example.downzlibrary.Parameters;

/**
 * Created by saurabhkumar on 06/08/17.
 */

public class RequestParams {
    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public RequestParams setKey(String key) {
        this.key = key;
        return this;
    }

    public String getValue() {
        return value;
    }

    public RequestParams setValue(String value) {
        this.value = value;
        return this;
    }
}
