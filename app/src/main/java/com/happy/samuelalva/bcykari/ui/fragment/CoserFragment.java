package com.happy.samuelalva.bcykari.ui.fragment;

import com.happy.samuelalva.bcykari.support.adapter.CoserPagerAdapter;
import com.happy.samuelalva.bcykari.support.adapter.base.BasePagerAdapter;
import com.happy.samuelalva.bcykari.ui.fragment.base.ParentBaseFragment;

/**
 * Created by Samuel.Alva on 2015/5/6.
 */
public class CoserFragment extends ParentBaseFragment {

    @Override
    protected BasePagerAdapter getAdapter() {
        return new CoserPagerAdapter(getChildFragmentManager());
    }
}
