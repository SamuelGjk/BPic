package com.happy.samuelalva.bcykari.support.adapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samuel.Alva on 2015/6/10.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerAdapter.ItemViewHolder> {
    protected Context context;
    protected List<T> data;

    public BaseRecyclerAdapter(Context context) {
        this.context = context;
        this.data = new ArrayList<T>();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * c该方法需要子类实现，需要返回item布局的resource id
     *
     * @return
     */
    public abstract int getItemResource();

    /**
     * 使用该getItemView方法替换原来的getView方法，需要子类实现
     *
     * @param holder
     * @param position
     * @return
     */
    public abstract void doBindViewHolder(BaseRecyclerAdapter.ItemViewHolder holder, int position);

    @Override
    public BaseRecyclerAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseRecyclerAdapter.ItemViewHolder(View.inflate(context, getItemResource(), null));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerAdapter.ItemViewHolder holder, int position) {
        doBindViewHolder(holder, position);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> views = new SparseArray<View>();
        private View itemView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        public View getView(int resId) {
            View v = views.get(resId);
            if (null == v) {
                v = itemView.findViewById(resId);
                views.put(resId, v);
            }
            return v;
        }
    }

    public void addAll(List<T> elem) {
        data.addAll(elem);
        notifyDataSetChanged();
    }

    public void remove(T elem) {
        data.remove(elem);
        notifyDataSetChanged();
    }

    public void remove(int index) {
        data.remove(index);
        notifyDataSetChanged();
    }

    public void replaceAll(List<T> elem) {
        data.clear();
        data.addAll(elem);
        notifyDataSetChanged();
    }
}
