package com.example.downzlibrary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.DocumentsContract;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static java.lang.System.in;

/**
 * Created by saurabhkumar on 06/08/17.
 */

public class Response {
    private int code;
    private InputStream inputStream;

    public int getCode() {
        return code;
    }

    public Response setCode(int code) {
        this.code = code;
        return this;
    }

    public InputStream getData() {
        return inputStream;
    }

    public Response setData(InputStream data) {
        this.inputStream = data;
        return this;
    }

    /**
     * Reads an InputStream and converts it to a String.
     *
     * @return String
     * @throws IOException
     */
    public String getDataAsString() throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "UTF-8");
        for (; ; ) {
            int i = in.read(buffer, 0, buffer.length);
            if (i < 0)
                break;
            out.append(buffer, 0, i);
        }
        if (inputStream != null) {
            inputStream.close();
        }
        return out.toString();

    }

    /**
     * Converts input Stream to bitmap
     *
     * @return Bitmap
     */
    public Bitmap getAsBitmap() {
        Bitmap bitmap = BitmapFactory.decodeStream(this.inputStream);
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

}


