package com.happy.samuelalva.bcykari.ui.activity;

import com.happy.samuelalva.bcykari.support.Constants;
import com.happy.samuelalva.bcykari.support.Utility;
import com.happy.samuelalva.bcykari.support.adapter.AbsDetailListAdapter;
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
    protected AbsDetailListAdapter getAdapter() {
        return new PixivDetailListAdapter(this);
    }

    @Override
    protected void doRequest(String url, AsyncHttpResponseHandler handler) {
        PixivHttpClient.get(this, Constants.BASE_API_PIXIV + url, handler);
    }

    @Override
    protected void onDestroy() {
        PixivHttpClient.cancel(this);
        super.onDestroy();
    }

    @Override
    protected void updateData(Document doc) {
        Elements elements = doc.getElementsByAttributeValue("class", "medium-image  _work multiple ");
        final List<String> data = new ArrayList<>();
        if (elements.size() > 0) {
            doRequest(elements.first().attr("href"), new TextHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    Document childDoc = Jsoup.parse(responseString);
                    Elements cElements = childDoc.getElementsByAttributeValue("class", "image ui-scroll-view");
                    for (Element e : cElements) {
                        data.add(e.attr("data-src").replace("1200x1200", "240x480"));
                    }
                    mAdapter.replaceAll(data);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Utility.showToastForLoadFailure(PixivDetailActivity.this);
                }
            });
        } else {
            if (model.cover == null) {
                model.cover = doc.select("a.medium-image > img").first().attr("src").replace("600x600", "240x480");
            }
            data.add(model.cover);
            mAdapter.replaceAll(data);
        }
    }

    @Override
    protected String getAvatar(Document doc) {
        Element element = doc.select("div.usericon > a").first();
        if (element != null) {
            return element.getElementsByTag("img").attr("src");
        }
        return null;
    }

    @Override
    protected String getTitle(Document doc) {
        String title = doc.select("h1.title").get(3).text();
        return title;
    }
}
