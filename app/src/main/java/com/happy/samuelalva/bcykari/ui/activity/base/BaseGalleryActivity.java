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

package com.happy.samuelalva.bcykari.ui.activity.base;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.happy.samuelalva.bcykari.BPicApplication;
import com.happy.samuelalva.bcykari.R;
import com.happy.samuelalva.bcykari.support.Utility;
import com.happy.samuelalva.bcykari.support.adapter.ImagePagerAdapter;
import com.happy.samuelalva.bcykari.support.image.ImageSaver;

import java.io.File;
import java.util.List;

/**
 * Created by Samuel.Alva on 2015/4/16.
 */
public abstract class BaseGalleryActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    public static final String IMG_URLS = "IMG_URLS";
    public static final String CUR_PAGE = "CUR_PAGE";

    private ViewPager mPager;
    private View mAppBar;
    private ActionBar mActionBar;
    private ImagePagerAdapter mAdapter;

    protected List<String> urls;
    protected File mCacheDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        showSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setNavigationBarColor(getResources().getColor(R.color.translucent));

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setBackgroundResource(R.color.translucent);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        mAppBar = findViewById(R.id.custom_app_bar);

        mPager = (ViewPager) findViewById(R.id.imagePager);
        mPager.setOffscreenPageLimit(2);
        mPager.addOnPageChangeListener(this);

        Intent intent = getIntent();
        urls = intent.getStringArrayListExtra(IMG_URLS);
        int index = intent.getIntExtra(CUR_PAGE, 0);
        mPager.setAdapter(mAdapter = getAdapter());
        mPager.setCurrentItem(index);

        mActionBar.setTitle(String.valueOf(index + 1) + "/" + urls.size());

        mCacheDir = BPicApplication.getImageCacheDir();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_gallery, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_save:
                savePicture();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void hideSystemUI() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void showSystemUI() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public void SystemUIConfig() {
        if (mAppBar.getAlpha() == 0f) {
            ViewCompat.animate(mAppBar).alpha(1f).start();
            showSystemUI();
        } else if (mAppBar.getAlpha() == 1f) {
            ViewCompat.animate(mAppBar).alpha(0f).start();
            hideSystemUI();
        }
    }

    protected abstract ImagePagerAdapter getAdapter();

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mActionBar.setTitle(String.valueOf(position + 1) + "/" + urls.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void savePicture() {
        int position = mPager.getCurrentItem();
        if (mAdapter.isDownloaded(position)) {
            String cacheName = Utility.getCacheName(urls.get(position));
            File file = new File(mCacheDir, cacheName);
            String fileName = cacheName.substring(0, cacheName.lastIndexOf("."));
            ImageSaver.getInstance(this).save(file, fileName);
        } else {
            Utility.showToast(this, "图片正在加载");
        }
    }

    @Override
    protected void onDestroy() {
        mAdapter.stopDownload();
        super.onDestroy();
    }
}
