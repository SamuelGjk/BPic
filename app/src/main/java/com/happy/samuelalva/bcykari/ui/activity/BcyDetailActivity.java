package com.happy.samuelalva.bcykari.ui.activity;

import android.view.View;

import com.happy.samuelalva.bcykari.support.Constants;
import com.happy.samuelalva.bcykari.support.adapter.BcyDetailListAdapter;
import com.happy.samuelalva.bcykari.support.adapter.AbsDetailListAdapter;
import com.happy.samuelalva.bcykari.support.http.BcyHttpClient;
import com.happy.samuelalva.bcykari.ui.activity.base.BaseDetailActivity;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samuel.Alva on 2015/6/15.
 */
public class BcyDetailActivity extends BaseDetailActivity {
    @Override
    protected AbsDetailListAdapter getAdapter() {
        return new BcyDetailListAdapter(this);
    }

    @Override
    protected void doRequest(String url, AsyncHttpResponseHandler handler) {
        BcyHttpClient.get(this, Constants.BASE_API_BCY + url, handler);
    }

//    @Override
//    protected void onDestroy() {
//        BcyHttpClient.cancel(this);
//        super.onDestroy();
//    }

    @Override
    protected void updateData(Document doc) {
        mLoadingProgressBar.setVisibility(View.GONE);
        List<String> data = new ArrayList<>();
        Elements elements = doc.getElementsByAttributeValue("class", "detail_std detail_clickable");
        for (Element e : elements) {
            data.add(e.attr("src").replace("w650", "2X3"));
        }
        mAdapter.replaceAll(data);
    }

    @Override
    protected String getAvatar(Document doc) {
        String avatar = doc.select("a._avatar > img").first().attr("src");
        if (avatar.startsWith("/Public"))
            avatar = Constants.BASE_API_BCY + avatar;
        return avatar;
    }

    @Override
    protected String getTitle(Document doc) {
        return doc.select("h1.js-post-title").first().text();
    }
}
