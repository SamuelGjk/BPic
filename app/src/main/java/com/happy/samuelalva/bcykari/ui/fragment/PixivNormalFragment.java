package com.happy.samuelalva.bcykari.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.happy.samuelalva.bcykari.model.StatusModel;
import com.happy.samuelalva.bcykari.support.Constants;
import com.happy.samuelalva.bcykari.support.adapter.HomeListAdapter;
import com.happy.samuelalva.bcykari.support.http.PicHttpClient;
import com.happy.samuelalva.bcykari.ui.fragment.base.ChildBaseFragment;

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
        requestHostType = PicHttpClient.PIXIV;
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected List<StatusModel> responseDeal(String response) {
        Document doc = Jsoup.parse(response);
        Elements detailUrls = doc.select("div.ranking-image-item > a");
        Elements coverList = doc.select("div._layout-thumbnail > img");
        Elements authorList = doc.getElementsByAttributeValue("class", "icon-text");
        Elements avatarList = doc.getElementsByAttributeValue("class", "user-icon ui-scroll-view");

        Log.i("PIXIV", detailUrls.size() + "," + coverList.size() + "," + authorList.size() + "," + avatarList.size());

        List<StatusModel> data = new ArrayList<>();

        for (int i = 0; i < detailUrls.size(); i++) {
            StatusModel model = new StatusModel();

            model.detail = detailUrls.get(i).attr("href");
            model.cover = coverList.get(i).attr("data-src");
            model.author = authorList.get(i).html();
            model.avatar = avatarList.get(i).attr("data-src");

            data.add(model);

            Log.i("PIXIV", model.avatar);
        }

        return data;

//        Matcher coverMatcher = PIXIV_COVER_PATTERN.matcher(response);
//        Matcher authorMatcher = PIXIV_AUTHOR_PATTERN.matcher(response);
//        Matcher avatarMatcher = PIXIV_AVATAR_PATTERN.matcher(response);
//
//        List<StatusModel> data = new ArrayList<>();
//        while (coverMatcher.find() && authorMatcher.find() && avatarMatcher.find()) {
//            StatusModel model = new StatusModel();
//            model.cover = coverMatcher.group();
//
//            String temp_author = authorMatcher.group();
//            model.author = temp_author.substring(temp_author.indexOf(">") + 1, temp_author.lastIndexOf("<"));
//
//            String temp_avatar = avatarMatcher.group();
//            model.avatar = temp_avatar.substring(temp_avatar.indexOf("\"") + 1, temp_avatar.lastIndexOf("\""));
//            data.add(model);
//        }
    }

    @Override
    protected HomeListAdapter getAdapter() {
        return new HomeListAdapter(getActivity(), requestHostType, true);
    }
}
