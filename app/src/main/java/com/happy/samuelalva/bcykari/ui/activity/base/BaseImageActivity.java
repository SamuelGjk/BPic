package com.happy.samuelalva.bcykari.ui.activity.base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

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
public abstract class BaseImageActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    public static final String IMG_URLS = "IMG_URLS";
    public static final String CUR_PAGE = "CUR_PAGE";

    private ViewPager mPager;
    private TextView curPage;
    private ImagePagerAdapter mAdapter;

    protected List<String> urls;
    protected File mCacheDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

        mCacheDir = BPicApplication.getImageCacheDir();

        curPage = (TextView) findViewById(R.id.cur_page);
        mPager = (ViewPager) findViewById(R.id.imagePager);

        TextView totalPage = (TextView) findViewById(R.id.total_page);
        View saveBtn = findViewById(R.id.btn_save);

        mPager.setOffscreenPageLimit(2);
        mPager.addOnPageChangeListener(this);

        saveBtn.setOnClickListener(this);

        Intent intent = getIntent();
        urls = intent.getStringArrayListExtra(IMG_URLS);
        int index = intent.getIntExtra(CUR_PAGE, 0);

        mPager.setAdapter(mAdapter = getAdapter());
        mPager.setCurrentItem(index);
        curPage.setText(String.valueOf(index + 1));
        totalPage.setText(String.valueOf(urls.size()));
    }

    protected abstract ImagePagerAdapter getAdapter();

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        curPage.setText(String.valueOf(position + 1));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        String cacheName = Utility.getCacheName(urls.get(mPager.getCurrentItem()));
        File file = new File(mCacheDir, cacheName);
        if (file.exists()) {
            ImageSaver.getInstance(this).save(file, cacheName);
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
