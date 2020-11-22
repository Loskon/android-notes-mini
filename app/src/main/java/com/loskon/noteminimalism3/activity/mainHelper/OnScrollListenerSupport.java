package com.loskon.noteminimalism3.activity.mainHelper;



import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.loskon.noteminimalism3.rv.SwipeRecyclerViewAdapter;

/**
 *
 */

public class OnScrollListenerSupport extends RecyclerView.OnScrollListener {

    private final SwipeRecyclerViewAdapter swipeAdapter;

    public OnScrollListenerSupport(SwipeRecyclerViewAdapter swipeAdapter) {
        this.swipeAdapter = swipeAdapter;
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        swipeAdapter.closeAllItems();
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
    }
}
