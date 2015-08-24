package com.happy.samuelalva.bcykari.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.happy.samuelalva.bcykari.R;
import com.happy.samuelalva.bcykari.model.StatusModel;
import com.happy.samuelalva.bcykari.support.Constants;
import com.happy.samuelalva.bcykari.support.adapter.HomeListAdapter;
import com.happy.samuelalva.bcykari.ui.activity.PixivDetailActivity;
import com.happy.samuelalva.bcykari.ui.fragment.base.ChildBaseFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
            showToast(getString(R.string.ranking_not_updated));
            mSwipeRefresh.setRefreshing(false);
            dateChange(-1);
        }
        return data;
    }

    @Override
    protected HomeListAdapter getAdapter() {
        return new HomeListAdapter(parentActivity, mData, PixivDetailActivity.class);
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
                showToast(getString(R.string.no_after_day));
            } else {
                curCalendar.add(Calendar.DATE, i);
                requestUrl = Constants.getPixivDatlyIllustRankingApi(sdf.format(curCalendar.getTime()));
                doRefresh();
            }
        } else {
            showToast(getString(R.string.wait_a_minute));
        }
    }
}
