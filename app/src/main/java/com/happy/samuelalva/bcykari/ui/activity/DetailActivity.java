package com.happy.samuelalva.bcykari.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.happy.samuelalva.bcykari.R;
import com.happy.samuelalva.bcykari.support.Constants;
import com.happy.samuelalva.bcykari.support.Utility;
import com.happy.samuelalva.bcykari.support.adapter.DetailListAdapter;
import com.happy.samuelalva.bcykari.support.http.PicHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samuel.Alva on 2015/4/16.
 */
public class DetailActivity extends AppCompatActivity {
    public static final String DETAIL_URL = "DETAIL_URL";

    private CollapsingToolbarLayout mCollapsingToolbar;
    private SimpleDraweeView mBackdrop;

    private DetailListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setSupportActionBar((Toolbar) findViewById(R.id.detail_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View mStatusBarTintView = findViewById(R.id.status_bar_tint_view);
        ViewGroup.LayoutParams p = mStatusBarTintView.getLayoutParams();
        p.height = Utility.getStatusBarHeight(this);
        mStatusBarTintView.setLayoutParams(p);

        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mBackdrop = (SimpleDraweeView) findViewById(R.id.iv_backdrop);

        RecyclerView mList = (RecyclerView) findViewById(R.id.rv_detail);

        Intent intent = getIntent();
        int hostType = intent.getIntExtra(Constants.HOST_TYPE, PicHttpClient.BCY);
        String detailUrl = intent.getStringExtra(DETAIL_URL);

        mList.setLayoutManager(new GridLayoutManager(this, 2));
        mList.setHasFixedSize(true);
        mList.setAdapter(mAdapter = new DetailListAdapter(this, hostType));

        if (Utility.readNetworkState(this)) {
            if (hostType == PicHttpClient.BCY) {
                PicHttpClient.get(Constants.BASE_API_BCY + detailUrl, handler, hostType);
            }
        } else {
            Utility.showToastForNoNetwork(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private TextHttpResponseHandler handler = new TextHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            Document doc = Jsoup.parse(responseString);
            mCollapsingToolbar.setTitle(doc.getElementsByAttributeValue("class", "fz14 blue1").first().html());
            Elements elements = doc.getElementsByAttributeValue("class", "detail_std detail_clickable");
            List<String> data = new ArrayList<>();
            for (Element e : elements) {
                data.add(e.attr("src").replace("/w650", ""));
            }
            mBackdrop.setImageURI(Uri.parse(data.get(0) + "/w650"));
            mAdapter.addAll(data);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            Utility.showToastForLoadFailure(DetailActivity.this);
        }
    };
}
