package com.happy.samuelalva.bcykari.ui.activity.base;

import android.content.Intent;
import android.graphics.Bitmap;
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

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.happy.samuelalva.bcykari.R;
import com.happy.samuelalva.bcykari.model.StatusModel;
import com.happy.samuelalva.bcykari.support.Utility;
import com.happy.samuelalva.bcykari.support.adapter.DetailListAdapter;
import com.happy.samuelalva.bcykari.support.image.FastBlur;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by Samuel.Alva on 2015/4/16.
 */
public abstract class BaseDetailActivity extends AppCompatActivity {
    public static final String ENTITY = "ENTITY";

    private CollapsingToolbarLayout mCollapsingToolbar;
    private SimpleDraweeView mBackdrop;

    protected DetailListAdapter mAdapter;
    protected StatusModel model;

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
        model = intent.getParcelableExtra(ENTITY);

        mList.setLayoutManager(new GridLayoutManager(this, 2));
        mList.setHasFixedSize(true);
        mList.setAdapter(mAdapter = getAdapter());

        if (Utility.readNetworkState(this)) {
            doRequest(model.detail, handler);
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
            String avatar = model.avatar;
            if (avatar == null) {
                avatar = doc.select("a._avatar > img").first().attr("src");
            }
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(avatar)).setPostprocessor(new BasePostprocessor() {
                @Override
                public String getName() {
                    return "blurPostprocessor";
                }

                @Override
                public void process(Bitmap bitmap) {
                    FastBlur.doBlur(bitmap, 2, true);
                }
            }).build();
            PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder().setImageRequest(request).setOldController(mBackdrop.getController()).build();
            mBackdrop.setController(controller);
            mCollapsingToolbar.setTitle(model.author);
            updateData(doc);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            Utility.showToastForLoadFailure(BaseDetailActivity.this);
        }
    };

    protected abstract DetailListAdapter getAdapter();

    protected abstract void doRequest(String url, AsyncHttpResponseHandler handler);

    protected abstract void updateData(Document doc);
}
