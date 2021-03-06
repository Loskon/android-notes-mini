package com.loskon.noteminimalism3.auxiliary.main;

import android.app.Activity;

import com.dinuscxj.refresh.RecyclerRefreshLayout;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.other.MySizeItem;
import com.loskon.noteminimalism3.others.RefreshView;

/**
 * Отрисовка и обработчик для RecyclerRefreshLayout
 */

public class RefreshLayout {

    private final Activity activity;
    private RecyclerRefreshLayout refreshLayout;

    public RefreshLayout(Activity activity) {
        this.activity = activity;
    }

    public void build() {
        refreshLayout = activity.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshTargetOffset(MySizeItem.getSizeRefresh(activity));
        // С какой высоты заканчивается "анимация"
        refreshLayout.setAnimateToRefreshDuration(0);
        // Метод для настройки парамтров отображения
        refreshLayout.setRefreshView((new RefreshView(activity)),
                (new RecyclerRefreshLayout.LayoutParams(0, 0)));
        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(false);
        });
    }
}
