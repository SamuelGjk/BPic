package com.happy.samuelalva.bcykari.support.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.happy.samuelalva.bcykari.R;
import com.happy.samuelalva.bcykari.model.StatusModel;
import com.happy.samuelalva.bcykari.ui.activity.DetailActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2015/4/15.
 */
public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ItemViewHolder> {

    private Random random;
    private int lastPosition = -1;

    private Context context;
    private boolean hasAvatar;
    private List<StatusModel> data;
    private int hostType;

    public HomeListAdapter(Context context, boolean hasAvatar, int hostType) {
        this.context = context;
        this.hasAvatar = hasAvatar;
        this.data = new ArrayList<>();
        this.hostType = hostType;
        random = new Random();

        Fresco.initialize(context);
    }

    @Override
    public HomeListAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(View.inflate(context, R.layout.home_list_item, null));
    }

    @Override
    public void onBindViewHolder(HomeListAdapter.ItemViewHolder holder, final int position) {
        if (hasAvatar) {
            holder.avatar.setVisibility(View.VISIBLE);
            holder.avatar.setImageURI(Uri.parse(data.get(position).getAvatar()));
        }
        holder.cover.setImageURI(Uri.parse(data.get(position).getCover()));
        holder.author.setText(data.get(position).getAuthor());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetailActivity.class);
                i.putExtra(DetailActivity.HOST_TYPE, hostType);
                i.putExtra(DetailActivity.DETAIL_URL, data.get(position).getDetail());
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
            holder.itemLayout.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(HomeListAdapter.ItemViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        holder.itemLayout.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addAll(List<StatusModel> elem) {
        data.addAll(elem);
        notifyDataSetChanged();
    }

    public void replaceAll(List<StatusModel> elem) {
        data.clear();
        data.addAll(elem);
        notifyDataSetChanged();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private View card, itemLayout;
        private SimpleDraweeView avatar, cover;
        private TextView author;

        public ItemViewHolder(View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.card_view);
            itemLayout = itemView.findViewById(R.id.list_item_layout);
            avatar = (SimpleDraweeView) itemView.findViewById(R.id.avatar);
            cover = (SimpleDraweeView) itemView.findViewById(R.id.cover);
            author = (TextView) itemView.findViewById(R.id.author);
        }
    }

}
