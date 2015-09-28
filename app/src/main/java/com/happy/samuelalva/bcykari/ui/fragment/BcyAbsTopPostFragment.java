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

package com.happy.samuelalva.bcykari.ui.fragment;

import com.happy.samuelalva.bcykari.model.StatusModel;
import com.happy.samuelalva.bcykari.support.Constants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SamuelGjk on 2015/9/27.
 */
public abstract class BcyAbsTopPostFragment extends BcyAbsChildFragment {
    @Override
    protected List<StatusModel> responseDeal(String response) {
        Document doc = Jsoup.parse(response);
        Elements detailUrls = doc.select("div.work-thumbnail__topBd > a");
        Elements avatars = doc.getElementsByAttributeValue("class", "_avatar _avatar--user work-thumbnail__topavatar mr10");
        Elements authors = doc.select("span.fz12 > a");

        int index = 0;
        List<StatusModel> data = new ArrayList<>();
        for (Element e : detailUrls) {
            StatusModel model = new StatusModel();
            model.detail = e.attr("href");
            model.cover = e.getElementsByTag("img").first().attr("src");
            String avatar = avatars.get(index).getElementsByTag("img").first().attr("src");
            if (avatar.startsWith("/Public")) {
                avatar = Constants.BASE_API_BCY + avatar;
            }
            model.avatar = avatar.replace("middle", "big");
            model.author = authors.get(index).html();
            data.add(model);
            index++;
        }
        return data;
    }
}
