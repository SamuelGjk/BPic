package com.happy.samuelalva.bcykari.support.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.happy.samuelalva.bcykari.support.adapter.base.BasePagerAdapter;
import com.happy.samuelalva.bcykari.ui.fragment.PixivNormalFragment;

/**
 * Created by Samuel.Alva on 2015/5/6.
 */
public class PixivPagerAdapter extends BasePagerAdapter {
    public PixivPagerAdapter(FragmentManager fm) {
        super(fm);
        titles = new String[]{"全年龄（大概）"};
    }

    @Override
    protected Fragment createItem(int position) {
        switch (position) {
            case 0:
                return new PixivNormalFragment();

            default:
                return null;
        }
    }
}
