/*
 * Copyright 2015 SamuelGjk <samuel.alva@outlook.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.happy.samuelalva.bcykari.ui.fragment.base;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happy.samuelalva.bcykari.R;
import com.happy.samuelalva.bcykari.support.adapter.base.BasePagerAdapter;
import com.happy.samuelalva.bcykari.ui.activity.MainActivity;

/**
 * Created by Samuel.Alva on 2015/5/4.
 */
public abstract class ParentBaseFragment extends Fragment {

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
        mPager = (ViewPager) view.findViewById(R.id.pager);
        mTabLayout = (TabLayout) View.inflate(getActivity(), R.layout.tabs_layout, null);

        mPager.setOffscreenPageLimit(3);
        mPager.setAdapter(mAdapter = getAdapter());

        mTabLayout.setupWithViewPager(mPager);

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).addTab(mTabLayout);
        }
    }

    protected abstract BasePagerAdapter getAdapter();

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (mTabLayout != null)
            mTabLayout.setVisibility(hidden ? View.GONE : View.VISIBLE);
    }

    public void doRefresh() {
        Fragment f = mAdapter.getItemAt(mPager.getCurrentItem());
        if (f instanceof ChildBaseFragment)
            ((ChildBaseFragment) f).doRefresh();
    }
}
