package com.happy.samuelalva.bcykari.support.adapter;

import android.content.Context;
import android.content.Intent;

import com.happy.samuelalva.bcykari.ui.activity.PixivImageActivity;
import com.happy.samuelalva.bcykari.ui.activity.base.BaseImageActivity;

import java.util.ArrayList;

/**
 * Created by Samuel.Alva on 2015/6/15.
 */
public class PixivDetailListAdapter extends AbsDetailListAdapter {
    public PixivDetailListAdapter(Context context) {
        super(context);
    }

    @Override
    protected void doOnClick(int position, ArrayList<String> urls) {
        Intent i = new Intent(context, PixivImageActivity.class);
        i.putExtra(BaseImageActivity.CUR_PAGE, position);
        i.putStringArrayListExtra(BaseImageActivity.IMG_URLS, new ArrayList<>(data));
        context.startActivity(i);
    }
}
