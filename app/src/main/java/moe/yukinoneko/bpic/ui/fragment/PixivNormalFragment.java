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

package moe.yukinoneko.bpic.ui.fragment;

import android.os.Bundle;
import android.view.View;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import moe.yukinoneko.bpic.R;
import moe.yukinoneko.bpic.model.StatusModel;
import moe.yukinoneko.bpic.support.Constants;
import moe.yukinoneko.bpic.support.adapter.HomeListAdapter;
import moe.yukinoneko.bpic.ui.activity.PixivDetailActivity;
import moe.yukinoneko.bpic.ui.fragment.base.ChildBaseFragment;

/**
 * Created by Samuel.Alva on 2015/5/6.
 */
public class PixivNormalFragment extends ChildBaseFragment {

    private SimpleDateFormat sdf;
    private Calendar curCalendar;
    private String today;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sdf = new SimpleDateFormat("yyyyMMdd");
        initCalendar();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        requestUrl = Constants.getPixivDatlyIllustRankingApi(today);
        totalPage = 10;
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected List<StatusModel> responseDeal(String response) {
        Document doc = Jsoup.parse(response);
        Elements detailUrls = doc.select("div.ranking-image-item > a");
        Elements covers = doc.select("div._layout-thumbnail > img");
        Elements authors = doc.getElementsByAttributeValue("class", "icon-text");
        Elements avatars = doc.getElementsByAttributeValue("class", "user-icon ui-scroll-view");

        List<StatusModel> data = new ArrayList<>();
        for (int i = 0; i < detailUrls.size(); i++) {
            StatusModel model = new StatusModel();

            model.detail = detailUrls.get(i).attr("href");
            model.cover = covers.get(i).attr("data-src");
            model.author = authors.get(i).html();
            model.avatar = avatars.get(i).attr("data-src");

            data.add(model);
        }
        if (data.size() == 0) {
            showToast(R.string.ranking_not_updated);
            mSwipeRefresh.setRefreshing(false);
            dateChange(-1);
        }
        return data;
    }

    @Override
    protected HomeListAdapter getAdapter() {
        return new HomeListAdapter(mContext, mData, PixivDetailActivity.class);
    }

    private void initCalendar() {
        Date now = new Date();
        curCalendar = Calendar.getInstance();
        curCalendar.setTime(now);
        curCalendar.add(Calendar.DATE, -1);
        today = sdf.format(curCalendar.getTime());
    }

    public void dateChange(int i) {
        if (!mSwipeRefresh.isRefreshing()) {
            if (i == 1 && today.equals(sdf.format(curCalendar.getTime()))) {
                showToast(R.string.no_after_day);
            } else {
                curCalendar.add(Calendar.DATE, i);
                requestUrl = Constants.getPixivDatlyIllustRankingApi(sdf.format(curCalendar.getTime()));
                doRefresh();
            }
        } else {
            showToast(R.string.wait_a_minute);
        }
    }
}
