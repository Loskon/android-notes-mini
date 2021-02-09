package com.loskon.noteminimalism3.helper;

import android.app.Activity;

import com.dinuscxj.refresh.RecyclerRefreshLayout;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.others.RefreshView;

public class Refresh {

    private Activity activity;
    private RecyclerRefreshLayout refreshLayout;

    public Refresh (Activity activity) {
        this.activity = activity;
    }

    public void build() {
        refreshLayout = activity.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshTargetOffset((int) activity
                .getResources().getDimension(R.dimen.height_refresh_layout));
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
