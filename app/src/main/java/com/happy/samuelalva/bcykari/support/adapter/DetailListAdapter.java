package com.happy.samuelalva.bcykari.support.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.happy.samuelalva.bcykari.R;
import com.happy.samuelalva.bcykari.support.Constants;
import com.happy.samuelalva.bcykari.support.adapter.base.BaseRecyclerAdapter;
import com.happy.samuelalva.bcykari.ui.activity.ImageActivity;

import java.util.ArrayList;

/**
 * Created by Samuel.Alva on 2015/4/15.
 */
public class DetailListAdapter extends BaseRecyclerAdapter<String> {

    public DetailListAdapter(Context context, int hostType) {
        super(context, hostType);
    }

    @Override
    public int getItemResource() {
        return R.layout.detail_list_item;
    }

    @Override
    public void doBindViewHolder(BaseRecyclerAdapter.ItemViewHolder holder, final int position) {
        SimpleDraweeView iv = (SimpleDraweeView) holder.getView(R.id.iv_detail);
        iv.setImageURI(Uri.parse(data.get(position) + "/2X3"));

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ImageActivity.class);
                i.putExtra(Constants.HOST_TYPE, hostType);
                i.putExtra(ImageActivity.CUR_PAGE, position);
                i.putStringArrayListExtra(ImageActivity.IMG_URLS, new ArrayList<>(data));
                context.startActivity(i);
            }
        });
    }
}
