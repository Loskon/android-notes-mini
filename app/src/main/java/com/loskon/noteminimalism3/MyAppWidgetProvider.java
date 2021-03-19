package com.loskon.noteminimalism3;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.loskon.noteminimalism3.ui.activity.NoteActivity;

import static com.loskon.noteminimalism3.auxiliary.other.MyIntent.PUT_EXTRA_ID;
import static com.loskon.noteminimalism3.auxiliary.other.MyIntent.PUT_EXTRA_SEL_NOTE_CATEGORY;

/**
 * A widget provider.  We have a string that we pull from a preference in order to show
 * the configuration settings and the current time when the widget was updated.  We also
 * register a BroadcastReceiver for time-changed and timezone-changed broadcasts, and
 * update then too.
 */

public class MyAppWidgetProvider extends AppWidgetProvider {
    // log tag
    private static final String TAG = "ExampleAppWidgetProvider";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //Log.d(TAG, "onUpdate");
        // For each widget that needs an update, get the text that we should display:
        //   - Create a RemoteViews object for it
        //   - Set the text in the RemoteViews object
        //   - Tell the AppWidgetManager to show that views object for the widget.
        for (int appWidgetId : appWidgetIds) {
            String title = AppWidgetConfigure.loadTitlePref(context, appWidgetId);
            long idPrefix = AppWidgetConfigure.loadIdPref(context, appWidgetId);
            String date = AppWidgetConfigure.loadDatePref(context, appWidgetId);
            updateAppWidget(context, appWidgetManager, appWidgetId, title, idPrefix, date, false);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        //Log.d(TAG, "onDeleted");
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            AppWidgetConfigure.deleteTitlePref(context, appWidgetId);
        }
    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String titlePrefix, long noteId, String date, boolean isDelete) {
        //Log.d(TAG, "updateAppWidget appWidgetId=" + appWidgetId + " titlePrefix=" + titlePrefix);
        if (appWidgetId != -1) {
            noteId = getId(isDelete, noteId);

            Intent intent = new Intent(context, NoteActivity.class);
            intent.putExtra(PUT_EXTRA_SEL_NOTE_CATEGORY, 0);
            intent.putExtra(PUT_EXTRA_ID, noteId);
            PendingIntent pendingAppIntent = PendingIntent.getActivity(context, appWidgetId,
                    intent, PendingIntent.FLAG_CANCEL_CURRENT);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            views.setTextViewText(R.id.title_widget, titlePrefix);
            views.setTextViewText(R.id.date_widget, date);
            views.setOnClickPendingIntent(R.id.linLytWidget, pendingAppIntent);
            // Поручите менеджеру виджетов обновить виджет
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    private static long getId(boolean isDelete, long noteId) {
      if (isDelete) return 0;
      else return noteId;
    }
}
