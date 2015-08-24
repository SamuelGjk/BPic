package com.happy.samuelalva.bcykari.ui.fragment.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happy.samuelalva.bcykari.R;
import com.happy.samuelalva.bcykari.model.StatusModel;
import com.happy.samuelalva.bcykari.receiver.ConnectivityReceiver;
import com.happy.samuelalva.bcykari.support.Utility;
import com.happy.samuelalva.bcykari.support.adapter.HomeListAdapter;
import com.happy.samuelalva.bcykari.support.http.BPicHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samuel.Alva on 2015/4/17.
 */
public abstract class ChildBaseFragment extends Fragment {

    private RecyclerView mList;
    private HomeListAdapter mAdapter;
    private GridLayoutManager mLayoutManager;
    protected SwipeRefreshLayout mSwipeRefresh;

    private boolean clean;
    private int nextPage = 2;
    protected List<StatusModel> mData;
    protected String requestUrl;
    protected double totalPage;
    protected Activity parentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base_child, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentActivity = getActivity();
        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefresh.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }
        });
        mList = (RecyclerView) view.findViewById(R.id.rv_timeline);
        mList.setLayoutManager(mLayoutManager = new GridLayoutManager(parentActivity, 2));
        mList.setHasFixedSize(true);
        mList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0 && !mSwipeRefresh.isRefreshing() && mLayoutManager.findLastCompletelyVisibleItemPosition() >= mAdapter.getItemCount() - 3) {
                    doLoad();
                }
            }
        });
        mData = new ArrayList<>();
        mList.setAdapter(mAdapter = getAdapter());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doRefresh();
            }
        }, 358);
    }

    protected void doRefresh() {
        mSwipeRefresh.setRefreshing(true);
        mList.smoothScrollToPosition(0);
        clean = true;
        doRequest(requestUrl, handler);
    }

    protected void doLoad() {
        if (nextPage > totalPage) {
            showToast(getString(R.string.no_more));
        } else {
            mSwipeRefresh.setRefreshing(true);
            clean = false;
            doRequest(requestUrl + nextPage, handler);
        }
    }

    protected void doRequest(String url, AsyncHttpResponseHandler handler) {
        if (!ConnectivityReceiver.isConnected) {
            mSwipeRefresh.setRefreshing(false);
            showToast(getString(R.string.no_network));
        } else {
            BPicHttpClient.get(parentActivity, url, null, handler);
        }
    }

    protected void showToast(String msg) {
        Utility.showToast(parentActivity, msg);
    }

    private TextHttpResponseHandler handler = new TextHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, final String responseString) {
            mSwipeRefresh.setRefreshing(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    List<StatusModel> data = responseDeal(responseString);
                    if (data.size() != 0) {
                        if (clean) {
                            mData.clear();
                            mData.addAll(data);
                            nextPage = 2;
                        } else {
                            mData.addAll(data);
                            nextPage++;
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }, 210);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            mSwipeRefresh.setRefreshing(false);
            showToast(getString(R.string.load_failed));
        }
    };

    protected abstract List<StatusModel> responseDeal(String response);

    protected abstract HomeListAdapter getAdapter();

}
