package com.happy.samuelalva.bcykari.support.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.happy.samuelalva.bcykari.R;
import com.happy.samuelalva.bcykari.model.StatusModel;
import com.happy.samuelalva.bcykari.support.Constants;
import com.happy.samuelalva.bcykari.support.adapter.base.BaseRecyclerAdapter;
import com.happy.samuelalva.bcykari.ui.activity.DetailActivity;

import java.util.Random;

/**
 * Created by Samuel.Alva on 2015/4/15.
 */
public class HomeListAdapter extends BaseRecyclerAdapter<StatusModel> {
    private Random random;
    private int lastPosition = -1;
    private boolean hasAvatar;

    public HomeListAdapter(Context context, int hostType, boolean hasAvatar) {
        super(context, hostType);

        this.hasAvatar = hasAvatar;
        random = new Random();
    }

    @Override
    public void onViewDetachedFromWindow(BaseRecyclerAdapter.ItemViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        holder.getView(R.id.list_item_layout).clearAnimation();
    }

    @Override
    public int getItemResource() {
        return R.layout.home_list_item;
    }

    @Override
    public void doBindViewHolder(BaseRecyclerAdapter.ItemViewHolder holder, final int position) {
        View card = holder.getView(R.id.card_view);
        View itemLayout = holder.getView(R.id.list_item_layout);
        SimpleDraweeView avatar = (SimpleDraweeView) holder.getView(R.id.avatar);
        SimpleDraweeView cover = (SimpleDraweeView) holder.getView(R.id.cover);
        TextView author = (TextView) holder.getView(R.id.author);
        if (hasAvatar) {
            avatar.setVisibility(View.VISIBLE);
            avatar.setImageURI(Uri.parse(data.get(position).avatar));
        }
        cover.setImageURI(Uri.parse(data.get(position).cover));
        author.setText(data.get(position).author);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetailActivity.class);
                i.putExtra(Constants.HOST_TYPE, hostType);
                i.putExtra(DetailActivity.DETAIL_URL, data.get(position).detail);
                context.startActivity(i);
            }
        });

        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.list_item_into);
            switch (random.nextInt(2)) {
                case 0:
                    animation.setDuration(500);
                    break;
            }
            itemLayout.startAnimation(animation);
            lastPosition = position;
        }
    }

}
