package com.happy.samuelalva.bcykari.ui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.happy.samuelalva.bcykari.R;
import com.happy.samuelalva.bcykari.support.Utility;
import com.happy.samuelalva.bcykari.ui.fragment.CoserFragment;
import com.happy.samuelalva.bcykari.ui.fragment.IllustFragment;
import com.melnykov.fab.FloatingActionButton;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private static final int ILLUST = 0, COS = 1;
    private Fragment[] mFragments = new Fragment[2];

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private NavigationView mDrawerNavigation;
    private FrameLayout tabContainer;
    private View btnSettings;
    private FloatingActionButton mFab;

    private FragmentManager mManager;
    private int curFragment;

    public FrameLayout getTabContainer() {
        return tabContainer;
    }

    public interface Refresher {
        void doRefresh();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabContainer = (FrameLayout) findViewById(R.id.tab_container);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.darkPrimaryColor);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, 0, 0);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerNavigation = (NavigationView) findViewById(R.id.drawer_navigation);
        mDrawerNavigation.setNavigationItemSelectedListener(this);

        btnSettings = findViewById(R.id.btn_settings);
        btnSettings.setOnClickListener(this);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(this);

        mFragments[ILLUST] = new IllustFragment();
        mFragments[COS] = new CoserFragment();

        mManager = getSupportFragmentManager();
        FragmentTransaction ft = mManager.beginTransaction();
        for (Fragment f : mFragments) {
            ft.add(R.id.content_frame, f);
            ft.hide(f);
        }
        ft.commit();

        selectItem(0);

        if (Utility.readNetworkState(this) && !Utility.isWIFI)
            Utility.showToast(this, "正在使用你那又贵量又少的数据流量，请一定要谨慎(..•˘_˘•..)", Toast.LENGTH_LONG);
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
        curFragment = index;
        mDrawerLayout.closeDrawer(Gravity.START);
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
//        mDrawerNavigation.setItemTextColor(getResources().getColorStateList(R.color.drawer_navigation_item_text_color_bcy));
        mDrawerLayout.closeDrawer(Gravity.START);
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
                if (f instanceof Refresher)
                    ((Refresher) f).doRefresh();
                break;
        }
    }
}
