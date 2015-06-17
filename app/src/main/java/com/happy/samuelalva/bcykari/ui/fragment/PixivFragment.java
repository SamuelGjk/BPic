package com.happy.samuelalva.bcykari.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.happy.samuelalva.bcykari.R;
import com.happy.samuelalva.bcykari.support.adapter.PixivPagerAdapter;
import com.happy.samuelalva.bcykari.support.adapter.base.BasePagerAdapter;
import com.happy.samuelalva.bcykari.ui.fragment.base.ParentBaseFragment;

/**
 * Created by Samuel.Alva on 2015/5/6.
 */
public class PixivFragment extends ParentBaseFragment {
    private Menu menu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    protected BasePagerAdapter getAdapter() {
        return new PixivPagerAdapter(getChildFragmentManager());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_pixiv, menu);
        this.menu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment f = mAdapter.getItemAt(mPager.getCurrentItem());
        PixivNormalFragment pnf = null;
        if (f instanceof PixivNormalFragment) {
            pnf = (PixivNormalFragment) f;
        }

        if (pnf != null) {
            switch (item.getItemId()) {
                case R.id.menu_the_day_after:
                    pnf.dateChange(1);
                    break;
                case R.id.menu_the_day_before:
                    pnf.dateChange(-1);
                    break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (menu != null)
            menu.setGroupVisible(R.id.menu_date_selector, hidden);
    }
}
