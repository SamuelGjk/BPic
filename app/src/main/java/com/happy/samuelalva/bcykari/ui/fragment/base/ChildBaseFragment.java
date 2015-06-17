package com.happy.samuelalva.bcykari.ui.fragment.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.happy.samuelalva.bcykari.R;
import com.happy.samuelalva.bcykari.model.StatusModel;
import com.happy.samuelalva.bcykari.receiver.ConnectivityReceiver;
import com.happy.samuelalva.bcykari.support.Utility;
import com.happy.samuelalva.bcykari.support.adapter.HomeListAdapter;
import com.happy.samuelalva.bcykari.ui.activity.MainActivity;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.util.List;

/**
 * Created by Samuel.Alva on 2015/4/17.
 */
public abstract class ChildBaseFragment extends Fragment {
    protected String requestUrl;

    protected RecyclerView mList;
    protected SwipeRefreshLayout mSwipeRefresh;
    protected HomeListAdapter mAdapter;
    protected GridLayoutManager mLayoutManager;

    protected boolean isRefresh;
    protected int nextPage = 2;
    protected double totalPage;

    protected MainActivity parentActivity;

    private String noMoreStr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        noMoreStr = getResources().getString(R.string.no_more);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base_child, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentActivity = (MainActivity) getActivity();
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

                if (dy < 0) {
                    parentActivity.showFab();
                } else {
                    parentActivity.hideFab();
                }

                if (dy > 0 && !mSwipeRefresh.isRefreshing() && mLayoutManager.findLastCompletelyVisibleItemPosition() >= mAdapter.getItemCount() - 1) {
                    doLoad();
                }
            }
        });
        mList.setAdapter(mAdapter = getAdapter());

        doRefresh();
    }

    protected void doRefresh() {
        mList.smoothScrollToPosition(0);
        if (ConnectivityReceiver.readNetworkState(parentActivity)) {
            isRefresh = true;
            mSwipeRefresh.setRefreshing(true);
            doRequest(requestUrl, handler);
        } else {
            mSwipeRefresh.setRefreshing(false);
            Utility.showToastForNoNetwork(parentActivity);
        }
    }

    protected void doLoad() {
        if (nextPage > totalPage) {
            Toast.makeText(parentActivity, noMoreStr, Toast.LENGTH_SHORT).show();
        } else if (ConnectivityReceiver.readNetworkState(parentActivity)) {
            isRefresh = false;
            mSwipeRefresh.setRefreshing(true);
            doRequest(requestUrl + nextPage, handler);
        } else {
            mSwipeRefresh.setRefreshing(false);
            Utility.showToastForNoNetwork(parentActivity);
        }
    }

    protected abstract List<StatusModel> responseDeal(String response);

    protected abstract HomeListAdapter getAdapter();

    protected abstract void doRequest(String url, AsyncHttpResponseHandler handler);

    private TextHttpResponseHandler handler = new TextHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, final String responseString) {
            final List<StatusModel> data = responseDeal(responseString);
            mSwipeRefresh.setRefreshing(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isRefresh) {
                        mAdapter.replaceAll(data);
                        nextPage = 2;
                    } else {
                        mAdapter.addAll(data);
                        nextPage++;
                    }
                }
            }, 200);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            mSwipeRefresh.setRefreshing(false);
            Utility.showToastForLoadFailure(parentActivity);
        }
    };

}
