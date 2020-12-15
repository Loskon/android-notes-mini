package com.loskon.noteminimalism3.rv;

import android.content.Context;
import android.content.Intent;
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
import com.loskon.noteminimalism3.ui.activity.NoteActivity;
import com.loskon.noteminimalism3.db.DbAdapter;
import com.loskon.noteminimalism3.model.Note;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

/**
 *
 */

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<NoteViewHolder>
      {

    private final DbAdapter dbAdapter;

    private final List<Note> notes;
    private final ArrayList <Note> toRemoveList = new ArrayList<>();
    protected Stack<View> cachedViews = new Stack<>();
    private final Context context;

    private boolean isSelectionModeOn;
    private int color, border, radius;

    private final int radius_dp;
    private final int stoke_dp;
    private final int selNotesCategory; // Выбранный режим заметок
    private final Date date = new Date(); // Устанавливаем дату

    @SuppressWarnings("FieldCanBeLocal")
    private final int NUM_CACHED_VIEWS = 6;

    private Callback callbackListenerSwipeAdapter; // Поле слушателя для обратного вызова

    private static CallbackColor3 callbackColor3;

    public void registerCallBack3(CallbackColor3 callbackColor3){
        this.callbackColor3 = callbackColor3;
    }



    public interface CallbackColor3{
        void callingBackColor3(Note note, int position);
    }


    // setting the listener
    public void setCallbackListenerSwipeAdapter(Callback callbackListenerSwipeAdapter)    {
        this.callbackListenerSwipeAdapter = callbackListenerSwipeAdapter;
    }

    public CustomRecyclerViewAdapter(Context context, List<Note> notes,
                                     int selNotesCategory, boolean isSelectionModeOn) {
        this.context = context;
        this.notes = notes;
        this.selNotesCategory = selNotesCategory;
        this.isSelectionModeOn = isSelectionModeOn;

        dbAdapter = new DbAdapter(this.context);
        radius_dp = (int) this.context.getResources().getDimension(R.dimen.corner_radius);
        stoke_dp = (int) this.context.getResources().getDimension(R.dimen.border_stroke);
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

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {

        final Note note = notes.get(position);
        GradientDrawable gradientDrawable = new GradientDrawable();

        //try () catch ()

        holder.title.setText(note.getTitle());
        holder.date.setText(DateFormat.getDateTimeInstance(
                DateFormat.SHORT, DateFormat.SHORT).format(note.getDate()));

        if (note.getFavoritesItem() && selNotesCategory != 2) {
           holder.view.setVisibility(View.VISIBLE);
        }
        else {
           holder.view.setVisibility(View.INVISIBLE);
        }
        //holder.imgStarFavorites.setVisibility(starVisible);

        if (note.getSelectItemForDel()) {
            varForGradientDrawable(radius_dp, stoke_dp,Color.GRAY);
        } else {
            varForGradientDrawable(0, 0,Color.TRANSPARENT);
        }

        // Сброс всех выделенных переменных
        if (!isSelectionModeOn) {
            note.setSelectItemForDel(false);
            varForGradientDrawable(0, 0, Color.TRANSPARENT);
        }

        gradientDrawable.setCornerRadius(radius);
        gradientDrawable.setStroke(border, color);
        holder.constraint.setBackground(gradientDrawable);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    private void methodItemSelection(Note note, int position) {

        dbAdapter.open();
        if (note.getSelectItemForDel()) {

            // Очистка заметки от удаления
            if (selNotesCategory != 2) {
                dbAdapter.updateSelectItemForDel(note, false, date);
            }

            toRemoveList.remove(note);
            note.setSelectItemForDel(false);
        } else {

            // Добавление заметки для удаления
            if (selNotesCategory != 2) {
                dbAdapter.updateSelectItemForDel(note, true, date);
            }

            toRemoveList.add(note);
            note.setSelectItemForDel(true);

        }
        dbAdapter.close();
        notifyItemChanged(position);
    }

    private void varForGradientDrawable(int radius, int border, int color) {
        this.radius = radius;
        this.border = border;
        this.color = color;
    }

    private void handleRecyclerItemClick(RecyclerView recyclerView, View itemView) {
        int position = recyclerView.getChildLayoutPosition(itemView);
        Note note  = notes.get(position);

        if (note != null) {
            if (!isSelectionModeOn) {
                Intent intent = new Intent(context, NoteActivity.class);
                intent.putExtra("selectedNoteMode", selNotesCategory);
                intent.putExtra("id", note.getId());
                context.startActivity(intent);
            } else {
                methodItemSelection(note, position);
            }
        }
    }

    private void handleRecyclerItemLongClick(RecyclerView recyclerView, View itemView) {
        int position = recyclerView.getChildLayoutPosition(itemView);
        Note note = notes.get(position);
        if (note != null) {
            if (!isSelectionModeOn) {
                isSelectionModeOn = true;
                notifyDataSetChanged();
                if (callbackListenerSwipeAdapter != null)
                    callbackListenerSwipeAdapter.onCallbackClick(true);
            }
            methodItemSelection(note, position);
        }
        Toast.makeText(context, "J: "+ getItemCount(), Toast.LENGTH_SHORT).show();
    }

    public void deleteSelectedItems(boolean isDelete) {
        isSelectionModeOn = false;

        dbAdapter.open();
        if (isDelete) {
            // Удаление
            for (Note note : toRemoveList) {
                if (selNotesCategory == 2) {
                    dbAdapter.deleteNote(note.getId());
                } else {
                    dbAdapter.updateFavorites(note, false);
                }
            }
            notes.removeAll(toRemoveList);

        } else {
            // Отмена удаления
            for (Note note : toRemoveList) {
                if (selNotesCategory != 2)
                    dbAdapter.updateSelectItemForDel(note, false, note.getDate());
            }
        }
        dbAdapter.close();
        toRemoveList.clear();
        notifyDataSetChanged();
    }

    public void resetItem (Note note, int position) {

        dbAdapter.open();
        dbAdapter.updateSelectItemForDel(note, false, note.getDate());
        dbAdapter.close();

        notes.add(position, note);
        notifyItemInserted(position);
        //notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        if (notes.size() == 0) {
            Toast.makeText(context, "0"+ getItemCount(), Toast.LENGTH_SHORT).show();
        } else {

            Note note = notes.get(position);

            dbAdapter.open();
            if (selNotesCategory == 2) {
                dbAdapter.deleteNote(note.getId());
            } else {
                note.setSelectItemForDel(true);
                dbAdapter.updateSelectItemForDel(note,true, new Date());
                callbackColor3.callingBackColor3(note, position);
            }
            dbAdapter.close();

            notes.remove(position);
            notifyItemRemoved(position);


        }
    }
}
