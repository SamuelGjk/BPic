package com.happy.samuelalva.bcykari.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.happy.samuelalva.bcykari.R;
import com.happy.samuelalva.bcykari.support.Constants;
import com.happy.samuelalva.bcykari.support.Utility;
import com.happy.samuelalva.bcykari.support.adapter.ImagePagerAdapter;
import com.happy.samuelalva.bcykari.support.http.PicHttpClient;
import com.happy.samuelalva.bcykari.support.image.ImageSaver;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/4/16.
 */
public class DetailActivity extends Activity implements ViewPager.OnPageChangeListener {
    public static final String DETAIL_URL = "DETAIL_URL";
    public static final String HOST_TYPE = "HOST_TYPE";

    protected final Pattern HD_COSER_PATTERN = Pattern.compile("http://img[0-9].bcyimg.com/coser/[0-9]+/post/[0-9]\\w+/\\w+(\\.jpg|\\.png|\\.jpeg|\\.gif|\\w)/w650");
    protected final Pattern HD_COVER_PATTERN = Pattern.compile("http://img[0-9].bcyimg.com/drawer/[0-9]+/post/\\w+/\\w+(\\.jpg|\\.gif|\\.jpeg|\\.png|\\w)");

    private ViewPager mPager;
    private TextView curPage, totalPage;
    private View saveBtn;

    private Intent intent;
    private int hostType;

    private File mCacheDir;

    private boolean isCos = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mCacheDir = new File(getExternalCacheDir().getPath() + "/image");

        curPage = (TextView) findViewById(R.id.cur_page);
        totalPage = (TextView) findViewById(R.id.total_page);

        saveBtn = findViewById(R.id.btn_save);

        mPager = (ViewPager) findViewById(R.id.imagePager);
        mPager.setOffscreenPageLimit(2);
        mPager.addOnPageChangeListener(this);

        intent = getIntent();
        hostType = intent.getIntExtra(HOST_TYPE, PicHttpClient.BCY);

        String detailUrl = intent.getStringExtra(DETAIL_URL);
        if (detailUrl.contains("coser")) {
            isCos = true;
        }
        if (Utility.readNetworkState(this)) {
            PicHttpClient.get(Constants.BASE_API_BCY + detailUrl, handler, hostType);
        } else {
            Utility.showToastForNoNetwork(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PicHttpClient.cancel(this);
    }

    private TextHttpResponseHandler handler = new TextHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            final List<String> urls = new ArrayList<>();
            if (isCos) {
                Matcher hdCoserMatcher = HD_COSER_PATTERN.matcher(responseString);
                while (hdCoserMatcher.find()) {
                    String url = hdCoserMatcher.group();
                    urls.add(url.substring(0, url.length() - 5));
                }
            } else {
                Matcher hdCoverMatcher = HD_COVER_PATTERN.matcher(responseString);
                while (hdCoverMatcher.find()) {
                    urls.add(hdCoverMatcher.group());
                }
            }

            mPager.setAdapter(new ImagePagerAdapter(DetailActivity.this, urls, mCacheDir, hostType));

            curPage.setText("1");
            totalPage.setText(" / " + urls.size());

            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String cacheName = Utility.getCacheName(urls.get(mPager.getCurrentItem()));
                    File file = new File(mCacheDir, cacheName);
                    if (!file.exists()) {
                        Utility.showToast(DetailActivity.this, "图片正在加载", Toast.LENGTH_SHORT);
                    } else {
                        ImageSaver.getInstance().save(DetailActivity.this, file, cacheName);
                    }
                }
            });
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

        }
    };

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
}
