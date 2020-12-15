package com.loskon.noteminimalism3.ui.mainHelper;

import android.content.Context;

import com.dinuscxj.refresh.RecyclerRefreshLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.loskon.noteminimalism3.others.RefreshView;
import com.loskon.noteminimalism3.rv.CustomRecyclerViewAdapter;

//
//


/**
 * Отрисовка прозрачного swipeRefreshLayout для создания иллюзии взаимодействия со
 * списком, а также в помощи появления bottomAppBar при скролле
 */

public class RefreshHandlerAndRedrawing {

    private final Context context;
    private final CustomRecyclerViewAdapter swipeAdapter;
    private final RecyclerRefreshLayout mRefreshLayout;
    private final BottomAppBar bottomAppBar;

    public RefreshHandlerAndRedrawing(Context context, CustomRecyclerViewAdapter swipeAdapter, RecyclerRefreshLayout mRefreshLayout, BottomAppBar bottomAppBar) {
        this.context = context;
        this.swipeAdapter = swipeAdapter;
        this.mRefreshLayout = mRefreshLayout;
        this.bottomAppBar = bottomAppBar;
    }

    public void refreshMethod() {
        // Высота
        mRefreshLayout.setRefreshTargetOffset(80);
        // С какой высоты заканчивается "анимация"
        mRefreshLayout.setAnimateToRefreshDuration(0);
        // Метод для настройки парамтров отображения
        mRefreshLayout.setRefreshView((new RefreshView(context)),
                (new RecyclerRefreshLayout.LayoutParams(200, 300)));
        mRefreshLayout.setOnRefreshListener(() -> {
            mRefreshLayout.setRefreshing(false);
            bottomAppBar.performShow();
        });
    }
}
