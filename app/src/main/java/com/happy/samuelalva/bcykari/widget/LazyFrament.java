package com.happy.samuelalva.bcykari.widget;

import android.support.v4.app.Fragment;

/**
 * Created by SamuelGjk on 2015/9/26.
 */
public abstract class LazyFrament extends Fragment {
    private boolean loadCompleted = false;

    protected boolean isVisible = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
        }
    }

    protected void onVisible() {
        if (loadCompleted) return;
        lazyLoad();
        loadCompleted = true;
    }

    protected abstract void lazyLoad();
}
