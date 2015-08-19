package com.happy.samuelalva.bcykari.ui.fragment.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happy.samuelalva.bcykari.R;
import com.happy.samuelalva.bcykari.model.StatusModel;
import com.happy.samuelalva.bcykari.receiver.ConnectivityReceiver;
import com.happy.samuelalva.bcykari.support.Utility;
import com.happy.samuelalva.bcykari.support.adapter.AbsHomeListAdapter;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.util.List;

/**
 * Created by Samuel.Alva on 2015/4/17.
 */
public abstract class ChildBaseFragment extends Fragment {

    private RecyclerView mList;
    private AbsHomeListAdapter mAdapter;
    private StaggeredGridLayoutManager mLayoutManager;
    protected SwipeRefreshLayout mSwipeRefresh;

    private int[] lastCompleteVisibleItems;
    private boolean replace;
    private int nextPage = 2;
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
        mList.setLayoutManager(mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mList.setHasFixedSize(true);
        mList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                lastCompleteVisibleItems = mLayoutManager.findLastCompletelyVisibleItemPositions(null);
                if (dy > 0 && !mSwipeRefresh.isRefreshing() && Math.max(lastCompleteVisibleItems[0], lastCompleteVisibleItems[1]) >= mAdapter.getItemCount() - 1) {
                    doLoad();
                }
            }
        });
        mList.setAdapter(mAdapter = getAdapter());

        mList.post(new Runnable() {
            @Override
            public void run() {
                doRefresh();
            }
        });
    }

    protected void doRefresh() {
        mList.smoothScrollToPosition(0);
        replace = true;
        mSwipeRefresh.setRefreshing(true);
        doRequest(requestUrl, handler);
    }

    protected void doLoad() {
        if (nextPage > totalPage) {
            showToast(getString(R.string.no_more));
        } else {
            replace = false;
            mSwipeRefresh.setRefreshing(true);
            doRequest(requestUrl + nextPage, handler);
        }
    }

    protected void doRequest(String url, AsyncHttpResponseHandler handler) {
        if (!ConnectivityReceiver.isConnected) {
            mSwipeRefresh.setRefreshing(false);
            showToast(getString(R.string.no_network));
            return;
        }
    }

    protected void showToast(String msg) {
        Utility.showToast(parentActivity, msg);
    }

    private TextHttpResponseHandler handler = new TextHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, final String responseString) {
            final List<StatusModel> data = responseDeal(responseString);
            if (data != null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefresh.setRefreshing(false);
                    }
                }, 358);
                if (replace) {
                    mAdapter.replaceAll(data);
                    nextPage = 2;
                } else {
                    mAdapter.addAll(data);
                    nextPage++;
                }
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            mSwipeRefresh.setRefreshing(false);
            showToast(getString(R.string.load_failed));
        }
    };

    protected abstract List<StatusModel> responseDeal(String response);

    protected abstract AbsHomeListAdapter getAdapter();

}
