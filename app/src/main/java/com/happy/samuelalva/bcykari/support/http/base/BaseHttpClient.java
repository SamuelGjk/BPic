package com.happy.samuelalva.bcykari.support.http.base;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Created by SamuelGjk on 2015/8/20.
 */
public class BaseHttpClient {
    protected static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, AsyncHttpResponseHandler responseHandler) {
        client.get(url, responseHandler);
    }

    public static void get(Context context, String url, AsyncHttpResponseHandler responseHandler) {
        client.get(context, url, responseHandler);
    }

    public static void cancel(Context context) {
        client.cancelRequests(context, true);
    }
}
