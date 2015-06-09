package com.happy.samuelalva.bcykari.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.happy.samuelalva.bcykari.support.Constants;
import com.happy.samuelalva.bcykari.support.adapter.HomeListAdapter;
import com.happy.samuelalva.bcykari.support.http.PicHttpClient;

/**
 * Created by Samuel.Alva on 2015/5/6.
 */
public class CoserTopPostFragment extends BcyFragment {
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        requestUrl = Constants.COSER_TOP_POST_100;
        requestHostType = PicHttpClient.BCY;
        hasAvatar = true;
        super.onViewCreated(view, savedInstanceState);
    }

//    @Override
//    protected List<StatusModel> responseDeal(String response) {
//        Document doc = Jsoup.parse(response);
//        Elements detailUrls = doc.select("div.work-thumbnail__bd > a");
//        Elements avatars = doc.getElementsByAttributeValue("class", "i-work-thumbnail__uava");
//        Elements authors = doc.getElementsByAttributeValue("class", "work-thumbnail__author");
//        int index = 0;
//
//        List<StatusModel> data = new ArrayList<>();
//        for (Element e : detailUrls) {
//            StatusModel model = new StatusModel();
//
//            model.setDetail(e.attr("href"));
//            model.setCover(e.getElementsByTag("img").first().attr("src"));
//
//            String avatar = avatars.get(index).getElementsByTag("img").first().attr("src");
//            if (avatar.startsWith("/Public")) {
//                avatar = (Constants.BASE_API_BCY + avatar).replace("middle", "big");
//            }
//
//            model.setAvatar(avatar);
//            model.setAuthor(authors.get(index).html());
//
//            data.add(model);
//
//            index++;
//        }
//        return data;
//    }

    @Override
    protected HomeListAdapter getAdapter() {
        return new HomeListAdapter(getActivity(), true, requestHostType);
    }

    @Override
    protected void doLoad() {
        Toast.makeText(getActivity(), "没有更多了", Toast.LENGTH_SHORT).show();
    }
}
