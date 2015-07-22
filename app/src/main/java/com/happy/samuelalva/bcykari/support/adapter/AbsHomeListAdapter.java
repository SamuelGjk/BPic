package com.happy.samuelalva.bcykari.support.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.happy.samuelalva.bcykari.R;
import com.happy.samuelalva.bcykari.model.StatusModel;
import com.happy.samuelalva.bcykari.receiver.ConnectivityReceiver;
import com.happy.samuelalva.bcykari.support.Utility;
import com.happy.samuelalva.bcykari.support.adapter.base.BaseRecyclerViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Samuel.Alva on 2015/4/15.
 */
public abstract class AbsHomeListAdapter extends BaseRecyclerViewAdapter<StatusModel> {
    private final String TAG = AbsHomeListAdapter.class.getSimpleName();

    private Random random;
    private int lastPosition = -1;
    private boolean hasAvatar;

    public AbsHomeListAdapter(Context context, boolean hasAvatar) {
        super(context);

        this.hasAvatar = hasAvatar;
        random = new Random();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        ((ItemViewHolder) holder).getView(R.id.list_item_layout).clearAnimation();
    }

    @Override
    public int getItemResource() {
        return R.layout.home_list_item;
    }

    @Override
    public void doBindViewHolder(ItemViewHolder holder, final int position) {
        View card = holder.getView(R.id.card_view);
        View itemLayout = holder.getView(R.id.list_item_layout);
        CircleImageView avatar = holder.getView(R.id.avatar);
        ImageView cover = holder.getView(R.id.cover);
        TextView author = holder.getView(R.id.author);
        if (hasAvatar) {
            avatar.setVisibility(View.VISIBLE);
            Picasso.with(context).load(data.get(position).avatar).placeholder(android.R.color.darker_gray).into(avatar);
        }

        Picasso.with(context).load(data.get(position).cover).placeholder(android.R.color.darker_gray).into(cover);
        author.setText(data.get(position).author);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectivityReceiver.isConnected) {
                    doOnClick(data.get(position));
                } else {
                    Utility.showToast(context, context.getString(R.string.no_network));
                }
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

    protected abstract void doOnClick(StatusModel model);

}
