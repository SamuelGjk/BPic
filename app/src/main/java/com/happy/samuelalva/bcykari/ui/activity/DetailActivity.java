package com.happy.samuelalva.bcykari.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.happy.samuelalva.bcykari.R;
import com.happy.samuelalva.bcykari.support.Constants;
import com.happy.samuelalva.bcykari.support.Utility;
import com.happy.samuelalva.bcykari.support.adapter.ImagePagerAdapter;
import com.happy.samuelalva.bcykari.support.http.BcyHttpClient;
import com.happy.samuelalva.bcykari.support.image.ImageSaver;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by Administrator on 2015/4/16.
 */
public class DetailActivity extends Activity {
    public static final String DETAIL_URL = "DETAIL_URL";

    private ViewPager mPager;
    private View saveBtn;
    private ImagePagerAdapter mAdapter;

    private File mCacheDir;

    private boolean isCos = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mCacheDir = new File(getExternalCacheDir().getPath() + "/image");

        saveBtn = findViewById(R.id.btn_save);

        mPager = (ViewPager) findViewById(R.id.imagePager);
        mPager.setOffscreenPageLimit(2);

        String detailUrl = getIntent().getStringExtra(DETAIL_URL);
        if (detailUrl.contains("coser")) {
            isCos = true;
        }
        if (Utility.readNetworkState(this)) {
            BcyHttpClient.get(Constants.BASE_API + detailUrl, handler);
        } else {
            Utility.showToastForNoNetwork(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BcyHttpClient.cancel(this);
    }

    private TextHttpResponseHandler handler = new TextHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            final List<String> urls = new ArrayList<>();
            if (isCos) {
                Matcher hdCoserMatcher = Constants.HD_COSER_PATTERN.matcher(responseString);
                while (hdCoserMatcher.find()) {
                    String url = hdCoserMatcher.group();
                    urls.add(url.substring(0, url.length() - 5));
                }
            } else {
                Matcher hdCoverMatcher = Constants.HD_COVER_PATTERN.matcher(responseString);
                while (hdCoverMatcher.find()) {
                    urls.add(hdCoverMatcher.group());
                }
            }

            mPager.setAdapter(mAdapter = new ImagePagerAdapter(DetailActivity.this, urls, mCacheDir));
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

}
