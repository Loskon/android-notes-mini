package com.loskon.noteminimalism3.ui.widgets.note;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.other.MyDate;
import com.loskon.noteminimalism3.model.Note;

import java.util.List;

/**
 * Адаптер для списка с выбором заметки для виджета
 */

public class NoteWidgetAdapter extends ArrayAdapter<Note> {

    private final Context context;
    private final List<Note> notes;
    private int color;

    public NoteWidgetAdapter(Context context, int layoutResourceId, List<Note> notes) {
        super(context, layoutResourceId, notes);
        this.context = context;
        this.notes = notes;
    }

    public void setColorViewFav(int color) {
        this.color = color;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View outerContainer = convertView;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        if (convertView == null) {
            outerContainer = inflater.inflate(R.layout.inc_card_view_notes, parent, false);
        }

        TextView title =  outerContainer.findViewById(R.id.txt_title_card_note);
        TextView date =  outerContainer.findViewById(R.id.txt_date_card_note);
        View view =  outerContainer.findViewById(R.id.viewFavForCard);

        title.setText(notes.get(position).getTitle().trim());
        date.setText(MyDate.getNowDate(notes.get(position).getDate()));
        view.setBackgroundTintList(ColorStateList.valueOf(color));

        if (notes.get(position).getFavoritesItem()) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.INVISIBLE);
        }

        return outerContainer;
    }
}
