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
import com.happy.samuelalva.bcykari.support.Utility;
import com.happy.samuelalva.bcykari.support.adapter.HomeListAdapter;
import com.happy.samuelalva.bcykari.support.http.BcyHttpClient;
import com.happy.samuelalva.bcykari.ui.activity.MainActivity;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.util.List;

/**
 * Created by Administrator on 2015/4/17.
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

    protected int mHeaderHeight;

    protected MainActivity parentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base_child, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentActivity = (MainActivity) getActivity();
        mHeaderHeight = Utility.getActionBarHeight(parentActivity);
        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefresh.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
        mSwipeRefresh.setProgressViewOffset(false, 0, (int) ((mHeaderHeight + Utility.dp2px(parentActivity, 36)) * 1.2));
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

                int mTranslationY = getmTranslationY();
                if (dy > 0 && !mSwipeRefresh.isRefreshing() && mLayoutManager.findLastCompletelyVisibleItemPosition() >= mAdapter.getItemCount() - 1) {
                    doLoad();
                }

                if (((mTranslationY > -mHeaderHeight && dy > 0) || (mTranslationY < 0 && dy < 0)) && mTranslationY != -dy) {
                    mTranslationY -= dy;
                }

                if (mTranslationY < -mHeaderHeight) {
                    mTranslationY = -mHeaderHeight;
                } else if (mTranslationY > 0) {
                    mTranslationY = 0;
                }

                parentActivity.updateHeaderTranslation(mTranslationY);
            }
        });
        mList.setAdapter(mAdapter = getAdapter());

        doRefresh();
    }

    protected void doRefresh() {
        mList.smoothScrollToPosition(0);
        if (Utility.readNetworkState(parentActivity)) {
            isRefresh = true;
            mSwipeRefresh.setRefreshing(true);
            BcyHttpClient.get(requestUrl, handler);
        } else {
            mSwipeRefresh.setRefreshing(false);
            Utility.showToastForNoNetwork(parentActivity);
        }
    }

    protected void doLoad() {
        if (nextPage > totalPage) {
            Toast.makeText(parentActivity, "没有了=-=", Toast.LENGTH_SHORT).show();
        } else if (Utility.readNetworkState(parentActivity)) {
            isRefresh = false;
            mSwipeRefresh.setRefreshing(true);
            BcyHttpClient.get(requestUrl + nextPage, handler);
        } else {
            mSwipeRefresh.setRefreshing(false);
            Utility.showToastForNoNetwork(parentActivity);
        }
    }

    private int getmTranslationY() {
        return parentActivity.getmTranslationY();
    }

    protected abstract List<StatusModel> responseDeal(String response);

    protected abstract HomeListAdapter getAdapter();

    private TextHttpResponseHandler handler = new TextHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, final String responseString) {
            final List<StatusModel> data = responseDeal(responseString);
            mSwipeRefresh.setRefreshing(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isRefresh) {
                        mAdapter.replaceAll(responseDeal(responseString));
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
        }
    };

}
