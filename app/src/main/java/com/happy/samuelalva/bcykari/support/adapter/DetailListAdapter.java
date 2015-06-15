package com.happy.samuelalva.bcykari.support.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.happy.samuelalva.bcykari.R;
import com.happy.samuelalva.bcykari.support.adapter.base.BaseRecyclerAdapter;

import java.util.ArrayList;

/**
 * Created by Samuel.Alva on 2015/4/15.
 */
public abstract class DetailListAdapter extends BaseRecyclerAdapter<String> {

    public DetailListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemResource() {
        return R.layout.detail_list_item;
    }

    @Override
    public void doBindViewHolder(BaseRecyclerAdapter.ItemViewHolder holder, final int position) {
        SimpleDraweeView iv = (SimpleDraweeView) holder.getView(R.id.iv_detail);
        iv.setImageURI(Uri.parse(picUrlDeal(data.get(position))));

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOnClick(position, new ArrayList<>(data));
            }
        });
    }

    protected abstract void doOnClick(int position, ArrayList<String> urls);

    protected abstract String picUrlDeal(String url);
}
