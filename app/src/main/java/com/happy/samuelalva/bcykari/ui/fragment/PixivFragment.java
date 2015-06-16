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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Samuel.Alva on 2015/5/6.
 */
public class PixivFragment extends ParentBaseFragment {
    private Menu menu;
    private SimpleDateFormat sdf;
    private Calendar curCalendar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sdf = new SimpleDateFormat("yyyyMMdd");
        initCalendar();
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
        String date = null;

        switch (item.getItemId()) {
            case R.id.menu_the_day_after:
                date = getAfterDay();
                break;
            case R.id.menu_the_day_before:
                date = getBeforeDay();
                break;
        }

        Fragment f = mAdapter.getItemAt(mPager.getCurrentItem());
        if (f instanceof PixivNormalFragment)
            ((PixivNormalFragment) f).dateChange(date);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (menu != null)
            menu.setGroupVisible(R.id.menu_date_selector, hidden);
    }

    private void initCalendar() {
        Date now = new Date();
        curCalendar = Calendar.getInstance();
        curCalendar.setTime(now);
        curCalendar.add(Calendar.DATE, -1);
    }

    private String getBeforeDay() {
        curCalendar.add(Calendar.DATE, -1);
        return sdf.format(curCalendar.getTime());
    }

    private String getAfterDay() {
        curCalendar.add(Calendar.DATE, 1);
        return sdf.format(curCalendar.getTime());
    }
}
