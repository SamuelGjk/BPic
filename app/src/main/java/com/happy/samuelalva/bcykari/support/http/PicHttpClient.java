package com.happy.samuelalva.bcykari.support.http;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by Samuel.Alva on 2015/5/3.
 */
public class PicHttpClient {
    public static final int BCY = 0, PIXIV = 1;
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, AsyncHttpResponseHandler responseHandler, int type) {
        if (type == BCY) {
            addBcyHeader();
        } else {
            addPixivHeader();
        }
        client.get(url, responseHandler);
    }

    public static void get(Context context, String url, AsyncHttpResponseHandler responseHandler, int type) {
        if (type == BCY) {
            addBcyHeader();
        } else {
            addPixivHeader();
        }
        client.get(context, url, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }

    public static void cancel(Context context) {
        client.cancelRequests(context, true);
    }

    private static void addBcyHeader() {
        client.removeAllHeaders();
        client.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36");
    }

    private static void addPixivHeader() {
        client.removeAllHeaders();
        client.addHeader("Referer", "http://www.pixiv.net/");
    }
}
