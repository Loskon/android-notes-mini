package com.loskon.noteminimalism3.rv.swipe;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.loskon.noteminimalism3.model.Note;
import com.loskon.noteminimalism3.rv.CustomRecyclerViewAdapter;

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.SimpleCallback {
    private final CustomRecyclerViewAdapter mAdapter;

    public SimpleItemTouchHelperCallback(CustomRecyclerViewAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mAdapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAbsoluteAdapterPosition();
        mAdapter.deleteItem(position);
    }
}