package com.happy.samuelalva.bcykari.support.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
public class HomeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private Random random;
    private int lastPosition = -1;

    private Context context;
    private boolean hasAvatar;
    private List<StatusModel> data;
    private LayoutInflater mInflater;

    public HomeListAdapter(Context context, boolean hasAvatar) {
        this.context = context;
        this.hasAvatar = hasAvatar;
        this.data = new ArrayList<>();
        this.mInflater = LayoutInflater.from(context);
        random = new Random();

        Fresco.initialize(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            return new HeaderViewHolder(mInflater.inflate(R.layout.list_header, null));
        } else {
            return new ItemHolder(mInflater.inflate(R.layout.home_list_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemHolder) {
            ItemHolder h = (ItemHolder) holder;
            if (hasAvatar) {
                h.avatar.setVisibility(View.VISIBLE);
                h.avatar.setImageURI(Uri.parse(data.get(position - 2).getAvatar()));
            }
            h.cover.setImageURI(Uri.parse(data.get(position - 2).getCover()));
            h.author.setText(data.get(position - 2).getAuthor());
            h.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, DetailActivity.class);
                    i.putExtra(DetailActivity.DETAIL_URL, data.get(position - 2).getDetail());
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
                h.itemLayout.startAnimation(animation);
                lastPosition = position;
            }
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        if (holder instanceof ItemHolder)
            ((ItemHolder) holder).itemLayout.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return data.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0 || position == 1) ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
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

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View view) {
            super(view);
        }
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        private View card, itemLayout;
        private SimpleDraweeView avatar, cover;
        private TextView author;

        public ItemHolder(View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.card_view);
            itemLayout = itemView.findViewById(R.id.list_item_layout);
            avatar = (SimpleDraweeView) itemView.findViewById(R.id.avatar);
            cover = (SimpleDraweeView) itemView.findViewById(R.id.cover);
            author = (TextView) itemView.findViewById(R.id.author);
        }
    }

}
