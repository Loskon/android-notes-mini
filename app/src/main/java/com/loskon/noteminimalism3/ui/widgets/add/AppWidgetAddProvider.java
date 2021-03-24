package com.loskon.noteminimalism3.ui.widgets.add;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.ui.activity.NoteActivity;

import static com.loskon.noteminimalism3.auxiliary.other.MyIntent.PUT_EXTRA_ID;
import static com.loskon.noteminimalism3.auxiliary.other.MyIntent.PUT_EXTRA_SEL_NOTE_CATEGORY;

/**
 * The widget.
 */

public class AppWidgetAddProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent intent = new Intent(context, NoteActivity.class);
        intent.putExtra(PUT_EXTRA_SEL_NOTE_CATEGORY, 0);
        intent.putExtra(PUT_EXTRA_ID, 0);
        PendingIntent pending = PendingIntent.getActivity(context, 0, intent, 0);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_add);
        views.setOnClickPendingIntent(R.id.ivWidget, pending);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

}