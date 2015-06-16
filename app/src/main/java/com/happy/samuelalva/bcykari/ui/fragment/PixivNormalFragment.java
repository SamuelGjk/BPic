package com.happy.samuelalva.bcykari.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.happy.samuelalva.bcykari.model.StatusModel;
import com.happy.samuelalva.bcykari.support.Constants;
import com.happy.samuelalva.bcykari.support.adapter.HomeListAdapter;
import com.happy.samuelalva.bcykari.support.adapter.PixivHomeListAdapter;
import com.happy.samuelalva.bcykari.support.http.PixivHttpClient;
import com.happy.samuelalva.bcykari.ui.fragment.base.ChildBaseFragment;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samuel.Alva on 2015/5/6.
 */
public class PixivNormalFragment extends ChildBaseFragment {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        requestUrl = Constants.DAILY_ILLUST_RANKING_PIXIV;
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
        return data;
    }

    @Override
    protected HomeListAdapter getAdapter() {
        return new PixivHomeListAdapter(getActivity(), true);
    }

    @Override
    protected void doRequest(String url, AsyncHttpResponseHandler handler) {
        PixivHttpClient.get(url, handler);
    }

    public void dateChange(String date) {
        requestUrl = requestUrl.replace("&p=", "&date=" + date + "&p=");
        Log.i("requestUrl", requestUrl);
        doRefresh();
    }
}
