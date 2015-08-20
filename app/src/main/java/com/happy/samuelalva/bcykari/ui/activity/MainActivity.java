package com.happy.samuelalva.bcykari.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.happy.samuelalva.bcykari.R;
import com.happy.samuelalva.bcykari.receiver.ConnectivityReceiver;
import com.happy.samuelalva.bcykari.ui.fragment.BcyCoserParentFragment;
import com.happy.samuelalva.bcykari.ui.fragment.BcyIllustParentFragment;
import com.happy.samuelalva.bcykari.ui.fragment.PixivParentFragment;
import com.happy.samuelalva.bcykari.ui.fragment.base.ParentBaseFragment;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private static final int BCY_ILLUST = 0, BCY_COS = 1, PIXIV = 2;

    private BroadcastReceiver mReceiver;

    private Fragment[] mFragments = new Fragment[3];

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mDrawerNavigation;
    private FrameLayout tabContainer;
    private View mNavigationHeader;
    private ImageView mNavigationHeaderImageView;
    private FloatingActionButton mFab;

    private FragmentManager mManager;
    private int curFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mReceiver = new ConnectivityReceiver();
        registerReceiver(mReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabContainer = (FrameLayout) findViewById(R.id.tab_container);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.darkPrimaryColor);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, 0, 0);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerNavigation = (NavigationView) findViewById(R.id.drawer_navigation);
        mDrawerNavigation.setNavigationItemSelectedListener(this);

        mNavigationHeader = findViewById(R.id.navigation_header);
        mNavigationHeaderImageView = (ImageView) findViewById(R.id.iv_navigation_header);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(this);

        mFragments[BCY_ILLUST] = new BcyIllustParentFragment();
        mFragments[BCY_COS] = new BcyCoserParentFragment();
        mFragments[PIXIV] = new PixivParentFragment();

        mManager = getSupportFragmentManager();
        FragmentTransaction ft = mManager.beginTransaction();
        for (Fragment f : mFragments) {
            ft.add(R.id.content_frame, f);
            ft.hide(f);
        }
        ft.commit();

        selectItem(0, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.menu_settings == item.getItemId()) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    private void selectItem(int index, boolean isBcy) {
        FragmentTransaction ft = mManager.beginTransaction();
        for (int i = 0; i < mFragments.length; i++) {
            if (i == index) {
                ft.show(mFragments[i]);
            } else {
                ft.hide(mFragments[i]);
            }
        }
        ft.commit();

        headerChange(isBcy);
        curFragment = index;
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    public void addTab(View tab) {
        tabContainer.addView(tab);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigation_item_bcy_illust:
                selectItem(BCY_ILLUST, true);
                break;
            case R.id.navigation_item_bcy_cos:
                selectItem(BCY_COS, true);
                break;
            case R.id.navigation_item_pixiv_daily_illust:
                selectItem(PIXIV, false);
                break;
        }
        menuItem.setChecked(true);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Fragment f = mFragments[curFragment];
                if (f instanceof ParentBaseFragment)
                    ((ParentBaseFragment) f).doRefresh();
                break;
        }
    }

    private void headerChange(boolean isBcy) {
        mNavigationHeader.setBackgroundResource(isBcy ? R.color.bcy_color : R.color.pixiv_color);
        mNavigationHeaderImageView.setImageResource(isBcy ? R.mipmap.bcy_header : R.mipmap.pixiv_header);
        mDrawerNavigation.setItemTextColor(getResources().getColorStateList(isBcy ? R.color.navigation_item_text_color_bcy : R.color.navigation_item_text_color_pixiv));
    }
}
