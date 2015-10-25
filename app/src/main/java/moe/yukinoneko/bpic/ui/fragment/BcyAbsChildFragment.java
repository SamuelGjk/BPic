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


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import moe.yukinoneko.bpic.model.StatusModel;
import moe.yukinoneko.bpic.support.Constants;
import moe.yukinoneko.bpic.support.adapter.HomeListAdapter;
import moe.yukinoneko.bpic.ui.activity.BcyDetailActivity;
import moe.yukinoneko.bpic.ui.fragment.base.ChildBaseFragment;

/**
 * Created by Samuel.Alva on 2015/6/7.
 */
public abstract class BcyAbsChildFragment extends ChildBaseFragment {
    protected boolean hasAvatar;

    @Override
    protected List<StatusModel> responseDeal(String response) {
        Document doc = Jsoup.parse(response);
        Elements detailUrls = doc.select("div.work-thumbnail__bd > a");
        Elements avatars = doc.getElementsByAttributeValue("class", "i-work-thumbnail__uava");
        Elements authors = doc.getElementsByAttributeValue("class", "work-thumbnail__author");

        if (!hasAvatar) {
            Element pageItem = doc.getElementsByAttributeValue("class", "pager__item pager__item--is-cur pager__item--disabled").first();
            String strTotalPage = pageItem.getElementsByTag("span").first().html();
            totalPage = Math.ceil(Double.parseDouble(strTotalPage.substring(strTotalPage.indexOf("共") + 1, strTotalPage.lastIndexOf("篇"))) / 60);
        }

        int index = 0;
        List<StatusModel> data = new ArrayList<>();
        for (Element e : detailUrls) {
            StatusModel model = new StatusModel();
            model.detail = e.attr("href");
            model.cover = e.getElementsByTag("img").first().attr("src");
            if (hasAvatar) {
                String avatar = avatars.get(index).getElementsByTag("img").first().attr("src");
                if (avatar.startsWith("/Public")) {
                    avatar = Constants.BASE_API_BCY + avatar;
                }
                model.avatar = avatar.replace("middle", "big");
            }
            model.author = authors.get(index).html();
            data.add(model);
            index++;
        }
        return data;
    }

    @Override
    protected HomeListAdapter getAdapter() {
        return new HomeListAdapter(mContext, mData, BcyDetailActivity.class);
    }
}
