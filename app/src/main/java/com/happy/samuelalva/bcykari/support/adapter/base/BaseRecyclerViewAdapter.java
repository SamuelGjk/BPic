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
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected Context context;
    protected List<T> data;

    public BaseRecyclerViewAdapter(Context context) {
        this.context = context;
        this.data = new ArrayList<T>();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * 该方法需要子类实现，需要返回item布局的resource id
     *
     * @return
     */
    public abstract int getItemResource();

    /**
     * 使用该doBindViewHolder方法替换原来的getView方法，需要子类实现
     *
     * @param holder
     * @param position
     * @return
     */
    public abstract void doBindViewHolder(ItemViewHolder holder, int position);

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(View.inflate(context, getItemResource(), null));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        doBindViewHolder((ItemViewHolder) holder, position);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> views = new SparseArray<View>();
        private View itemView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        @SuppressWarnings("unchecked")
        public <T extends View> T getView(int resId) {
            View v = views.get(resId);
            if (null == v) {
                v = itemView.findViewById(resId);
                views.put(resId, v);
            }
            return (T) v;
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
