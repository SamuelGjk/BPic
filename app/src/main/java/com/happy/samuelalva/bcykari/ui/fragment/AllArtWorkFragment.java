package com.happy.samuelalva.bcykari.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.happy.samuelalva.bcykari.support.Constants;
import com.happy.samuelalva.bcykari.support.adapter.HomeListAdapter;
import com.happy.samuelalva.bcykari.support.http.PicHttpClient;

/**
 * Created by Samuel.Alva on 2015/4/19.
 */
public class AllArtWorkFragment extends BcyFragment {
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        requestUrl = Constants.ALL_ART_WORK;
        requestHostType = PicHttpClient.BCY;
        hasAvatar = false;
        super.onViewCreated(view, savedInstanceState);
    }

//    @Override
//    protected List<StatusModel> responseDeal(String response) {
//        Document doc = Jsoup.parse(response);
//        Elements detailUrls = doc.select("div.work-thumbnail__bd > a");
//        Elements authors = doc.getElementsByAttributeValue("class", "work-thumbnail__author");
//        Element pageItem = doc.getElementsByAttributeValue("class", "pager__item pager__item--is-cur pager__item--disabled").first();
//        String strTotalPage = pageItem.getElementsByTag("span").first().html();
//        totalPage = Math.ceil(Double.parseDouble(strTotalPage.substring(strTotalPage.indexOf("共") + 1, strTotalPage.lastIndexOf("篇"))) / 60);
//        int index = 0;
//
//        List<StatusModel> data = new ArrayList<>();
//        for (Element e : detailUrls) {
//            StatusModel model = new StatusModel();
//
//            model.setDetail(e.attr("href"));
//            model.setCover(e.getElementsByTag("img").first().attr("src"));
//            model.setAuthor(authors.get(index).html());
//            data.add(model);
//            index++;
//        }
//        return data;
//    }

    @Override
    protected HomeListAdapter getAdapter() {
        return new HomeListAdapter(getActivity(), false, requestHostType);
    }
}
