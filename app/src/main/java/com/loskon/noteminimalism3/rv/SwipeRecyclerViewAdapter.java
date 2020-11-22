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

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.loskon.noteminimalism3.others.Callback;
import com.loskon.noteminimalism3.activity.MainActivity;
import com.loskon.noteminimalism3.model.Note;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.activity.NoteActivity;
import com.loskon.noteminimalism3.db.DbAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class SwipeRecyclerViewAdapter extends RecyclerSwipeAdapter<NoteViewHolder> implements Callback {

    private final List<Note> mNoteList;
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final DbAdapter adapter;
    private boolean selectionMode = false;
    private int color,border, radius;
    private final int dpRadius;
    private final int dpStroke;
    private int mNoteValMode;
    private int starVisible;
    private Callback listener; // listener field
    private final ArrayList <Note> toRemove= new ArrayList<>();

    // setting the listener
    public void setListener(Callback listener)    {
        this.listener = listener;
    }

    public SwipeRecyclerViewAdapter(Context context, List<Note> notes,int noteValMode) {
        mContext = context;
        mNoteList = notes;
        mNoteValMode = noteValMode;
        mLayoutInflater = LayoutInflater.from(context);
        ((MainActivity) mContext).setListener(this);
        adapter = new DbAdapter(mContext);
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
        holder.date.setText(String.valueOf(note.getDate()));

        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right,
                holder.swipeLayout.findViewById(R.id.swipe_wrapper_right));
        holder.swipeLayout.setClickToClose(true);

        if (note.getFavoritesItem() && mNoteValMode !=2) {
            starVisible = View.VISIBLE;
            holder.imgFavorites.setImageResource(R.drawable.baseline_star_black_24);
        }
        else {
            starVisible = View.INVISIBLE;
            holder.imgFavorites.setImageResource(R.drawable.baseline_star_border_black_24);
        }
        holder.imgStarFavorites.setVisibility(starVisible);

        if (mNoteValMode == 2) {
            holder.imgFavorites.setImageResource(R.drawable.baseline_restore_from_trash_black_24);
            holder.imgDelete.setImageResource(R.drawable.baseline_delete_forever_black_24);
        }

        if (note.getSelectItemForDel()) {
            varForGradientDrawable(dpRadius, dpStroke,Color.GRAY);
        } else {
            varForGradientDrawable(0, 0,Color.TRANSPARENT);
        }

        // Сброс всех выделенных переменных
        if (!selectionMode) {
            note.setSelectItemForDel(false);
            varForGradientDrawable(0, 0, Color.TRANSPARENT);
        }

        gradientDrawable.setCornerRadius(radius);
        gradientDrawable.setStroke(border, color);
        holder.constraint.setBackground(gradientDrawable);
        holder.swipeLayout.setRightSwipeEnabled(!selectionMode);

        holder.swipeLayout.getSurfaceView().setOnClickListener(view -> {
            if (!selectionMode) {
                mItemManger.closeAllItems();
                Intent intent = new Intent(mContext, NoteActivity.class);
                intent.putExtra("id", note.getId());
                mContext.startActivity(intent);
            } else {
                jff(note,position);
            }
        });

        holder.swipeLayout.getSurfaceView().setOnLongClickListener(view -> {
            if (!selectionMode) {
                selectionMode = true;
                notifyDataSetChanged();
                if(listener!=null) listener.onAddClick(true);
            }
                jff(note,position);
            return true;
        });

        holder.imgFavorites.setOnClickListener(view -> {
            holder.swipeLayout.close();


            adapter.open();
            if (mNoteValMode == 2) {
                holder.swipeLayout.postDelayed(() -> {
                    mItemManger.removeShownLayouts(holder.swipeLayout);
                    mNoteList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mNoteList.size());
                    mItemManger.closeAllItems();
                }, 400);
                adapter.updateSelectItemForDel(note, false);
            } else {
                if (note.getFavoritesItem()){
                    adapter.updateFavorites(note,false);
                    note.setFavoritesItem(false);
                } else {
                    adapter.updateFavorites(note,true);
                    note.setFavoritesItem(true);
                }
                holder.swipeLayout.postDelayed(() -> kasf(position), 400);
            }
            adapter.close();

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

            adapter.open();
            if (mNoteValMode == 2) {
                adapter.deleteNote(note.getId());
            } else {
                adapter.updateSelectItemForDel(note, true);
                adapter.updateFavorites(note,false);
            }
            adapter.close();
        });

        // Для того, чтобы был открыт всегда одно свайп меню
        mItemManger.bindView(holder.itemView, position);
    }

    private void jff (Note note, int position) {

        adapter.open();
        if (note.getSelectItemForDel()) {
            if(mNoteValMode !=2) adapter.updateSelectItemForDel(note, false);
            toRemove.remove(note);
            note.setSelectItemForDel(false);
        }
        else {
            if(mNoteValMode !=2) adapter.updateSelectItemForDel(note,true);
            toRemove.add(note);
            note.setSelectItemForDel(true);

        }
        adapter.close();

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
    public void onAddClick(boolean deleteOrClose) {
        selectionMode = false;
        adapter.open();
        if (deleteOrClose) {
            for (Note note:mNoteList) {
                if (mNoteValMode == 2) {
                    adapter.deleteNote(note.getId());
                } else {
                    adapter.updateFavorites(note, false);
                }
            }
            mNoteList.removeAll(toRemove);
        } else {
            for (Note note:mNoteList) {
                if (mNoteValMode !=2) adapter.updateSelectItemForDel(note, false);
            }
        }
        adapter.close();
        toRemove.clear();
        notifyDataSetChanged();
    }
}
