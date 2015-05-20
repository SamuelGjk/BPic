package com.happy.samuelalva.bcykari.ui.fragment.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout;
import com.happy.samuelalva.bcykari.R;
import com.happy.samuelalva.bcykari.support.adapter.base.BasePagerAdapter;
import com.happy.samuelalva.bcykari.ui.activity.MainActivity;

/**
 * Created by Samuel.Alva on 2015/5/4.
 */
public abstract class ParentBaseFragment extends Fragment implements MainActivity.Refresher {

    protected SlidingTabLayout mTabLayout;
    protected ViewPager mPager;
    protected BasePagerAdapter mAdapter;

    protected int mLastScrollY;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base_parent, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTabLayout = (SlidingTabLayout) View.inflate(getActivity(), R.layout.tabs_layout, null);
        mTabLayout.setDistributeEvenly(true);
        mTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return Color.WHITE;
            }
        });
        mTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                propagateToolbarState(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPager = (ViewPager) view.findViewById(R.id.pager);
        mPager.setOffscreenPageLimit(3);
        mPager.setAdapter(mAdapter = getAdapter());
        mTabLayout.setViewPager(mPager);
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
                ((MainActivity) getActivity()).updateHeaderTranslation(0);
            }
        }
    }

    private void propagateToolbarState(int position) {
        int mTranslationY = 0;
        // Set scrollY for the active fragments
        for (int i = 0; i < mAdapter.getCount(); i++) {
            // Skip non current item
            if (i != position) {
                continue;
            }

            // Skip destroyed or not created item
            Fragment f = mAdapter.getItemAt(i);
            if (f == null) {
                continue;
            }

            mTranslationY = -((MainActivity) getActivity()).getmTranslationY();

            if (mTranslationY == 0)
                mLastScrollY = 0;

            if (mTranslationY != mLastScrollY && mTranslationY > mLastScrollY) {
                RecyclerView recyclerView = (RecyclerView) f.getView().findViewById(R.id.rv_timeline);
                recyclerView.scrollBy(0, mTranslationY);
                mLastScrollY = mTranslationY;
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
