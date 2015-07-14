package com.happy.samuelalva.bcykari.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.happy.samuelalva.bcykari.R;
import com.happy.samuelalva.bcykari.model.StatusModel;
import com.happy.samuelalva.bcykari.support.Constants;
import com.happy.samuelalva.bcykari.support.adapter.PixivPagerAdapter;
import com.happy.samuelalva.bcykari.support.adapter.base.BasePagerAdapter;
import com.happy.samuelalva.bcykari.ui.activity.PixivDetailActivity;
import com.happy.samuelalva.bcykari.ui.activity.base.BaseDetailActivity;
import com.happy.samuelalva.bcykari.ui.fragment.base.ParentBaseFragment;

/**
 * Created by Samuel.Alva on 2015/5/6.
 */
public class PixivParentFragment extends ParentBaseFragment implements SearchView.OnQueryTextListener {
    private String searchHint;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchHint = getResources().getString(R.string.search_pixiv_illust_id);
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
        searchView.setQueryHint(searchHint);
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
        StatusModel model = new StatusModel();
        model.detail = Constants.MEMBER_ILLUST_API_PIXIV + query;
        Intent intent = new Intent(getActivity(), PixivDetailActivity.class);
        intent.putExtra(BaseDetailActivity.ENTITY, model);
        startActivity(intent);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
