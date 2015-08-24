package com.happy.samuelalva.bcykari.support.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.happy.samuelalva.bcykari.R;
import com.happy.samuelalva.bcykari.receiver.ConnectivityReceiver;
import com.happy.samuelalva.bcykari.support.Utility;
import com.happy.samuelalva.bcykari.ui.activity.base.BaseImageActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SamuelGjk on 2015/8/22.
 */
public class DetailListAdapter extends RecyclerView.Adapter<DetailListAdapter.ViewHolder> {
    private Context mContext;
    private List<String> mList;
    private Class mClass;

    public DetailListAdapter(Context context, List<String> list, Class cls) {
        mContext = context;
        mList = list;
        mClass = cls;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.position = position;
        Picasso.with(mContext).load(mList.get(position)).placeholder(android.R.color.darker_gray).into(holder.iv);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View card;
        ImageView iv;
        int position;

        public ViewHolder(View itemView) {
            super(itemView);
            card = itemView;
            iv = (ImageView) itemView.findViewById(R.id.iv_detail);
            card.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (ConnectivityReceiver.isConnected) {
                Intent i = new Intent(mContext, mClass);
                i.putExtra(BaseImageActivity.CUR_PAGE, position);
                i.putStringArrayListExtra(BaseImageActivity.IMG_URLS, (ArrayList) mList);
                mContext.startActivity(i);
            } else {
                Utility.showToast(mContext, mContext.getString(R.string.no_network));
            }
        }
    }
}
