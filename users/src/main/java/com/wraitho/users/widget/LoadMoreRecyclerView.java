package com.wraitho.users.widget;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 *
 * This class and the whole pagination solution was over brought from former projects, so it's kinda old.
 * Probably should be replaced by Paging library from Google
 *
 * Adapter class which deals with pagination on a view level. It shows the custom footer view, and requests for new data.
 */
public abstract class LoadMoreRecyclerView<NormalViewHolder extends RecyclerView.ViewHolder, FooterViewHolder extends RecyclerView.ViewHolder>
                                            extends ExtendedRecyclerViewAdapter<NormalViewHolder,FooterViewHolder> {
    private final AtomicBoolean keepOnAppending;
    private final AtomicBoolean dataPending;
    private PublishSubject<Integer> loadMoreListener;

    public LoadMoreRecyclerView() {
        this.keepOnAppending = new AtomicBoolean(true);
        this.dataPending = new AtomicBoolean(false);

        loadMoreListener = PublishSubject.create();
        loadMoreListener.subscribe(integer -> Log.d("FASZA", "APPEND"));
    }

    /**
     * Let the adapter know that data is load and ready to view.
     *
     * @param keepOnAppending whether the adapter should request to load more when scrolling to the bottom.
     */
    public void onDataReady(boolean keepOnAppending) {
        dataPending.set(false);
        this.keepOnAppending.set(keepOnAppending);
        notifyDataSetChanged();
    }

    /**
     * Let the sub adapter know we need more data.
     * @param holder ViewHolder holding the footer view;
     */
    @Override
    protected void onBindFooterView(FooterViewHolder holder) {
        if (!dataPending.get() && dataPending.compareAndSet(false, true)) {
            loadMoreListener.onNext(    0);
        }
    }

    public Observable getLoadMoreListener() {
        return loadMoreListener;
    }
}
