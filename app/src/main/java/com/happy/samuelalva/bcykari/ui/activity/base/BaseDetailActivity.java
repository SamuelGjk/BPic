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

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.happy.samuelalva.bcykari.R;
import com.happy.samuelalva.bcykari.support.Constants;
import com.happy.samuelalva.bcykari.support.adapter.DetailListAdapter;
import com.happy.samuelalva.bcykari.support.image.FastBlur;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Samuel.Alva on 2015/4/16.
 */
public abstract class BaseDetailActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String DETAIL_URL = "DETAIL_URL";

    private CollapsingToolbarLayout mCollapsingToolbar;
    private ImageView mBackdrop;
    private TextView tvAuthor;
    protected AlertDialog mFailureDialog;
    protected View mLoadingProgressBar, mLoadFailureView;

    protected DetailListAdapter mAdapter;
    protected String detailUrl;
    protected List<String> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View mStatusBarTintView = findViewById(R.id.status_bar_tint_view);
            mStatusBarTintView.setVisibility(View.VISIBLE);
        }

        Intent intent = getIntent();
        detailUrl = intent.getStringExtra(DETAIL_URL);

        mLoadingProgressBar = findViewById(R.id.loading_progress_bar);
        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mBackdrop = (ImageView) findViewById(R.id.iv_backdrop);
        tvAuthor = (TextView) findViewById(R.id.tv_author);
        mLoadFailureView = findViewById(R.id.load_failure_view);

        mLoadFailureView.setOnClickListener(this);

        RecyclerView mList = (RecyclerView) findViewById(R.id.rv_detail);
        mList.setLayoutManager(new GridLayoutManager(this, 2));
        mList.setHasFixedSize(true);
        mData = new ArrayList<>();
        mList.setAdapter(mAdapter = getAdapter());

        initIdErrorDialog();

        doRequest(detailUrl, handler);
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

            String avatar;
            if (getAvatar(doc) != null) {
                avatar = getAvatar(doc);
            } else {
                mFailureDialog.setTitle(R.string.this_work_was_deleted);
                mFailureDialog.show();
                return;
            }
            String title = getTitle(doc);
            String author = getAuthor(doc);

            mCollapsingToolbar.setTitle(TextUtils.isEmpty(title) ? getString(R.string.no_titile) : title);
            tvAuthor.setText(author);

            RequestCreator creator = Picasso.with(BaseDetailActivity.this).load(avatar);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                creator.transform(new Transformation() {
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
                });
            }
            creator.into(mBackdrop);

            updateData(doc);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            if (statusCode == HttpURLConnection.HTTP_NOT_FOUND) {
                mFailureDialog.setTitle(R.string.the_id_does_not_exist);
                mFailureDialog.show();
            } else {
                mLoadingProgressBar.setVisibility(View.GONE);
                mLoadFailureView.setVisibility(View.VISIBLE);
            }
        }
    };

    private void initIdErrorDialog() {
        View v = View.inflate(this, R.layout.dialog_layout_detail_activity, null);
        TextInputLayout mTextInputLayout = (TextInputLayout) v.findViewById(R.id.text_input_layout);
        final AppCompatEditText etPixivId = (AppCompatEditText) v.findViewById(R.id.et_pixiv_id);

        mTextInputLayout.setHint(getString(R.string.re_enter_id));

        mFailureDialog = new AlertDialog.Builder(this).setView(v).setCancelable(false).setPositiveButton(android.R.string.ok, null).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).create();

        mFailureDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                etPixivId.setText(null);
                ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String new_id = etPixivId.getText().toString();
                        if (!TextUtils.isEmpty(new_id)) {
                            mFailureDialog.dismiss();
                            doRequest(Constants.MEMBER_ILLUST_API_PIXIV + new_id, handler);
                        }
                    }
                });
            }
        });

        mFailureDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    finish();
                    mFailureDialog.dismiss();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        mLoadFailureView.setVisibility(View.GONE);
        mLoadingProgressBar.setVisibility(View.VISIBLE);
        doRequest(detailUrl, handler);
    }

    protected abstract void doRequest(String url, AsyncHttpResponseHandler handler);

    protected abstract DetailListAdapter getAdapter();

    protected abstract void updateData(Document doc);

    protected abstract String getAvatar(Document doc);

    protected abstract String getTitle(Document doc);

    protected abstract String getAuthor(Document doc);
}
