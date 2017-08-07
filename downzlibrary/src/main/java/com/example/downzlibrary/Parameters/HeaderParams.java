package com.example.downzlibrary.Parameters;

/**
 * Created by saurabhkumar on 06/08/17.
 */

public class HeaderParams {
    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public HeaderParams setKey(String key) {
        this.key = key;
        return this;
    }

    public String getValue() {
        return value;
    }

    public HeaderParams setValue(String value) {
        this.value = value;
        return this;
    }
}
