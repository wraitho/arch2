package com.wraitho.users.widget;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 *
 * This class and the whole pagination solution was over brought from former projects, so it's kinda old.
 * Probably should be replaced by Paging library from Google
 *
 * Extends the basic {@link android.support.v7.widget.RecyclerView.Adapter} to enable Header view and Footer view
 *
 * @param <NORMAL_VH> ViewHolder class extends {@link android.support.v7.widget.RecyclerView.ViewHolder} for "normal" items.
 * @param <FOOTER_VH> ViewHolder class extends {@link android.support.v7.widget.RecyclerView.ViewHolder} for the footer item.
 */
public abstract class ExtendedRecyclerViewAdapter<NORMAL_VH extends RecyclerView.ViewHolder,
                                                    FOOTER_VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private enum ViewType {
        NORMAL(0),
        FOOTER(1);

        private int code;

        ViewType(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount()-1) {
            return ViewType.FOOTER.getCode();
        }
        return ViewType.NORMAL.getCode();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ViewType.FOOTER.getCode()) {
            return onCreateFooterViewHolder(parent);
        }
        return onCreateNormalViewHolder(parent);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == ViewType.FOOTER.getCode()) {
            onBindFooterView((FOOTER_VH) holder);
        } else {
            onBindNormalView((NORMAL_VH) holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return getRealItemCount()+1;
    }

    protected abstract FOOTER_VH onCreateFooterViewHolder(ViewGroup parent);

    protected abstract NORMAL_VH onCreateNormalViewHolder(ViewGroup parent);

    protected abstract void onBindFooterView(FOOTER_VH holder);

    protected abstract void onBindNormalView(NORMAL_VH holder, int position);

    protected abstract int getRealItemCount();
}
