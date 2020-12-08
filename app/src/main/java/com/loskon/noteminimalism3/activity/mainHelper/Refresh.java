package com.loskon.noteminimalism3.activity.mainHelper;

import android.app.Activity;

import com.dinuscxj.refresh.RecyclerRefreshLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.others.RefreshView;

public class Refresh {

    private static RecyclerRefreshLayout refreshLayout;

    public static void setRefreshLayout(Activity activity, BottomAppBar bottomAppBar) {
        refreshLayout = activity.findViewById(R.id.refresh_layout);
        // Высота
        refreshLayout.setRefreshTargetOffset((int)
                activity.getResources().getDimension(R.dimen.height_refresh_layout));
        // С какой высоты заканчивается "анимация"
        refreshLayout.setAnimateToRefreshDuration(0);
        // Метод для настройки парамтров отображения
        refreshLayout.setRefreshView((new RefreshView(activity)),
                (new RecyclerRefreshLayout.LayoutParams(200, 300)));
        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(false);
            bottomAppBar.performShow();
        });
    }
}
