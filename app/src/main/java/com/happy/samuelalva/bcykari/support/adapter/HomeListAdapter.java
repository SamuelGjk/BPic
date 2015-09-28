package com.happy.samuelalva.bcykari.support.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.happy.samuelalva.bcykari.R;
import com.happy.samuelalva.bcykari.model.StatusModel;
import com.happy.samuelalva.bcykari.ui.activity.base.BaseDetailActivity;
import com.happy.samuelalva.bcykari.widget.RatioImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by SamuelGjk on 2015/8/22.
 */
public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> {
    private Context mContext;
    private List<StatusModel> mList;
    private Class mClass;

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
        final StatusModel model = mList.get(position);
        holder.detailUrl = model.detail;
        holder.author.setText(model.author);
        if (!TextUtils.isEmpty(model.avatar)) {
            Picasso.with(mContext).load(model.avatar).noFade().placeholder(android.R.color.darker_gray).into(holder.avatar);
            holder.avatar.setVisibility(View.VISIBLE);
        }
        holder.cover.setOriginalSize(model.width, model.height);
        Picasso.with(mContext).load(model.cover).config(Bitmap.Config.RGB_565).placeholder(android.R.color.darker_gray).transform(new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {
                if (!(model.width > 0 && model.height > 0)) {
                    model.width = source.getWidth();
                    model.height = source.getHeight();
                    holder.cover.setOriginalSize(source.getWidth(), source.getHeight());
                }
                return source;
            }

            @Override
            public String key() {
                return "setCoverViewSize";
            }
        }).into(holder.cover);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView avatar;
        RatioImageView cover;
        TextView author;

        String detailUrl;

        public ViewHolder(View itemView) {
            super(itemView);
            avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
            cover = (RatioImageView) itemView.findViewById(R.id.cover);
            author = (TextView) itemView.findViewById(R.id.author);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(mContext, mClass);
            i.putExtra(BaseDetailActivity.DETAIL_URL, detailUrl);
            mContext.startActivity(i);
        }
    }
}
