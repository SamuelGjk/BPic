package com.happy.samuelalva.bcykari.support.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.happy.samuelalva.bcykari.support.adapter.base.BasePagerAdapter;
import com.happy.samuelalva.bcykari.ui.fragment.BcyAllWorkFragment;
import com.happy.samuelalva.bcykari.ui.fragment.BcyCoserTopPostFragment;

/**
 * Created by Samuel.Alva on 2015/5/6.
 */
public class BcyCoserPagerAdapter extends BasePagerAdapter {
    int[] a = new int[]{1, 2, 0x0052, 0x0053, 0x0054};

    public BcyCoserPagerAdapter(FragmentManager fm) {
        super(fm);
        titles = new String[]{"正片", "本周热门"};
    }

    @Override
    protected Fragment createItem(int position) {
        switch (position) {
            case 0:
                return new BcyAllWorkFragment();
            case 1:
                return new BcyCoserTopPostFragment();

            default:
                return null;
        }
    }
}
