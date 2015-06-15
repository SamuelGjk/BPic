package com.happy.samuelalva.bcykari.support.http;

import android.content.Context;

import com.happy.samuelalva.bcykari.support.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by Samuel.Alva on 2015/5/3.
 */
public class PixivHttpClient {
    private static AsyncHttpClient client = new AsyncHttpClient();

    static {
        client.addHeader("Referer", Constants.BASE_API_PIXIV);
    }

    public static void get(String url, AsyncHttpResponseHandler responseHandler) {
        client.get(url, responseHandler);
    }

    public static void get(Context context, String url, AsyncHttpResponseHandler responseHandler) {
        client.get(context, url, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }

    public static void cancel(Context context) {
        client.cancelRequests(context, true);
    }
}
