/*
 * Copyright 2015 SamuelGjk <samuel.alva@outlook.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package moe.yukinoneko.bpic.support.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import moe.yukinoneko.bpic.R;
import moe.yukinoneko.bpic.model.StatusModel;
import moe.yukinoneko.bpic.ui.activity.base.BaseDetailActivity;
import moe.yukinoneko.bpic.widget.RatioImageView;

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
