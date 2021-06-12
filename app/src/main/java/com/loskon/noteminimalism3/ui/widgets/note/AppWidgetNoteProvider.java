package com.loskon.noteminimalism3.ui.widgets.note;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.ui.activities.MainActivity;
import com.loskon.noteminimalism3.ui.activities.NoteActivity;

import static com.loskon.noteminimalism3.auxiliary.other.MyIntent.PUT_EXTRA_ID;
import static com.loskon.noteminimalism3.auxiliary.other.MyIntent.PUT_EXTRA_SEL_NOTE_CATEGORY;

/**
 * A widget provider.  We have a string that we pull from a preference in order to show
 * the configuration settings and the current time when the widget was updated.  We also
 * register a BroadcastReceiver for time-changed and timezone-changed broadcasts, and
 * update then too.
 */

public class AppWidgetNoteProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            String title = AppWidgetConfigure.loadTitle(context, appWidgetId);
            long noteId = AppWidgetConfigure.loadId(context, appWidgetId);
            String date = AppWidgetConfigure.loadDate(context, appWidgetId);
            updateAppWidget(context, appWidgetManager, appWidgetId, title, noteId, date, false);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            AppWidgetConfigure.deleteTitlePref(context, appWidgetId);
        }
    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String titlePrefix, long noteId, String date, boolean isDelete) {
        Intent intent;

        if (appWidgetId != -1) {
            noteId = getId(isDelete, noteId);

            if (noteId == -1) {
                intent = new Intent(context, MainActivity.class);
            } else {
                intent = new Intent(context, NoteActivity.class);
                intent.putExtra(PUT_EXTRA_SEL_NOTE_CATEGORY, 0);
                intent.putExtra(PUT_EXTRA_ID, noteId);
            }

            PendingIntent pendingAppIntent = PendingIntent.getActivity(context, appWidgetId,
                    intent, PendingIntent.FLAG_CANCEL_CURRENT);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget_note);
            views.setTextViewText(R.id.title_widget, titlePrefix);
            views.setTextViewText(R.id.date_widget, date);
            views.setOnClickPendingIntent(R.id.linLytWidget, pendingAppIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    private static long getId(boolean isDelete, long noteId) {
      if (isDelete) return -1;
      else return noteId;
    }
}
