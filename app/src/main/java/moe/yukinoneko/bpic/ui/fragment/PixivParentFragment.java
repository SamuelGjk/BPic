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

package moe.yukinoneko.bpic.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import moe.yukinoneko.bpic.R;
import moe.yukinoneko.bpic.support.Constants;
import moe.yukinoneko.bpic.support.adapter.PixivPagerAdapter;
import moe.yukinoneko.bpic.support.adapter.base.BasePagerAdapter;
import moe.yukinoneko.bpic.ui.activity.PixivDetailActivity;
import moe.yukinoneko.bpic.ui.activity.base.BaseDetailActivity;
import moe.yukinoneko.bpic.ui.fragment.base.ParentBaseFragment;

/**
 * Created by Samuel.Alva on 2015/5/6.
 */
public class PixivParentFragment extends ParentBaseFragment implements SearchView.OnQueryTextListener {

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
        menu.findItem(R.id.menu_search).setVisible(true);
        menu.setGroupVisible(R.id.menu_group_date_selector, true);

        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
        searchView.setQueryHint(getString(R.string.search_pixiv_illust_id));
        searchView.setOnQueryTextListener(this);
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
    public boolean onQueryTextSubmit(String query) {
        String detail = Constants.MEMBER_ILLUST_API_PIXIV + query;
        Intent intent = new Intent(getActivity(), PixivDetailActivity.class);
        intent.putExtra(BaseDetailActivity.DETAIL_URL, detail);
        startActivity(intent);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
