package com.happy.samuelalva.bcykari.support.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.happy.samuelalva.bcykari.R;
import com.happy.samuelalva.bcykari.model.StatusModel;
import com.happy.samuelalva.bcykari.ui.activity.base.BaseDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by SamuelGjk on 2015/8/22.
 */
public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> {
    private Context mContext;
    private List<StatusModel> mList;
    private Class mClass;
    private int mLastPosition = -1;

    public HomeListAdapter(Context context, List<StatusModel> list, Class cls) {
        mContext = context;
        mList = list;
        mClass = cls;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        StatusModel model = mList.get(position);
        holder.model = model;
        holder.author.setText(model.author);
        if (!TextUtils.isEmpty(model.avatar)) {
            Picasso.with(mContext).load(model.avatar).placeholder(android.R.color.darker_gray).into(holder.avatar);
            holder.avatar.setVisibility(View.VISIBLE);
        }
        Picasso.with(mContext).load(model.cover).placeholder(android.R.color.darker_gray).into(holder.cover);

        if (position > mLastPosition) {
            holder.card.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            holder.card.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    holder.card.startAnimation(animation);
                }
            }, 64);
            mLastPosition = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.card.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View card;
        CircleImageView avatar;
        ImageView cover;
        TextView author;

        StatusModel model;

        public ViewHolder(View itemView) {
            super(itemView);
            card = itemView;
            avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
            cover = (ImageView) itemView.findViewById(R.id.cover);
            author = (TextView) itemView.findViewById(R.id.author);
            card.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(mContext, mClass);
            i.putExtra(BaseDetailActivity.ENTITY, model);
            mContext.startActivity(i);
        }
    }
}
