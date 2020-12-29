package com.loskon.noteminimalism3.helper;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.loskon.noteminimalism3.rv.MyRecyclerViewAdapter;

/**
 * Данное расширение позволяет следить за состоянем пустого списка
 */

public class CheckEmptyRecyclerView extends RecyclerView.AdapterDataObserver {

    private final TextView textView;
    private final MyRecyclerViewAdapter rvAdapter;

    public CheckEmptyRecyclerView(TextView textView, MyRecyclerViewAdapter rvAdapter) {
        this.textView = textView;
        this.rvAdapter = rvAdapter;
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
        if (rvAdapter.getItemCount() == 0) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }
}
