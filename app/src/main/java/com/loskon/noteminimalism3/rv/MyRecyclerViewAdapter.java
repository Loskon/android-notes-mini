 package com.loskon.noteminimalism3.rv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import androidx.recyclerview.widget.RecyclerView;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.helper.GetSizeItem;
import com.loskon.noteminimalism3.helper.MyColor;
import com.loskon.noteminimalism3.helper.GetDate;
import com.loskon.noteminimalism3.helper.MyIntent;
import com.loskon.noteminimalism3.db.DbAdapter;
import com.loskon.noteminimalism3.model.Note;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

/**
 *
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<NoteViewHolder>
           {

    private final DbAdapter dbAdapter;

    private final List<Note> notes;
    private final ArrayList <Note> toRemoveNotesList = new ArrayList<>();
    protected Stack<View> cachedViews = new Stack<>();
    private final Context context;

    private boolean isSelectionModeOn;
    private final boolean isOneSizeOn;
    private boolean isTypeNotesSingleOn;

    private int color, border, radius;
    private int radius_dp;
    private int stoke_dp;
    private final int selNotesCategory; // Выбранный режим заметок
    private final int numOfLines;
    private int numSelItem;
    private final int fontSize;
    private final int dateFontSize;

    @SuppressWarnings("FieldCanBeLocal")
    private final int NUM_CACHED_VIEWS = 6;

    private CallbackDelMode callbackDelMode; // Поле слушателя для обратного вызова

    // setting the listener
    public void setCallbackDelMode(CallbackDelMode callbackDelMode)    {
        this.callbackDelMode = callbackDelMode;
    }

    public MyRecyclerViewAdapter(Context context, List<Note> notes, DbAdapter dbAdapter,
                                 int selNotesCategory, boolean isSelectionModeOn,
                                 boolean isTypeNotesSingleOn, int numOfLines, boolean isOneSizeOn,
                                 int fontSize, int dateFontSize) {
        this.context = context;
        this.notes = notes;
        this.dbAdapter = dbAdapter;
        this.selNotesCategory = selNotesCategory;
        this.isSelectionModeOn = isSelectionModeOn;
        this.isTypeNotesSingleOn = isTypeNotesSingleOn;
        this.numOfLines = numOfLines;
        this.isOneSizeOn = isOneSizeOn;
        this.fontSize = fontSize;
        this.dateFontSize = dateFontSize;

        //Toast.makeText(context, "hi" + fontSize, Toast.LENGTH_SHORT).show();

        initSettings();
    }

    private void initSettings() {
        radius_dp = GetSizeItem.getRadiusLinLay(context);
        stoke_dp = GetSizeItem.getStrokeLinLay(context);
    }

    public void setTypeOfNotes(boolean isTypeNotesSingleOn) {
        this.isTypeNotesSingleOn = isTypeNotesSingleOn;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View recyclerViewItem = layoutInflater.inflate(R.layout.card_view_item,
                parent, false);

        AsyncLayoutInflater asyncLayoutInflater = new AsyncLayoutInflater(parent.getContext());
        for (int i = 0; i < NUM_CACHED_VIEWS; i++) {
            asyncLayoutInflater.inflate(R.layout.card_view_item,
                     parent, inflateListener);
        }

        recyclerViewItem.setOnClickListener(v -> handleItemClick( (RecyclerView) parent, v));
        recyclerViewItem.setOnLongClickListener(v -> {
            handleItemLongClick( (RecyclerView) parent, v);
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
            holder.date.setText(GetDate.getNowDate(note.getDate()));

            holder.title.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            holder.date.setTextSize(TypedValue.COMPLEX_UNIT_SP, dateFontSize);

            holder.title.setMaxLines(numOfLines);

            if (!isTypeNotesSingleOn || !isOneSizeOn) {
                if (isOneSizeOn) {
                    holder.title.setMinLines(numOfLines);
                }
            } else {
                holder.title.setMinLines(1);
            }

            holder.view.setBackgroundTintList(ColorStateList
                    .valueOf(MyColor.getColorCustom(context)));

            if (note.getFavoritesItem()) {
                holder.view.setVisibility(View.VISIBLE);
            } else {
                holder.view.setVisibility(View.INVISIBLE);
            }

            if (note.getSelectItemForDel()) {
                varForGradientDrawable(radius_dp, stoke_dp, MyColor.getColorCustom(context));
            } else {
                varForGradientDrawable(0, 0, Color.TRANSPARENT);
            }

            // Сброс всех выделенных переменных
            if (!isSelectionModeOn) {
                note.setSelectItemForDel(false);
                varForGradientDrawable(0, 0, Color.TRANSPARENT);
            }

            gradientDrawable.setCornerRadius(radius);
            gradientDrawable.setStroke(border, color);
            holder.linearLayoutCard.setBackground(gradientDrawable);

        }
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    private void methodItemSelection(Note note, int position) {

        if (note.getSelectItemForDel()) {
            numSelItem--;
            removeItemFromRemoveList(note);
        } else {
            numSelItem++;
            addItemInRemoveList(note);
        }

        checkSelectAll();
        notifyItemChanged(position);
    }

    private void varForGradientDrawable(int radius, int border, int color) {
        this.radius = radius;
        this.border = border;
        this.color = color;
    }

    private void handleItemClick(RecyclerView recyclerView, View itemView) {
        int position = recyclerView.getChildLayoutPosition(itemView);
        Note note  = notes.get(position);

        if (note != null) {
            if (isSelectionModeOn) {
                methodItemSelection(note, position);
            } else {
                Intent intent = MyIntent.intentOpenNote(context, selNotesCategory, note.getId());
                context.startActivity(intent);
            }
        }
    }

    private void handleItemLongClick(RecyclerView recyclerView, View itemView) {
        int position = recyclerView.getChildLayoutPosition(itemView);
        Note note = notes.get(position);

        if (note != null) {
            if (!isSelectionModeOn) {
                isSelectionModeOn = true;
                numSelItem = 0;
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

        if (isDelete) {
            // Удаление
            dbAdapter.open();
            for (Note note : toRemoveNotesList) {
                if (selNotesCategory == 2) {
                    dbAdapter.deleteNote(note.getId());
                } else {
                    note.setFavoritesItem(false);
                    dbAdapter.updateFavorites(note, false);
                    dbAdapter.updateSelectItemForDel(note, true, new Date());
                }
            }
            dbAdapter.close();
            notes.removeAll(toRemoveNotesList);
        }

        for (Note note : toRemoveNotesList) {
            note.setSelectItemForDel(false);
        }

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
                note.setSelectItemForDel(true);
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

    public void selectAll() {
        toRemoveNotesList.clear(); // Очистка от уже добавленных элементов

        if (numSelItem == getItemCount()) {
            numSelItem = 0;
            for (Note note : notes) {
                removeItemFromRemoveList(note);
            }
        } else {
            numSelItem = getItemCount();
            for (Note note : notes) {
                addItemInRemoveList(note);
            }
        }

        checkSelectAll();
        notifyDataSetChanged();
    }

    private void addItemInRemoveList(Note note) {
        note.setSelectItemForDel(true);
        toRemoveNotesList.add(note);
    }

    private void removeItemFromRemoveList(Note note) {
        note.setSelectItemForDel(false);
        toRemoveNotesList.remove(note);
    }

    private void checkSelectAll() {
        if (callbackDelMode != null) {
            callbackDelMode.onCallbackClick2(numSelItem == getItemCount());
            callbackDelMode.onCallbackClick3(numSelItem);
        }
    }
}
