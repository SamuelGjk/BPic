package com.happy.samuelalva.bcykari.support.adapter;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.happy.samuelalva.bcykari.R;
import com.happy.samuelalva.bcykari.model.StatusModel;
import com.happy.samuelalva.bcykari.support.adapter.base.BaseRecyclerAdapter;
import com.squareup.picasso.Picasso;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Samuel.Alva on 2015/4/15.
 */
public abstract class HomeListAdapter extends BaseRecyclerAdapter<StatusModel> {
    private Random random;
    private int lastPosition = -1;
    private boolean hasAvatar;

    public HomeListAdapter(Context context, boolean hasAvatar) {
        super(context);

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
        CircleImageView avatar = (CircleImageView) holder.getView(R.id.avatar);
        ImageView cover = (ImageView) holder.getView(R.id.cover);
        TextView author = (TextView) holder.getView(R.id.author);
        if (hasAvatar) {
            avatar.setVisibility(View.VISIBLE);
            Picasso.with(context).load(data.get(position).avatar).placeholder(android.R.color.darker_gray).into(avatar);
        }
        Picasso.with(context).load(data.get(position).cover).placeholder(android.R.color.darker_gray).into(cover);
        author.setText(data.get(position).author);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOnClick(data.get(position));
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
