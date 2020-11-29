package com.loskon.noteminimalism3.rv;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.loskon.noteminimalism3.others.Callback;
import com.loskon.noteminimalism3.activity.MainActivity;
import com.loskon.noteminimalism3.model.Note;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.activity.NoteActivity;
import com.loskon.noteminimalism3.db.DbAdapter;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */

public class SwipeRecyclerViewAdapter extends RecyclerSwipeAdapter<NoteViewHolder> implements Callback {

    private final List<Note> mNoteList;
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final DbAdapter dbAdapter;
    private boolean isSelectionMode;
    private int color, border, radius;
    private final int dpRadius;
    private final int dpStroke;
    private final int selectedNoteMode;
    private int starVisible;
    private Callback callbackListenerSwipeAdapter; // listener field
    private final ArrayList <Note> toRemoveList = new ArrayList<>();
    private Date date = new Date();

    // setting the listener
    public void setCallbackListenerSwipeAdapter(Callback callbackListenerSwipeAdapter)    {
        this.callbackListenerSwipeAdapter = callbackListenerSwipeAdapter;
    }

    public SwipeRecyclerViewAdapter(Context context, List<Note> notes,
                                    int selectedNoteMode, boolean isSelectionMode) {
        mContext = context;
        mNoteList = notes;
        this.selectedNoteMode = selectedNoteMode;
        this.isSelectionMode = isSelectionMode;
        mLayoutInflater = LayoutInflater.from(context);
        ((MainActivity) mContext).setCallbackListenerMain(this);
        dbAdapter = new DbAdapter(mContext);
        dpRadius = (int) mContext.getResources().getDimension(R.dimen.corner_radius);
        dpStroke = (int) mContext.getResources().getDimension(R.dimen.border_stroke);
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View recyclerViewItem = mLayoutInflater.inflate(R.layout.swipe_item_note,
                    parent, false);
        return new NoteViewHolder(recyclerViewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {

        final Note note = mNoteList.get(position);
        GradientDrawable gradientDrawable = new GradientDrawable();

        //try () catch ()

        holder.title.setText(note.getTitle());
        holder.date.setText(DateFormat.getDateTimeInstance(
                DateFormat.SHORT, DateFormat.SHORT).format(note.getDate()));

        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right,
                holder.swipeLayout.findViewById(R.id.swipe_wrapper_right));
        holder.swipeLayout.setClickToClose(true);

        if (note.getFavoritesItem() && selectedNoteMode !=2) {
            starVisible = View.VISIBLE;
            holder.imgFavorites.setImageResource(R.drawable.baseline_star_black_24);
        }
        else {
            starVisible = View.INVISIBLE;
            holder.imgFavorites.setImageResource(R.drawable.baseline_star_border_black_24);
        }
        holder.imgStarFavorites.setVisibility(starVisible);

        if (selectedNoteMode == 2) {
            holder.imgFavorites.setImageResource(R.drawable.baseline_restore_from_trash_black_24);
            holder.imgDelete.setImageResource(R.drawable.baseline_delete_forever_black_24);
        }

        if (note.getSelectItemForDel()) {
            varForGradientDrawable(dpRadius, dpStroke,Color.GRAY);
        } else {
            varForGradientDrawable(0, 0,Color.TRANSPARENT);
        }

        // Сброс всех выделенных переменных
        if (!isSelectionMode) {
            note.setSelectItemForDel(false);
            varForGradientDrawable(0, 0, Color.TRANSPARENT);
        }

        gradientDrawable.setCornerRadius(radius);
        gradientDrawable.setStroke(border, color);
        holder.constraint.setBackground(gradientDrawable);
        holder.swipeLayout.setRightSwipeEnabled(!isSelectionMode);

        holder.swipeLayout.getSurfaceView().setOnClickListener(view -> {
            if (!isSelectionMode) {
                mItemManger.closeAllItems();
                Intent intent = new Intent(mContext, NoteActivity.class);
                intent.putExtra("selectedNoteMode", selectedNoteMode);
                intent.putExtra("id", note.getId());
                mContext.startActivity(intent);
            } else {
                jff(note,position);
            }
        });

        holder.swipeLayout.getSurfaceView().setOnLongClickListener(view -> {
            if (!isSelectionMode) {
                isSelectionMode = true;
                notifyDataSetChanged();
                if(callbackListenerSwipeAdapter !=null) callbackListenerSwipeAdapter.onCallbackClick(true);
            }
                jff(note,position);
            return true;
        });

        holder.imgFavorites.setOnClickListener(view -> {
            holder.swipeLayout.close();


            dbAdapter.open();
            if (selectedNoteMode == 2) {
                holder.swipeLayout.postDelayed(() -> {
                    mItemManger.removeShownLayouts(holder.swipeLayout);
                    mNoteList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mNoteList.size());
                    mItemManger.closeAllItems();
                }, 400);
                dbAdapter.updateSelectItemForDel(note, false, date);
            } else {
                if (note.getFavoritesItem()){
                    dbAdapter.updateFavorites(note,false);
                    note.setFavoritesItem(false);
                } else {
                    dbAdapter.updateFavorites(note,true);
                    note.setFavoritesItem(true);
                }
                holder.swipeLayout.postDelayed(() -> kasf(position), 400);
            }
            dbAdapter.close();

        });

        holder.imgDelete.setOnClickListener(view -> {
            holder.swipeLayout.close();

            holder.swipeLayout.postDelayed(() -> {
                mItemManger.removeShownLayouts(holder.swipeLayout);
                mNoteList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mNoteList.size());
                mItemManger.closeAllItems();
            }, 400);

            note.setSelectItemForDel(true);
            note.setFavoritesItem(false);

            dbAdapter.open();
            if (selectedNoteMode == 2) {
                dbAdapter.deleteNote(note.getId());
            } else {
                dbAdapter.updateSelectItemForDel(note, true, date);
                dbAdapter.updateFavorites(note,false);
            }
            dbAdapter.close();
        });

        // Для того, чтобы был открыт всегда одно свайп меню
        mItemManger.bindView(holder.itemView, position);
    }

    private void jff (Note note, int position) {

        dbAdapter.open();
        if (note.getSelectItemForDel()) {
            if(selectedNoteMode !=2) dbAdapter.updateSelectItemForDel(note, false, date);
            toRemoveList.remove(note);
            note.setSelectItemForDel(false);
        }
        else {
            if(selectedNoteMode !=2) dbAdapter.updateSelectItemForDel(note,true, date);
            toRemoveList.add(note);
            note.setSelectItemForDel(true);

        }
        dbAdapter.close();

        kasf(position);
    }

    private void kasf (int position) {
        notifyItemChanged(position);
        mItemManger.closeAllItems();
    }

    private void varForGradientDrawable (int radius, int border, int color) {
        this.radius=radius;
        this.border=border;
        this.color=color;
    }

    @Override
    public int getItemCount() {
        return mNoteList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_layout;
    }

    @Override
    public void onCallbackClick(boolean deleteOrClose) {
        isSelectionMode = false;
        dbAdapter.open();
        if (deleteOrClose) {
            for (Note note:mNoteList) {
                if (selectedNoteMode == 2) {
                    dbAdapter.deleteNote(note.getId());
                } else {
                    dbAdapter.updateFavorites(note, false);
                }
            }
            mNoteList.removeAll(toRemoveList);
        } else {
            for (Note note:mNoteList) {
                if (selectedNoteMode !=2) dbAdapter.updateSelectItemForDel(note, false, note.getDate());
            }
        }
        dbAdapter.close();
        toRemoveList.clear();
        notifyDataSetChanged();
    }
}
