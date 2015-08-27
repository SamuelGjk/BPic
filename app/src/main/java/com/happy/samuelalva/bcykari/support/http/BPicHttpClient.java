package com.happy.samuelalva.bcykari.support.http;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

/**
 * Created by SamuelGjk on 2015/8/20.
 */
public class BPicHttpClient {
    private static AsyncHttpClient client = new AsyncHttpClient();
    static {
        client.setUserAgent("Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36");
    }

    public static void get(Context context, String url, Header[] headers, AsyncHttpResponseHandler responseHandler) {
        client.get(context, url, headers, null, responseHandler);
    }

    public static void cancel(Context context) {
        client.cancelRequests(context, true);
    }
}