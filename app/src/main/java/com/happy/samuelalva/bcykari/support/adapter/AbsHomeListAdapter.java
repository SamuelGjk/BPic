package com.happy.samuelalva.bcykari.support.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.happy.samuelalva.bcykari.R;
import com.happy.samuelalva.bcykari.model.StatusModel;
import com.happy.samuelalva.bcykari.receiver.ConnectivityReceiver;
import com.happy.samuelalva.bcykari.support.Utility;
import com.happy.samuelalva.bcykari.support.adapter.base.BaseRecyclerViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Samuel.Alva on 2015/4/15.
 */
public abstract class AbsHomeListAdapter extends BaseRecyclerViewAdapter<StatusModel> {
    private final String TAG = AbsHomeListAdapter.class.getSimpleName();

    private boolean hasAvatar;

    private SparseArray<Integer> mHeights;

    public AbsHomeListAdapter(Context context, boolean hasAvatar) {
        super(context);
        this.hasAvatar = hasAvatar;

        mHeights = new SparseArray<>();
    }

    @Override
    public int getItemResource() {
        return R.layout.home_list_item;
    }

    @Override
    public void doBindViewHolder(ItemViewHolder holder, final int position) {
        View card = holder.getView(R.id.card_view);
        CircleImageView avatar = holder.getView(R.id.avatar);
        ImageView cover = holder.getView(R.id.cover);
        TextView author = holder.getView(R.id.author);

        Integer height = mHeights.get(position);
        if (height == null) {
            height = (int) Utility.dp2px(context, (float) (170 + Math.random() * 50));
            mHeights.append(position, height);
        }
        ViewGroup.LayoutParams lp = cover.getLayoutParams();
        lp.height = height;
        cover.setLayoutParams(lp);

        if (hasAvatar) {
            avatar.setVisibility(View.VISIBLE);
            Picasso.with(context).load(data.get(position).avatar).placeholder(android.R.color.darker_gray).into(avatar);
        }

        Picasso.with(context).load(data.get(position).cover).placeholder(android.R.color.darker_gray).into(cover);

        author.setText(data.get(position).author);
        card.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ConnectivityReceiver.isConnected) {
                            doClick(data.get(position));
                        } else {
                            Utility.showToast(context, context.getString(R.string.no_network));
                        }
                    }
                }
        );
    }

    protected abstract void doClick(StatusModel model);

    @Override
    public void replaceAll(List<StatusModel> elem) {
        mHeights.clear();
        super.replaceAll(elem);
    }
}
