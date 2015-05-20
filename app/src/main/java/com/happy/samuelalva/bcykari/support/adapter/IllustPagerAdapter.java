package com.happy.samuelalva.bcykari.support.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.happy.samuelalva.bcykari.support.adapter.base.BasePagerAdapter;
import com.happy.samuelalva.bcykari.ui.fragment.AllArtWorkFragment;
import com.happy.samuelalva.bcykari.ui.fragment.IllustTopPostFragment;

/**
 * Created by Samuel.Alva on 2015/4/19.
 */
public class IllustPagerAdapter extends BasePagerAdapter {

    public IllustPagerAdapter(FragmentManager fm) {
        super(fm);
        titles = new String[]{"原创", "本周热门"};
    }

    @Override
    protected Fragment createItem(int position) {
        switch (position) {
            case 0:
                return new AllArtWorkFragment();
            case 1:
                return new IllustTopPostFragment();

            default:
                return null;
        }
    }
}
