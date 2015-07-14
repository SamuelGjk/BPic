package com.happy.samuelalva.bcykari.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.happy.samuelalva.bcykari.support.Constants;

/**
 * Created by Administrator on 2015/6/30.
 */
public class BcyAllFanartFragment extends BcyAbsChildFragment {
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        requestUrl = Constants.ALL_FANART_API_BCY;
        hasAvatar = false;
        super.onViewCreated(view, savedInstanceState);
    }
}
