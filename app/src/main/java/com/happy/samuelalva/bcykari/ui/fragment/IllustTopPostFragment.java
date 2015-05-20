package com.happy.samuelalva.bcykari.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.happy.samuelalva.bcykari.model.StatusModel;
import com.happy.samuelalva.bcykari.support.Constants;
import com.happy.samuelalva.bcykari.support.adapter.HomeListAdapter;
import com.happy.samuelalva.bcykari.ui.fragment.base.ChildBaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by Administrator on 2015/4/17.
 */
public class IllustTopPostFragment extends ChildBaseFragment {
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        requestUrl = Constants.ILLUST_TOP_POST_100;
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected List<StatusModel> responseDeal(String response) {
        Matcher coverMatcher = Constants.COVER_PATTERN.matcher(response);
        Matcher authorMatcher = Constants.AUTHOR_PATTERN.matcher(response);
        Matcher avatarMatcher = Constants.AVATAR_PATTERN.matcher(response);
        Matcher detailMatcher = Constants.ILLUST_DETAIL_PATTERN.matcher(response);

        List<StatusModel> data = new ArrayList<>();
        while (coverMatcher.find() && authorMatcher.find() && avatarMatcher.find() && detailMatcher.find()) {
            StatusModel model = new StatusModel();
            model.setCover(coverMatcher.group());
            model.setAuthor(authorMatcher.group().substring(authorMatcher.group().indexOf(">") + 1, authorMatcher.group().lastIndexOf("<")));
            if (avatarMatcher.group().startsWith("/Public")) {
                model.setAvatar((Constants.BASE_API + avatarMatcher.group()).replace("middle", "big"));
            } else {
                model.setAvatar(avatarMatcher.group().replace("middle", "big"));
            }
            model.setDetail(detailMatcher.group());
            data.add(model);
        }
        return data;
    }

    @Override
    protected HomeListAdapter getAdapter() {
        return new HomeListAdapter(getActivity(), true);
    }

    @Override
    protected void doLoad() {
        Toast.makeText(getActivity(), "没有更多了", Toast.LENGTH_SHORT).show();
    }
}
