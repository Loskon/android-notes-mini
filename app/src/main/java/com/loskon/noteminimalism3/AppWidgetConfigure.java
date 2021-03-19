package com.loskon.noteminimalism3;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.

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
import com.loskon.noteminimalism3.auxiliary.other.MyColor;
import com.loskon.noteminimalism3.auxiliary.other.MyDate;
import com.loskon.noteminimalism3.auxiliary.other.MyIntent;
import com.loskon.noteminimalism3.auxiliary.sharedpref.MySharedPref;
import com.loskon.noteminimalism3.db.DbAdapter;
import com.loskon.noteminimalism3.db.NoteDbSchema.NoteTable.Columns;
import com.loskon.noteminimalism3.model.Note;
import com.loskon.noteminimalism3.ui.activity.NoteActivity;

import java.util.List;

/**
 * The configuration screen for the ExampleAppWidgetProvider widget sample.
 */

public class AppWidgetConfigure extends Activity {
    static final String TAG = "ExampleAppWidgetConfigure";
    private static final String PREFS_NAME
            = "com.example.android.apis.appwidget.ExampleAppWidgetProvider";
    private static final String PREF_PREFIX_KEY = "prefix_";
    private static final String PREF_PREFIX_DATE = "prefix_date_";
    private static final String PREF_PREFIX_ID_KEY = "prefix_id_";

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private List<Note> notes;
    private Context context;

    public AppWidgetConfigure() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);
        setContentView(R.layout.activity_widget_list);
        MyColor.setColorStatBarAndTaskDesc(this);

        context = AppWidgetConfigure.this;

        FloatingActionButton fabWidget = findViewById(R.id.fabWidget);
        BottomAppBar btmAppBarWidget = findViewById(R.id.btmAppBarWidget);

        MyColor.setNavIconColor(context, btmAppBarWidget);
        MyColor.setColorFab(context, fabWidget);

        ListView listView = (ListView) findViewById(R.id.listView);
        DbAdapter dbAdapter = new DbAdapter(context);

        listView.setOnItemClickListener(clickListener);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        dbAdapter.open();
        notes = dbAdapter.getNotes("del_items = 0", Columns.COLUMN_DATE + " DESC");
        dbAdapter.close();

        NoteWidgetAdapter noteWidgetAdapter = new NoteWidgetAdapter(
                context, R.layout.activity_widget_list, notes);
        noteWidgetAdapter.setColorViewFav(MyColor.getMyColor(this));
        listView.setAdapter(noteWidgetAdapter);
        noteWidgetAdapter.notifyDataSetChanged();

        fabWidget.setOnClickListener(view -> {
            MyIntent.addNewNoteFromWidget(context);
        });

        btmAppBarWidget.setNavigationOnClickListener(v -> {
            finish();
        });

        NoteActivity.regCallbackNoteWidget(this::buildAppWidget);
    }

    AdapterView.OnItemClickListener clickListener = (adapterView, view, position, l) -> {
        String title = notes.get(position).getTitle();
        long id = notes.get(position).getId();
        String date = MyDate.getNowDate(notes.get(position).getDate());
        buildAppWidget(title, id, date);
    };

    private void buildAppWidget(String title, long noteId, String date) {
        saveTitlePref(context, mAppWidgetId, title, noteId, date);
        MySharedPref.setCustomInt(context, noteId, mAppWidgetId);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        MyAppWidgetProvider.updateAppWidget(context, appWidgetManager,
                mAppWidgetId, title, noteId, date, false);

        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text, long id, String date) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.putLong(PREF_PREFIX_ID_KEY + appWidgetId, id);
        prefs.putString(PREF_PREFIX_DATE + appWidgetId, date);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String prefix = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (prefix != null) {
            return prefix;
        } else {
            return context.getString(R.string.unknown_error);
        }
    }

    static long loadIdPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getLong(PREF_PREFIX_ID_KEY + appWidgetId, 0);
    }

    static String loadDatePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String prefix = prefs.getString(PREF_PREFIX_DATE + appWidgetId, null);
        if (prefix != null) {
            return prefix;
        } else {
            return context.getString(R.string.unknown_error);
        }
    }

    public static void deleteTitlePref(Context context, int appWidgetId) {
        MySharedPref.deleteCustomInt(context, loadIdPref(context, appWidgetId));
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId).apply();
        prefs.remove(PREF_PREFIX_DATE + appWidgetId).apply();
        prefs.remove(PREF_PREFIX_ID_KEY + appWidgetId).apply();
    }
}
