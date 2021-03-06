package com.codepath.apps.restclienttemplate;

import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.apps.restclienttemplate.interfaces.RequestCompleteCallback;

public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private final int visibleThreshold = 5;
    // The current offset index of data you have loaded
    private int currentPage = 0;
    // True if we are still waiting for the last set of data to load.
    private boolean loading = false;
    // Sets the starting page index
    private int startingPageIndex = 0;

    LinearLayoutManager mLayoutManager;

    public EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        super.onScrolled(view, dx, dy);
        int totalItemCount = mLayoutManager.getItemCount();

        if (totalItemCount == 0 || loading) {
            return;
        }

        int lastVisibleItemIndex = mLayoutManager.findLastVisibleItemPosition();

        Log.i("onScrolled", "onScrolled: " + loading + " " + totalItemCount + " " + lastVisibleItemIndex);

        if (lastVisibleItemIndex + visibleThreshold > totalItemCount) {
            loading = true;
            currentPage++;
            RequestCompleteCallback callback = new RequestCompleteCallback() {
                @Override
                public void requestComplete(boolean success) {
                        loading = false;
                }
            };
            Log.i("onScrolled", "onScrolled: Getting more");
            onLoadMore(currentPage, totalItemCount, view, callback);
        }
    }

    // Call this method whenever performing new searches
    public void resetState() {
        this.currentPage = this.startingPageIndex;
        this.loading = true;
    }

    // Defines the process for actually loading more data based on page
    public abstract void onLoadMore(int page, int totalItemsCount, RecyclerView view, RequestCompleteCallback callback);

}