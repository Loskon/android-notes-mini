package com.loskon.noteminimalism3.activity.mainHelper;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.loskon.noteminimalism3.rv.SwipeRecyclerViewAdapter;

/**
 *
 */

public class CustomOnItemTouchListener implements RecyclerView.OnItemTouchListener {

    private final SwipeRecyclerViewAdapter swipeAdapter;

    public CustomOnItemTouchListener(SwipeRecyclerViewAdapter swipeAdapter) {
        this.swipeAdapter = swipeAdapter;
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        if (motionEvent.getAction() != MotionEvent.ACTION_UP) {
            return false;
        }
        View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if (child != null) {
            return false;
        } else {
            swipeAdapter.closeAllItems();
            return true;
        }
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
