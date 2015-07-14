package com.happy.samuelalva.bcykari.ui.activity.base;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.happy.samuelalva.bcykari.R;
import com.happy.samuelalva.bcykari.model.StatusModel;
import com.happy.samuelalva.bcykari.support.Utility;
import com.happy.samuelalva.bcykari.support.adapter.AbsDetailListAdapter;
import com.happy.samuelalva.bcykari.support.image.FastBlur;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;
import com.melnykov.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.apache.http.Header;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by Samuel.Alva on 2015/4/16.
 */
public abstract class BaseDetailActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String ENTITY = "ENTITY";

    private CollapsingToolbarLayout mCollapsingToolbar;
    private ImageView mBackdrop;
    private TextView tvAuthor;

    protected FloatingActionButton mFAB;

    protected AbsDetailListAdapter mAdapter;
    protected StatusModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View mStatusBarTintView = findViewById(R.id.status_bar_tint_view);
        if (Build.VERSION.SDK_INT < 20) {
            mStatusBarTintView.setVisibility(View.GONE);
        }
        ViewGroup.LayoutParams p = mStatusBarTintView.getLayoutParams();
        p.height = Utility.getStatusBarHeight(this);
        mStatusBarTintView.setLayoutParams(p);

        Intent intent = getIntent();
        model = intent.getParcelableExtra(ENTITY);

        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mBackdrop = (ImageView) findViewById(R.id.iv_backdrop);
        tvAuthor = (TextView) findViewById(R.id.tv_author);

        mFAB = (FloatingActionButton) findViewById(R.id.fab);
        mFAB.hide();
        mFAB.setOnClickListener(this);

        RecyclerView mList = (RecyclerView) findViewById(R.id.rv_detail);
        mList.setLayoutManager(new GridLayoutManager(this, 2));
        mList.setHasFixedSize(true);
        mList.setAdapter(mAdapter = getAdapter());

        doRequest(model.detail, handler);
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
            if (model.avatar == null) {
                if (getAvatar(doc) != null) {
                    model.avatar = getAvatar(doc);
                } else {
                    Utility.showToast(BaseDetailActivity.this, "没有获取到数据");
                    finish();
                    return;
                }
            }

            String title = getTitle(doc);
            if (model.author == null) {
                model.author = doc.select("h2.name > a").first().html();
            }

            mCollapsingToolbar.setTitle(TextUtils.isEmpty(title) ? "No Title" : title);
            tvAuthor.setText(model.author);

            Picasso.with(BaseDetailActivity.this).load(model.avatar).transform(new Transformation() {
                @Override
                public Bitmap transform(Bitmap source) {
                    Bitmap bitmap = source.copy(Bitmap.Config.ARGB_8888, true);
                    source.recycle();
                    return FastBlur.doBlur(bitmap, 2, true);
                }

                @Override
                public String key() {
                    return "doBlur";
                }
            }).into(mBackdrop);

            updateData(doc);
            mFAB.hide();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            Utility.showToastForLoadFailure(BaseDetailActivity.this);
            mFAB.setVisibility(View.VISIBLE);
            mFAB.show();
        }
    };

    @Override
    public void onClick(View v) {
        doRequest(model.detail, handler);
    }

    protected abstract AbsDetailListAdapter getAdapter();

    protected abstract void doRequest(String url, AsyncHttpResponseHandler handler);

    protected abstract void updateData(Document doc);

    protected abstract String getAvatar(Document doc);

    protected abstract String getTitle(Document doc);
}
