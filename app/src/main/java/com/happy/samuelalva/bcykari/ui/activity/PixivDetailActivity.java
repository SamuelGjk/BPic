/*
 * Copyright 2015 SamuelGjk <samuel.alva@outlook.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.happy.samuelalva.bcykari.ui.activity;

import android.view.View;

import com.happy.samuelalva.bcykari.R;
import com.happy.samuelalva.bcykari.support.Constants;
import com.happy.samuelalva.bcykari.support.adapter.DetailListAdapter;
import com.happy.samuelalva.bcykari.support.http.BPicHttpClient;
import com.happy.samuelalva.bcykari.ui.activity.base.BaseDetailActivity;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Samuel.Alva on 2015/6/15.
 */
public class PixivDetailActivity extends BaseDetailActivity {
    @Override
    protected DetailListAdapter getAdapter() {
        return new DetailListAdapter(this, mData, PixivImageActivity.class);
    }

    @Override
    protected void doRequest(String url, AsyncHttpResponseHandler handler) {
        BPicHttpClient.get(this, Constants.BASE_API_PIXIV + url, null, handler);
    }

    @Override
    protected void updateData(Document doc) {
        Elements elements = doc.getElementsByAttributeValue("class", "medium-image  _work multiple ");
        if (elements.size() > 0) {
            doRequest(elements.first().attr("href"), new TextHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    mLoadingProgressBar.setVisibility(View.GONE);
                    Document childDoc = Jsoup.parse(responseString);
                    Elements cElements = childDoc.getElementsByAttributeValue("class", "image ui-scroll-view");
                    List<String> data = new ArrayList<>();
                    for (Element e : cElements) {
                        data.add(e.attr("data-src").replace("1200x1200", "240x480"));
                    }
                    mData.addAll(data);
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    mLoadFailureView.setVisibility(View.VISIBLE);
                }
            });
        } else {
            Element e = doc.select("a.medium-image > img").first();
            if (e == null) {
                mFailureDialog.setTitle(R.string.r18_is_prohibited);
                mFailureDialog.show();
                return;
            }
            String cover = e.attr("src").replace("600x600", "240x480");
            mLoadingProgressBar.setVisibility(View.GONE);
            mData.add(cover);
            mAdapter.notifyDataSetChanged();
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
        return doc.select("h1.title").get(3).html();
    }

    @Override
    protected String getAuthor(Document doc) {
        return doc.select("h2.name > a").first().html();
    }
}
