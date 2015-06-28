package com.happy.samuelalva.bcykari.support.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.happy.samuelalva.bcykari.R;
import com.happy.samuelalva.bcykari.support.adapter.base.BaseRecyclerAdapter;
import com.squareup.picasso.Picasso;

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
        ImageView iv = (ImageView) holder.getView(R.id.iv_detail);
        Picasso.with(context).load(picUrlDeal(data.get(position))).placeholder(android.R.color.darker_gray).into(iv);

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
