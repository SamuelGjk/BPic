package com.happy.samuelalva.bcykari.ui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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
import com.happy.samuelalva.bcykari.support.Utility;
import com.happy.samuelalva.bcykari.ui.fragment.CoserFragment;
import com.happy.samuelalva.bcykari.ui.fragment.IllustFragment;
import com.happy.samuelalva.bcykari.ui.fragment.PixivFragment;
import com.happy.samuelalva.bcykari.ui.fragment.base.ParentBaseFragment;
import com.melnykov.fab.FloatingActionButton;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private static final int ILLUST = 0, COS = 1, PIXIV = 2;
    private Fragment[] mFragments = new Fragment[3];

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mDrawerNavigation;
    private FrameLayout tabContainer;
    private View mNavigationHeader;
    private ImageView mNavigationHeaderImageView;
    private FloatingActionButton mFab;

    private Menu menu;

    private FragmentManager mManager;
    private int curFragment, bcyCurFragment, pixivCurFragment = 2;
    private boolean isPixiv;

    public FrameLayout getTabContainer() {
        return tabContainer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabContainer = (FrameLayout) findViewById(R.id.tab_container);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.darkPrimaryColor);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, 0, 0);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerNavigation = (NavigationView) findViewById(R.id.drawer_navigation);
        mDrawerNavigation.setNavigationItemSelectedListener(this);

        menu = mDrawerNavigation.getMenu();

        mNavigationHeader = findViewById(R.id.navigation_header);
        mNavigationHeaderImageView = (ImageView) findViewById(R.id.iv_navigation_header);
        mNavigationHeader.setOnClickListener(this);

        View btnSettings = findViewById(R.id.btn_settings);
        btnSettings.setOnClickListener(this);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(this);

        mFragments[ILLUST] = new IllustFragment();
        mFragments[COS] = new CoserFragment();
        mFragments[PIXIV] = new PixivFragment();

        mManager = getSupportFragmentManager();
        FragmentTransaction ft = mManager.beginTransaction();
        for (Fragment f : mFragments) {
            ft.add(R.id.content_frame, f);
            ft.hide(f);
        }
        ft.commit();

        selectItem(0);

        if (ConnectivityReceiver.readNetworkState(this) && !ConnectivityReceiver.isWIFI)
            Utility.showToastForMobileData(this);
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

    private void selectItem(int index) {
        FragmentTransaction ft = mManager.beginTransaction();
        for (int i = 0; i < mFragments.length; i++) {
            if (i == index) {
                ft.show(mFragments[i]);
            } else {
                ft.hide(mFragments[i]);
            }
        }
        ft.commit();
        if (index < 2) {
            bcyCurFragment = index;
        } else {
            pixivCurFragment = index;
        }
        curFragment = index;
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    public void showFab() {
        mFab.show();
    }

    public void hideFab() {
        mFab.hide();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigation_item_illust:
                selectItem(ILLUST);
                break;
            case R.id.navigation_item_cos:
                selectItem(COS);
                break;
        }
        menuItem.setChecked(true);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                break;
            case R.id.fab:
                Fragment f = mFragments[curFragment];
                if (f instanceof ParentBaseFragment)
                    ((ParentBaseFragment) f).doRefresh();
                break;
            case R.id.navigation_header:
                if (isPixiv) {
                    isPixiv = false;
                    mNavigationHeader.setBackgroundResource(R.color.bcy_color);
                    mNavigationHeaderImageView.setImageResource(R.mipmap.bcy_header);
                    mDrawerNavigation.setItemTextColor(getResources().getColorStateList(R.color.navigation_item_text_color_bcy));
                    menu.setGroupVisible(R.id.menu_bcy, true);
                    menu.setGroupVisible(R.id.menu_pixiv, false);
                    selectItem(bcyCurFragment);
                } else {
                    isPixiv = true;
                    mNavigationHeader.setBackgroundResource(R.color.pixiv_color);
                    mNavigationHeaderImageView.setImageResource(R.mipmap.pixiv_header);
                    mDrawerNavigation.setItemTextColor(getResources().getColorStateList(R.color.navigation_item_text_color_pixiv));
                    menu.setGroupVisible(R.id.menu_bcy, false);
                    menu.setGroupVisible(R.id.menu_pixiv, true);
                    selectItem(pixivCurFragment);
                }
                break;
        }
    }
}
