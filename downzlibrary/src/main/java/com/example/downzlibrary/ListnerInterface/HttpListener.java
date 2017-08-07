package com.example.downzlibrary.ListnerInterface;

/**
 * Created by saurabhkumar on 06/08/17.
 */

public interface HttpListener<T> {
    /**
     * callback starts
     */
    public void onRequest();

    /**
     * Callback that's fired after response
     *
     * @param data of the type T holds the response
     */
    public void onResponse(T data);

    public void onError();

    public void onCancel();
}
