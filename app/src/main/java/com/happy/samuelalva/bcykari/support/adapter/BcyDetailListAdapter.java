package com.happy.samuelalva.bcykari.support.adapter;

import android.content.Context;
import android.content.Intent;

import com.happy.samuelalva.bcykari.ui.activity.BcyImageActivity;
import com.happy.samuelalva.bcykari.ui.activity.base.BaseImageActivity;

import java.util.ArrayList;

/**
 * Created by Samuel.Alva on 2015/6/15.
 */
public class BcyDetailListAdapter extends DetailListAdapter {
    public BcyDetailListAdapter(Context context) {
        super(context);
    }

    @Override
    protected void doOnClick(int position, ArrayList<String> urls) {
        Intent i = new Intent(context, BcyImageActivity.class);
        i.putExtra(BaseImageActivity.CUR_PAGE, position);
        i.putStringArrayListExtra(BaseImageActivity.IMG_URLS, new ArrayList<>(data));
        context.startActivity(i);
    }

    @Override
    protected String picUrlDeal(String url) {
        if (url.contains("coser")) {
            url += "/2X3";
        } else {
            url = url.replace("post", "cover");
        }
        return url;
    }
}
