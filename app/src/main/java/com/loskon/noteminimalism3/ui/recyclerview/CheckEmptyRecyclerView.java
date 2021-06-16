package com.loskon.noteminimalism3.ui.recyclerview;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.loskon.noteminimalism3.R;

/**
 * Данное расширение позволяет следить за состоянем пустого списка
 */

public class CheckEmptyRecyclerView extends RecyclerView.AdapterDataObserver {

    private final TextView textEmpty;
    private final MyRecyclerViewAdapter rvAdapter;

    public CheckEmptyRecyclerView(Activity activity, MyRecyclerViewAdapter rvAdapter) {
        this.rvAdapter = rvAdapter;
        textEmpty = activity.findViewById(R.id.textEmpty);
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
            textEmpty.setVisibility(View.VISIBLE);
        } else {
            textEmpty.setVisibility(View.GONE);
        }
    }
}
