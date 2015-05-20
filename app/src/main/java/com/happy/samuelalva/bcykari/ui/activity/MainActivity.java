package com.happy.samuelalva.bcykari.ui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.happy.samuelalva.bcykari.R;
import com.happy.samuelalva.bcykari.support.Utility;
import com.happy.samuelalva.bcykari.ui.fragment.CoserFragment;
import com.happy.samuelalva.bcykari.ui.fragment.IllustFragment;
import com.melnykov.fab.FloatingActionButton;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Fragment[] mFragments = new Fragment[2];

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private FrameLayout tabContainer;
    private View mHeaderView, btnIllust, btnCos, btnSettings;
    private FloatingActionButton mFab;

    private TypedValue outValue;

    private FragmentManager mManager;
    private int curFragment;

    private int mTranslationY;

    public FrameLayout getTabContainer() {
        return tabContainer;
    }

    public int getmTranslationY() {
        return mTranslationY;
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

        mHeaderView = findViewById(R.id.header_view);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.darkPrimaryColor);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, 0, 0);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        btnIllust = findViewById(R.id.btn_illust);
        btnCos = findViewById(R.id.btn_cos);
        btnSettings = findViewById(R.id.btn_settings);

        outValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);

        btnCos.setBackgroundResource(outValue.resourceId);

        btnIllust.setOnClickListener(this);
        btnCos.setOnClickListener(this);
        btnSettings.setOnClickListener(this);


        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(this);

        mFragments[0] = new IllustFragment();
        mFragments[1] = new CoserFragment();

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

    public void updateHeaderTranslation(int translationY) {
        mHeaderView.setTranslationY(translationY);
        mTranslationY = translationY;
    }

    public void showFab() {
        mFab.show();
    }

    public void hideFab() {
        mFab.hide();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_illust:
                selectItem(0);
                btnIllust.setBackgroundColor(0x10000000);
                btnCos.setBackgroundResource(outValue.resourceId);
                break;
            case R.id.btn_cos:
                selectItem(1);
                btnIllust.setBackgroundResource(outValue.resourceId);
                btnCos.setBackgroundColor(0x10000000);
                break;
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
