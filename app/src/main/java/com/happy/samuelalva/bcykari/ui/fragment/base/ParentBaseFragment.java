package com.happy.samuelalva.bcykari.ui.fragment.base;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.happy.samuelalva.bcykari.R;
import com.happy.samuelalva.bcykari.support.adapter.base.BasePagerAdapter;
import com.happy.samuelalva.bcykari.ui.activity.MainActivity;

/**
 * Created by Samuel.Alva on 2015/5/4.
 */
public abstract class ParentBaseFragment extends Fragment implements MainActivity.Refresher {

    protected TabLayout mTabLayout;
    protected ViewPager mPager;
    protected BasePagerAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base_parent, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTabLayout = (TabLayout) View.inflate(getActivity(), R.layout.tabs_layout, null);
        mPager = (ViewPager) view.findViewById(R.id.pager);
        mPager.setOffscreenPageLimit(3);
        mPager.setAdapter(mAdapter = getAdapter());
        mTabLayout.setupWithViewPager(mPager);
    }

    protected abstract BasePagerAdapter getAdapter();

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (getActivity() instanceof MainActivity) {
                FrameLayout tabContainer = ((MainActivity) getActivity()).getTabContainer();
                tabContainer.removeAllViews();
                tabContainer.addView(mTabLayout);
            }
        }
    }

    @Override
    public void doRefresh() {
        Fragment f = mAdapter.getItemAt(mPager.getCurrentItem());
        if (f instanceof ChildBaseFragment)
            ((ChildBaseFragment) f).doRefresh();
    }
}
