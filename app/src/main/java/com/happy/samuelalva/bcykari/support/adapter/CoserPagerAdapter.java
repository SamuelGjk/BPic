package com.happy.samuelalva.bcykari.support.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.happy.samuelalva.bcykari.support.adapter.base.BasePagerAdapter;
import com.happy.samuelalva.bcykari.ui.fragment.AllWorkFragment;
import com.happy.samuelalva.bcykari.ui.fragment.CoserTopPostFragment;

/**
 * Created by Samuel.Alva on 2015/5/6.
 */
public class CoserPagerAdapter extends BasePagerAdapter {
    public CoserPagerAdapter(FragmentManager fm) {
        super(fm);
        titles = new String[]{"正片", "本周热门"};
    }

    @Override
    protected Fragment createItem(int position) {
        switch (position) {
            case 0:
                return new AllWorkFragment();
            case 1:
                return new CoserTopPostFragment();

            default:
                return null;
        }
    }
}
