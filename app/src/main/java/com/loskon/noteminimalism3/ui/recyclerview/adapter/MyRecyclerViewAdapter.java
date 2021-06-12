package com.loskon.noteminimalism3.ui.recyclerview.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import androidx.recyclerview.widget.RecyclerView;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.other.MyDate;
import com.loskon.noteminimalism3.auxiliary.other.MyIntent;
import com.loskon.noteminimalism3.auxiliary.other.MySizeItem;
import com.loskon.noteminimalism3.database.DbAdapter;
import com.loskon.noteminimalism3.model.Note;
import com.loskon.noteminimalism3.ui.recyclerview.other.CallbackDelMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

/**
 * Отображение элементов списка, обработка удаления, выделения и фильтр поиска
 */

public class MyRecyclerViewAdapter extends
        RecyclerView.Adapter<NoteViewHolder> implements Filterable {

    private final DbAdapter dbAdapter;

    private List<Note> notes;
    private final ArrayList<Note> toSearchList;
    private final ArrayList<Note> toRemoveList = new ArrayList<>();
    protected Stack<View> cachedViews = new Stack<>();
    private final Context context;

    private boolean isSelectionMode;
    private boolean isOneSize;
    private boolean isTypeNotesSingle;
    private boolean isSearchMode;
    private boolean isFavorite;

    private int colorStroke, borderStroke, radiusStroke;
    private int radiusStroke_dp, boredStroke_dp;
    private int selNotesCategory;
    private int numOfLines;
    private int numSelItem;
    private int fontSize, dateFontSize;
    private int color;

    @SuppressWarnings("FieldCanBeLocal")
    private final int NUM_CACHED_VIEWS = 6;

    private CallbackDelMode callbackDelMode;

    // setting the listener
    public void regCallbackDelMode(CallbackDelMode callbackDelMode) {
        this.callbackDelMode = callbackDelMode;
    }

    public MyRecyclerViewAdapter(Context context, List<Note> notes, DbAdapter dbAdapter) {
        this.context = context;
        this.notes = notes;
        this.dbAdapter = dbAdapter;
        toSearchList = (ArrayList<Note>) notes;
    }

    public void initSettings(int selNotesCategory, boolean isSelectionMode,
                             boolean isTypeNotesSingle, int numOfLines, boolean isOneSize,
                             int fontSize, int dateFontSize, int color) {
        this.selNotesCategory = selNotesCategory;
        this.isSelectionMode = isSelectionMode;
        this.isTypeNotesSingle = isTypeNotesSingle;
        this.numOfLines = numOfLines;
        this.isOneSize = isOneSize;
        this.fontSize = fontSize;
        this.dateFontSize = dateFontSize;
        this.color = color;
    }

    public void initStrokeInDp() {
        radiusStroke_dp = MySizeItem.getRadiusLinLay(context);
        boredStroke_dp = MySizeItem.getStrokeLinLay(context);
    }

    public void setTypeOfNotes(boolean isTypeNotesSingle) {
        this.isTypeNotesSingle = isTypeNotesSingle;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View recyclerViewItem = layoutInflater.inflate(R.layout.inc_card_view_notes,
                parent, false);

        AsyncLayoutInflater asyncLayoutInflater = new AsyncLayoutInflater(parent.getContext());
        for (int i = 0; i < NUM_CACHED_VIEWS; i++) {
            asyncLayoutInflater.inflate(R.layout.inc_card_view_notes,
                    parent, inflateListener);
        }

        recyclerViewItem.setOnClickListener(v -> handlerItemClick((RecyclerView) parent, v));
        recyclerViewItem.setOnLongClickListener(v -> {
            handleItemLongClick((RecyclerView) parent, v);
            return true;
        });

        return new NoteViewHolder(recyclerViewItem);
    }

    private final AsyncLayoutInflater.OnInflateFinishedListener inflateListener =
            (view, resId, parent) -> cachedViews.push(view);

    private void handlerItemClick(RecyclerView recyclerView, View itemView) {
        int position = recyclerView.getChildLayoutPosition(itemView);
        Note note = notes.get(position);

        if (isSelectionMode) {
            methodItemSelection(note, position);
        } else {
            MyIntent.openNote(context, selNotesCategory, note.getId());
        }
    }

    private void handleItemLongClick(RecyclerView recyclerView, View itemView) {
        int position = recyclerView.getChildLayoutPosition(itemView);
        Note note = notes.get(position);

        if (!isSelectionMode) goDeleteMode();
        methodItemSelection(note, position);
    }

    private void goDeleteMode() {
        isSelectionMode = true;
        numSelItem = 0;
        callbackDelMode.onCallBackSelMode(true);
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

    private void removeItemFromRemoveList(Note note) {
        note.setSelectItemForDel(false);
        toRemoveList.remove(note);
    }

    private void addItemInRemoveList(Note note) {
        note.setSelectItemForDel(true);
        toRemoveList.add(note);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {

        final Note note = notes.get(position);
        GradientDrawable gradientDrawable = new GradientDrawable();

        if (note != null) {

            holder.title.setText(note.getTitle().trim());
            holder.date.setText(MyDate.getNowDate(note.getDate()));

            holder.title.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            holder.date.setTextSize(TypedValue.COMPLEX_UNIT_SP, dateFontSize);

            holder.title.setMaxLines(numOfLines);

            if (!isTypeNotesSingle || !isOneSize) {
                if (isOneSize) {
                    holder.title.setMinLines(numOfLines);
                }
            } else {
                holder.title.setMinLines(1);
            }

            holder.view.setBackgroundTintList(ColorStateList.valueOf(color));

            if (note.getFavoritesItem()) {
                holder.view.setVisibility(View.VISIBLE);
            } else {
                holder.view.setVisibility(View.INVISIBLE);
            }

            if (note.getSelectItemForDel()) {
                varForGradientDrawable(radiusStroke_dp, boredStroke_dp, color);
            } else {
                varForGradientDrawable(0, 0, Color.TRANSPARENT);
            }

            gradientDrawable.setCornerRadius(radiusStroke);
            gradientDrawable.setStroke(borderStroke, colorStroke);
            holder.linearLayoutCard.setBackground(gradientDrawable);
        }
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public ArrayList<Note> getNotes() {
        return (ArrayList<Note>) notes;
    }

    private void varForGradientDrawable(int radius, int border, int colorStroke) {
        this.radiusStroke = radius;
        this.borderStroke = border;
        this.colorStroke = colorStroke;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void onExitFromDeleteMode(boolean isDelete) {
        isSelectionMode = false;

        if (isDelete) deleteSelectedItems();
        for (Note note : toRemoveList) note.setSelectItemForDel(false);

        toRemoveList.clear();
        notifyDataSetChanged();
    }

    private void deleteSelectedItems() {
        for (Note note : toRemoveList) selectingDeletionMethod(note);

        notes.removeAll(toRemoveList);
        if (isSearchMode) toSearchList.removeAll(toRemoveList);
    }

    private void selectingDeletionMethod(Note note) {
        dbAdapter.open();

        if (selNotesCategory == 2) {
            dbAdapter.deleteNote(note.getId());
        } else {
            sendToTrash(note);
        }

        dbAdapter.close();
    }

    private void sendToTrash(Note note) {
        isFavorite = note.getFavoritesItem();
        if (isFavorite) isFavorite(note, false);
        dbAdapter.updateItemDel(note, true, note.getDate(), new Date());
    }

    private void isFavorite(Note note, boolean isFav) {
        note.setFavoritesItem(isFav);
        dbAdapter.updateFavorites(note, isFav);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void onResetItem(Note note, int position) {
        dbAdapter.open();
        if (isFavorite) isFavorite(note, true);
        dbAdapter.updateItemDel(note, false, note.getDate(), new Date());
        dbAdapter.close();

        notes.add(position, note);
        if (isSearchMode) toSearchList.add(position, note);

        notifyDataSetChanged();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void onDeleteItem(Note note, int position) {
        selectingDeletionMethod(note);

        notes.remove(position);
        if (isSearchMode) toSearchList.remove(position);

        notifyItemRemoved(position);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void onSelectAllItems() {
        toRemoveList.clear();

        if (numSelItem == getItemCount()) {
            resetSelection();
        } else {
            selectAllItems();
        }

        checkSelectAll();
        notifyDataSetChanged();
    }

    private void resetSelection() {
        // Сбросить выделение со всех элементов
        numSelItem = 0;
        for (Note note : notes) removeItemFromRemoveList(note);
    }

    private void selectAllItems() {
        // Выделить все элементы
        numSelItem = getItemCount();
        for (Note note : notes) addItemInRemoveList(note);
    }

    private void checkSelectAll() {
        callbackDelMode.onCallBackNotAllSelected(numSelItem == getItemCount());
        callbackDelMode.onCallBackNumSel(numSelItem);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void onUnificationItems() {
        StringBuilder stringBuilder = new StringBuilder();
        String title = null;

        for (Note note : toRemoveList) {
            title = combineTitle(note, stringBuilder);
            deleteUnnecessaryItems(note);
        }

        Note note = toRemoveList.get(0);
        updateTitle(note, title);
        toRemoveList.remove(note);

        notes.removeAll(toRemoveList);
        toRemoveList.clear();

        callbackDelMode.onCallBackUni();
    }

    private String combineTitle(Note note, StringBuilder stringBuilder) {
        // Объединение текста
        if (note != toRemoveList.get(toRemoveList.size() - 1)) {
            stringBuilder.append(note.getTitle()).append("\n\n");
        } else {
            stringBuilder.append(note.getTitle());
        }

        return String.valueOf(stringBuilder);
    }

    private void deleteUnnecessaryItems(Note note) {
        // Удаление лишних заметок
        if (note != toRemoveList.get(0)) {
            dbAdapter.open();
            dbAdapter.deleteNote(note.getId());
            dbAdapter.close();
        }
    }

    private void updateTitle(Note note, String title) {
        // Обновить текст заметки
        dbAdapter.open();
        dbAdapter.updateTitle(note, title);
        dbAdapter.close();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    isSearchMode = false;
                    notes = toSearchList;
                } else {
                    isSearchMode = true;
                    ArrayList<Note> filteredList = new ArrayList<>();

                    for (Note note : toSearchList) {
                        if (note.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(note);
                        }
                    }

                    notes = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = notes;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                notes = (ArrayList<Note>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
