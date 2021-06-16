package com.loskon.noteminimalism3.ui.widgets.note;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loskon.noteminimalism3.auxiliary.other.AppFontManager;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.other.MyColor;
import com.loskon.noteminimalism3.auxiliary.other.MyDate;
import com.loskon.noteminimalism3.auxiliary.other.MyIntent;
import com.loskon.noteminimalism3.auxiliary.sharedpref.MySharedPref;
import com.loskon.noteminimalism3.database.DbAdapter;
import com.loskon.noteminimalism3.model.Note;
import com.loskon.noteminimalism3.ui.activities.NoteActivity;

import java.util.List;

import static com.loskon.noteminimalism3.database.NoteDbSchema.COLUMN_DATE;

/**
 * The screen for the configure widget.
 */

public class AppWidgetConfigure extends Activity {

    private static final String PREFS_NAME_WIDGETS = "prefs_name_app_widgets";

    private static final String PREF_KEY_WIDGET_TITLE = "pref_key_widget_title_";
    private static final String PREF_KEY_WIDGET_DATE = "pref_key_widget_date_";
    private static final String PREF_KEY_WIDGET_NOTE_ID = "pref_key_widget_note_id_";

    int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private DbAdapter dbAdapter;
    private Context context;

    private List<Note> notes;

    private ListView listView;
    private FloatingActionButton fabWidget;
    private BottomAppBar btmAppBarWidget;

    public AppWidgetConfigure() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        AppFontManager.setFont(this);
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);
        setContentView(R.layout.activity_widget_configure);
        MyColor.setColorStatBarAndTaskDesc(this);

        initialiseWidgets();
        initialiseConfigureWidgets();
        initialiseIntent();
        initialiseAdapters();
        handlersWidgets();
    }

    private void initialiseWidgets() {
        context = AppWidgetConfigure.this;

        fabWidget = findViewById(R.id.fabWidget);
        btmAppBarWidget = findViewById(R.id.btmAppBarWidget);

        listView =  findViewById(R.id.listView);
        dbAdapter = new DbAdapter(context);
    }

    private void initialiseConfigureWidgets() {
        MyColor.setNavIconColor(context, btmAppBarWidget);
        MyColor.setColorFab(context, fabWidget);

        listView.setOnItemClickListener(clickListener);
    }

    private void initialiseIntent() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
    }

    private void initialiseAdapters() {
        dbAdapter.open();
        notes = dbAdapter.getNotes("del_items = 0", COLUMN_DATE + " DESC");
        dbAdapter.close();

        NoteWidgetAdapter noteWidgetAdapter =
                new NoteWidgetAdapter(context, R.layout.activity_widget_configure, notes);
        noteWidgetAdapter.setColorViewFav(MyColor.getMyColor(this));
        listView.setAdapter(noteWidgetAdapter);
        noteWidgetAdapter.notifyDataSetChanged();
    }

    private void handlersWidgets() {
        fabWidget.setOnClickListener(view -> MyIntent.addNewNoteFromWidget(context));
        btmAppBarWidget.setNavigationOnClickListener(v -> finish());
        NoteActivity.regCallbackNoteWidget(this::buildAppWidget);
    }

    AdapterView.OnItemClickListener clickListener = (adapterView, view, position, l) -> {
        String title = notes.get(position).getTitle();
        long id = notes.get(position).getId();
        String date = MyDate.getNowDate(notes.get(position).getDate());
        buildAppWidget(title, id, date);
    };

    private void buildAppWidget(String title, long noteId, String date) {
        saveTitlePref(context, appWidgetId, title, noteId, date);
        MySharedPref.setCustomInt(context, noteId, appWidgetId);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        AppWidgetNoteProvider.updateAppWidget(context, appWidgetManager,
                appWidgetId, title, noteId, date, false);

        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId,
                              String text, long noteId, String date) {
        SharedPreferences.Editor prefs =
                context.getSharedPreferences(PREFS_NAME_WIDGETS, 0).edit();
        prefs.putString(PREF_KEY_WIDGET_TITLE + appWidgetId, text);
        prefs.putString(PREF_KEY_WIDGET_DATE + appWidgetId, date);
        prefs.putLong(PREF_KEY_WIDGET_NOTE_ID + appWidgetId, noteId);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitle(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME_WIDGETS, 0);
        String title = prefs.getString(PREF_KEY_WIDGET_TITLE + appWidgetId, null);
        if (title != null) {
            return title;
        } else {
            return context.getString(R.string.unknown_error);
        }
    }

    static String loadDate(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME_WIDGETS, 0);
        String date = prefs.getString(PREF_KEY_WIDGET_DATE + appWidgetId, null);
        if (date != null) {
            return date;
        } else {
            return context.getString(R.string.unknown_error);
        }
    }

    static long loadId(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME_WIDGETS, 0);
        return prefs.getLong(PREF_KEY_WIDGET_NOTE_ID + appWidgetId, -1);
    }

    public static void deleteTitlePref(Context context, int appWidgetId) {
        MySharedPref.deleteCustomInt(context, loadId(context, appWidgetId));
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME_WIDGETS, 0).edit();
        prefs.remove(PREF_KEY_WIDGET_TITLE + appWidgetId).apply();
        prefs.remove(PREF_KEY_WIDGET_DATE + appWidgetId).apply();
        prefs.remove(PREF_KEY_WIDGET_NOTE_ID + appWidgetId).apply();
    }
}
