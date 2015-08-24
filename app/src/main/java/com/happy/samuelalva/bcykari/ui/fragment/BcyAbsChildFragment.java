package com.happy.samuelalva.bcykari.ui.fragment;

import com.happy.samuelalva.bcykari.model.StatusModel;
import com.happy.samuelalva.bcykari.support.Constants;
import com.happy.samuelalva.bcykari.support.adapter.HomeListAdapter;
import com.happy.samuelalva.bcykari.ui.activity.BcyDetailActivity;
import com.happy.samuelalva.bcykari.ui.fragment.base.ChildBaseFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

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
        return new HomeListAdapter(parentActivity, mData, BcyDetailActivity.class);
    }
}
