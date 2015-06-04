package com.happy.samuelalva.bcykari.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.happy.samuelalva.bcykari.model.StatusModel;
import com.happy.samuelalva.bcykari.support.Constants;
import com.happy.samuelalva.bcykari.support.adapter.HomeListAdapter;
import com.happy.samuelalva.bcykari.support.http.PicHttpClient;
import com.happy.samuelalva.bcykari.ui.fragment.base.ChildBaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

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
        Matcher coverMatcher = PIXIV_COVER_PATTERN.matcher(response);
        Matcher authorMatcher = PIXIV_AUTHOR_PATTERN.matcher(response);
        Matcher avatarMatcher = PIXIV_AVATAR_PATTERN.matcher(response);

        List<StatusModel> data = new ArrayList<>();
        while (coverMatcher.find() && authorMatcher.find() && avatarMatcher.find()) {
            StatusModel model = new StatusModel();
            model.setCover(coverMatcher.group());

            String temp_author = authorMatcher.group();
            model.setAuthor(temp_author.substring(temp_author.indexOf(">") + 1, temp_author.lastIndexOf("<")));

            String temp_avatar = avatarMatcher.group();
            model.setAvatar(temp_avatar.substring(temp_avatar.indexOf("\"") + 1, temp_avatar.lastIndexOf("\"")));
            data.add(model);
        }
        return data;
    }

    @Override
    protected HomeListAdapter getAdapter() {
        return new HomeListAdapter(getActivity(), true, requestHostType);
    }
}
