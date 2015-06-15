package com.happy.samuelalva.bcykari.support.adapter;

import android.content.Context;
import android.content.Intent;

import com.happy.samuelalva.bcykari.model.StatusModel;
import com.happy.samuelalva.bcykari.ui.activity.BcyDetailActivity;
import com.happy.samuelalva.bcykari.ui.activity.base.BaseDetailActivity;

/**
 * Created by Samuel.Alva on 2015/4/15.
 */
public class BcyHomeListAdapter extends HomeListAdapter {

    public BcyHomeListAdapter(Context context, boolean hasAvatar) {
        super(context, hasAvatar);
    }

    @Override
    protected void doOnClick(StatusModel model) {
        Intent i = new Intent(context, BcyDetailActivity.class);
        i.putExtra(BaseDetailActivity.ENTITY, model);
        context.startActivity(i);
    }
}
