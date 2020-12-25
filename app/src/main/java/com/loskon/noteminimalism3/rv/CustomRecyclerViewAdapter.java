 package com.loskon.noteminimalism3.rv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import androidx.recyclerview.widget.RecyclerView;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.ui.Helper.ColorHelper;
import com.loskon.noteminimalism3.ui.Helper.DateHelper;
import com.loskon.noteminimalism3.ui.Helper.MainHelper;
import com.loskon.noteminimalism3.ui.Helper.SharedPrefHelper;
import com.loskon.noteminimalism3.db.DbAdapter;
import com.loskon.noteminimalism3.model.Note;
import com.loskon.noteminimalism3.ui.activity.SettingsAppearanceActivity;
import com.loskon.noteminimalism3.ui.preference.item.PrefItemNumOfLines;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

/**
 *
 */

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<NoteViewHolder>
        implements PrefItemNumOfLines.callbackNumOfLines, SettingsAppearanceActivity.CallbackColor34 {

    private final DbAdapter dbAdapter;

    private final List<Note> notes;
    private final ArrayList <Note> toRemoveNotesList = new ArrayList<>();
    protected Stack<View> cachedViews = new Stack<>();
    private final Context context;

    private boolean isSelectionModeOn, isOneSizeOn, isTypeNotesSingleOn;

    private int color, border, radius;
    private int radius_dp;
    private int stoke_dp;
    private final int selNotesCategory; // Выбранный режим заметок
    private int numberOfLines;

    @SuppressWarnings("FieldCanBeLocal")
    private final int NUM_CACHED_VIEWS = 6;

    private Callback callbackDelMode; // Поле слушателя для обратного вызова

    // setting the listener
    public void setCallbackDelMode(Callback callbackDelMode)    {
        this.callbackDelMode = callbackDelMode;
    }

    public CustomRecyclerViewAdapter(Context context, List<Note> notes, DbAdapter dbAdapter,
                                     int selNotesCategory, boolean isSelectionModeOn,
                                     boolean isTypeNotesSingleOn) {
        this.context = context;
        this.notes = notes;
        this.dbAdapter = dbAdapter;
        this.selNotesCategory = selNotesCategory;
        this.isSelectionModeOn = isSelectionModeOn;
        this.isTypeNotesSingleOn = isTypeNotesSingleOn;

        initSettings();
    }

    private void initSettings() {
        radius_dp = (int) this.context.getResources().getDimension(R.dimen.corner_radius);
        stoke_dp = (int) this.context.getResources().getDimension(R.dimen.border_stroke);

        (new PrefItemNumOfLines(context)).registerCallbackNumOfLines(this);
        numberOfLines = SharedPrefHelper.loadInt(context,
                SharedPrefHelper.KEY_NUM_OF_LINES, 3);

        (new SettingsAppearanceActivity()).registerCallBack34(this);
        isOneSizeOn = SharedPrefHelper.loadBoolean(context,
                SharedPrefHelper.KEY_ONE_SIZE, false);
    }

    public void setTypeOfNotes(boolean isTypeNotesSingleOn) {
        this.isTypeNotesSingleOn = isTypeNotesSingleOn;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View recyclerViewItem = layoutInflater.inflate(R.layout.card_for_swipe_item_note6,
                parent, false);

        AsyncLayoutInflater asyncLayoutInflater = new AsyncLayoutInflater(parent.getContext());
        for (int i = 0; i < NUM_CACHED_VIEWS; i++) {
            asyncLayoutInflater.inflate(R.layout.card_for_swipe_item_note6,
                     parent, inflateListener);
        }

        recyclerViewItem.setOnClickListener(v -> handleRecyclerItemClick( (RecyclerView) parent, v));
        recyclerViewItem.setOnLongClickListener(v -> {
            handleRecyclerItemLongClick( (RecyclerView) parent, v);
            return  true;
        });

        //View recyclerViewItem;
       // if (cachedViews.isEmpty()) {
          //  recyclerViewItem = layoutInflater.inflate(R.layout.card_for_swipe_item_note6,
                //    parent, false);
        //} else {
          //  recyclerViewItem = cachedViews.pop();
        //}
        return new NoteViewHolder(recyclerViewItem);
    }

    private final AsyncLayoutInflater.OnInflateFinishedListener inflateListener =
            (view, resId, parent) -> cachedViews.push(view);

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {

        final Note note = notes.get(position);
        GradientDrawable gradientDrawable = new GradientDrawable();

        if (note != null) {

            holder.title.setText(note.getTitle().trim());
            holder.date.setText(DateHelper.getNowDate(note.getDate()));

            holder.title.setMaxLines(numberOfLines);

            if (!isTypeNotesSingleOn || !isOneSizeOn) {
                if (isOneSizeOn) {
                    holder.title.setMinLines(numberOfLines);
                }
            } else {
                holder.title.setMinLines(1);
            }

            holder.view.setBackgroundTintList(ColorStateList
                    .valueOf(ColorHelper.getColorCustom(context)));


            if (note.getFavoritesItem() && selNotesCategory != 2) {
                holder.view.setVisibility(View.VISIBLE);
            } else {
                holder.view.setVisibility(View.INVISIBLE);
            }

            if (note.getSelectItemForDel()) {
                varForGradientDrawable(radius_dp, stoke_dp, Color.GRAY);
            } else {
                varForGradientDrawable(0, 0, Color.TRANSPARENT);
            }

            // Сброс всех выделенных переменных
            if (!isSelectionModeOn) {
                note.isSelectItemForDel(false);
                varForGradientDrawable(0, 0, Color.TRANSPARENT);
            }

            gradientDrawable.setCornerRadius(radius);
            gradientDrawable.setStroke(border, color);
            holder.constraint.setBackground(gradientDrawable);
        }
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    private void methodItemSelection(Note note, int position) {

        dbAdapter.open();
        if (note.getSelectItemForDel()) {

            numberItem--;

            // Очистка заметки от удаления
            if (selNotesCategory != 2) {
                dbAdapter.updateSelectItemForDel(note, false, new Date());
            }

            toRemoveNotesList.remove(note);
            note.isSelectItemForDel(false);
        } else {

            numberItem++;

            // Добавление заметки для удаления
            if (selNotesCategory != 2) {
                dbAdapter.updateSelectItemForDel(note, true, new Date());
            }

            toRemoveNotesList.add(note);
            note.isSelectItemForDel(true);

        }
        dbAdapter.close();

        selectAll2();
        notifyItemChanged(position);
    }

    private void varForGradientDrawable(int radius, int border, int color) {
        this.radius = radius;
        this.border = border;
        this.color = color;
    }

    @Override
    public void callingBackNumOfLines(int numOfLines) {
        this.numberOfLines = numOfLines;
    }

    @Override
    public void callingBackColor34(boolean isOneSizeOn) {
        this.isOneSizeOn = isOneSizeOn;
    }

    private void handleRecyclerItemClick(RecyclerView recyclerView, View itemView) {
        int position = recyclerView.getChildLayoutPosition(itemView);
        Note note  = notes.get(position);

        if (note != null) {
            if (isSelectionModeOn) {
                methodItemSelection(note, position);
            } else {
                MainHelper.intentOpenNote(context, selNotesCategory, note.getId());
            }
        }
    }

    private void handleRecyclerItemLongClick(RecyclerView recyclerView, View itemView) {
        int position = recyclerView.getChildLayoutPosition(itemView);
        Note note = notes.get(position);

        if (note != null) {
            if (!isSelectionModeOn) {
                isSelectionModeOn = true;
                numberItem = 0;
                //notifyDataSetChanged();
                if (callbackDelMode != null) {
                    callbackDelMode.onCallbackClick(true);
                }
            }
            methodItemSelection(note, position);
        }
    }

    public void deleteSelectedItems(boolean isDelete) {
        isSelectionModeOn = false;

        try {

        dbAdapter.open();
        if (isDelete) {
            // Удаление
            for (Note note : toRemoveNotesList) {
                if (selNotesCategory == 2) {
                    dbAdapter.deleteNote(note.getId());
                } else {
                    dbAdapter.updateFavorites(note, false);
                }
            }
            notes.removeAll(toRemoveNotesList);

        } else {
            // Отмена удаления
            for (Note note : toRemoveNotesList) {
                if (selNotesCategory != 2)
                    dbAdapter.updateSelectItemForDel(note, false, note.getDate());
            }
        }
        dbAdapter.close();

        toRemoveNotesList.clear();
        notifyDataSetChanged();

        } catch (Exception e) {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void resetItem (Note note, int position) {
        try {
            dbAdapter.open();
            dbAdapter.updateSelectItemForDel(note, false, note.getDate());
            dbAdapter.close();

            notes.add(position, note);
            notifyItemInserted(position);
            //notifyDataSetChanged();

        } catch (Exception e) {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void deleteItem(Note note, int position) {
        try {
            dbAdapter.open();
            if (selNotesCategory == 2) {
                dbAdapter.deleteNote(note.getId());
            } else {
                note.isSelectItemForDel(true);
                dbAdapter.updateSelectItemForDel(note, true, new Date());
                dbAdapter.updateFavorites(note, false);
            }
            dbAdapter.close();

            notes.remove(position);
            notifyItemRemoved(position);

        } catch (Exception e) {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public ArrayList <Note> getNotes() {
        return (ArrayList<Note>) notes;
    }

    private int numberItem;

    public void selectAll() {

        if (numberItem == notes.size()) {
            for (Note note : notes) {
                numberItem = 0;
                note.isSelectItemForDel(false);
                toRemoveNotesList.remove(note);
            }
        } else {
            for (Note note : notes) {
                numberItem = notes.size();
                note.isSelectItemForDel(true);
                toRemoveNotesList.add(note);
            }
        }

        selectAll2();
        notifyDataSetChanged();
    }

    private void selectAll2() {
        callbackDelMode.onCallbackClick2(numberItem == notes.size());
    }
}
