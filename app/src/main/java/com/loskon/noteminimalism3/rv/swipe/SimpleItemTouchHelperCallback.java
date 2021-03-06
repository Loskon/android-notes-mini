package com.loskon.noteminimalism3.rv.swipe;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.loskon.noteminimalism3.rv.adapter.MyRecyclerViewAdapter;

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.SimpleCallback {
    private final MyRecyclerViewAdapter rvAdapter;

    public SimpleItemTouchHelperCallback(MyRecyclerViewAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        rvAdapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAbsoluteAdapterPosition();
        //rvAdapter.deleteItem(position);
    }
}