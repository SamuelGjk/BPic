package com.happy.samuelalva.bcykari.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.happy.samuelalva.bcykari.support.Constants;

/**
 * Created by Samuel.Alva on 2015/4/19.
 */
public class AllArtWorkFragment extends BcyChildFragment {
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        requestUrl = Constants.ALL_ART_WORK;
        hasAvatar = false;
        super.onViewCreated(view, savedInstanceState);
    }
}
