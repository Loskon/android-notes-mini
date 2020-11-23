package com.loskon.noteminimalism3.activity.mainHelper;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.loskon.noteminimalism3.rv.SwipeRecyclerViewAdapter;

/**
 * Данное расширение позволяет следить за состоянем пустого списка
 */

public class CustomRecyclerViewEmpty extends RecyclerView.AdapterDataObserver {

    private final TextView mTextView;
    private final SwipeRecyclerViewAdapter swipeAdapter;

    public CustomRecyclerViewEmpty(TextView textView, SwipeRecyclerViewAdapter swipeAdapter) {
        mTextView = textView;
        this.swipeAdapter = swipeAdapter;
    }

    @Override
    public void onChanged() {
        super.onChanged();
        checkEmpty();
    }
    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        super.onItemRangeInserted(positionStart, itemCount);
        checkEmpty();
    }
    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
        super.onItemRangeRemoved(positionStart, itemCount);
        checkEmpty();
    }

    public void checkEmpty() {
        if (swipeAdapter.getItemCount() == 0) {
            mTextView.setVisibility(View.VISIBLE);
        } else {
            mTextView.setVisibility(View.GONE);
        }
    }
}
