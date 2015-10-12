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

package moe.yukinoneko.bpic.ui.activity;

import android.view.View;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import moe.yukinoneko.bpic.support.Constants;
import moe.yukinoneko.bpic.support.adapter.DetailListAdapter;
import moe.yukinoneko.bpic.support.http.BPicHttpClient;
import moe.yukinoneko.bpic.ui.activity.base.BaseDetailActivity;

/**
 * Created by Samuel.Alva on 2015/6/15.
 */
public class BcyDetailActivity extends BaseDetailActivity {
    @Override
    protected DetailListAdapter getAdapter() {
        return new DetailListAdapter(this, mData, BcyGalleryActivity.class);
    }

    @Override
    protected void doRequest(String url, AsyncHttpResponseHandler handler) {
        BPicHttpClient.get(this, Constants.BASE_API_BCY + url, null, handler);
    }

    @Override
    protected void updateData(Document doc) {
        mLoadingProgressBar.setVisibility(View.GONE);
        List<String> data = new ArrayList<>();
        Elements elements = doc.getElementsByAttributeValue("class", "detail_std detail_clickable");
        for (Element e : elements) {
            data.add(e.attr("src").replace("w650", "2X3"));
        }
        mData.addAll(data);
        mAdapter.notifyDataSetChanged();
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
        return doc.select("h1.js-post-title").first().html();
    }

    @Override
    protected String getAuthor(Document doc) {
        return doc.getElementsByAttributeValue("class", "fz14 blue1").first().html();
    }
}
