package com.happy.samuelalva.bcykari.support.http;

import com.happy.samuelalva.bcykari.support.http.base.BaseHttpClient;

/**
 * Created by Samuel.Alva on 2015/5/3.
 */
public class BcyHttpClient extends BaseHttpClient {
    static {
        client.setUserAgent("Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36");
    }
}
