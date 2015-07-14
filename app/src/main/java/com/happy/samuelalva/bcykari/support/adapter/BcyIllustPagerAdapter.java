package com.happy.samuelalva.bcykari.support.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.happy.samuelalva.bcykari.support.adapter.base.BasePagerAdapter;
import com.happy.samuelalva.bcykari.ui.fragment.BcyAllArtWorkFragment;
import com.happy.samuelalva.bcykari.ui.fragment.BcyAllFanartFragment;
import com.happy.samuelalva.bcykari.ui.fragment.BcyIllustTopPostFragment;

/**
 * Created by Samuel.Alva on 2015/4/19.
 */
public class BcyIllustPagerAdapter extends BasePagerAdapter {

    public BcyIllustPagerAdapter(FragmentManager fm) {
        super(fm);
        titles = new String[]{"原创", "二次创作", "本周热门"};
    }

    @Override
    protected Fragment createItem(int position) {
        switch (position) {
            case 0:
                return new BcyAllArtWorkFragment();
            case 1:
                return new BcyAllFanartFragment();
            case 2:
                return new BcyIllustTopPostFragment();

            default:
                return null;
        }
    }
}
