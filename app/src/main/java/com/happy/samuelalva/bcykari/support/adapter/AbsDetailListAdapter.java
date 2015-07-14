package com.happy.samuelalva.bcykari.support.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.happy.samuelalva.bcykari.R;
import com.happy.samuelalva.bcykari.receiver.ConnectivityReceiver;
import com.happy.samuelalva.bcykari.support.Utility;
import com.happy.samuelalva.bcykari.support.adapter.base.BaseRecyclerAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Samuel.Alva on 2015/4/15.
 */
public abstract class AbsDetailListAdapter extends BaseRecyclerAdapter<String> {
    private final String TAG = AbsDetailListAdapter.class.getSimpleName();

    public AbsDetailListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemResource() {
        return R.layout.detail_list_item;
    }

    @Override
    public void doBindViewHolder(ItemViewHolder holder, final int position) {
        View card = holder.getView(R.id.card_view);
        ImageView iv = holder.getView(R.id.iv_detail);
        Picasso.with(context).load(data.get(position)).placeholder(android.R.color.darker_gray).into(iv);

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectivityReceiver.isConnected) {
                    doOnClick(position, new ArrayList<>(data));
                } else {
                    Utility.showToastForNoNetwork(context);
                }
            }
        });
    }

    protected abstract void doOnClick(int position, ArrayList<String> urls);

}
