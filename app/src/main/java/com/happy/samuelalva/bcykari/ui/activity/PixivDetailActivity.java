package com.happy.samuelalva.bcykari.ui.activity;

import com.happy.samuelalva.bcykari.support.Constants;
import com.happy.samuelalva.bcykari.support.Utility;
import com.happy.samuelalva.bcykari.support.adapter.DetailListAdapter;
import com.happy.samuelalva.bcykari.support.adapter.PixivDetailListAdapter;
import com.happy.samuelalva.bcykari.support.http.PixivHttpClient;
import com.happy.samuelalva.bcykari.ui.activity.base.BaseDetailActivity;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samuel.Alva on 2015/6/15.
 */
public class PixivDetailActivity extends BaseDetailActivity {
    @Override
    protected DetailListAdapter getAdapter() {
        return new PixivDetailListAdapter(this);
    }

    @Override
    protected void doRequest(String url, AsyncHttpResponseHandler handler) {
        PixivHttpClient.get(this, Constants.BASE_API_PIXIV + url, handler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PixivHttpClient.cancel(this);
    }

    @Override
    protected void updateData(Document doc) {
        Elements elements = doc.getElementsByAttributeValue("class", "medium-image  _work multiple ");
        final List<String> data = new ArrayList<>();
        if (elements.size() > 0) {
            doRequest(elements.first().attr("href"), new TextHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    Document cDoc = Jsoup.parse(responseString);
                    Elements cElements = cDoc.getElementsByAttributeValue("class", "image ui-scroll-view");
                    for (Element e : cElements) {
                        data.add(e.attr("data-src"));
                    }
                    mAdapter.addAll(data);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Utility.showToastForLoadFailure(PixivDetailActivity.this);
                }
            });
        } else {
            data.add(model.cover.replace("240x480", "1200x1200"));
            mAdapter.addAll(data);
        }
    }
}
