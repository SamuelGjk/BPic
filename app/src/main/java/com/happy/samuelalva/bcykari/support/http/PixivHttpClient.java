package com.happy.samuelalva.bcykari.support.http;

import com.happy.samuelalva.bcykari.support.Constants;
import com.happy.samuelalva.bcykari.support.http.base.BaseHttpClient;

/**
 * Created by Samuel.Alva on 2015/5/3.
 */
public class PixivHttpClient extends BaseHttpClient {
    static {
        client.addHeader("Referer", Constants.BASE_API_PIXIV);
    }
}
