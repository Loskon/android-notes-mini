package com.loskon.noteminimalism3.rv.adapter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.loskon.noteminimalism3.R;

/**
 * Кастомный ViewHolder, который предоставляет доступ к View-компонентам
 */

public class NoteViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout linearLayoutCard;
    public CardView cardView;
    public TextView title;
    public TextView date;
    public View view;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.txt_title_card_note);
        date = itemView.findViewById(R.id.txt_date_card_note);

        linearLayoutCard = itemView.findViewById(R.id.linLytCard);
        cardView = itemView.findViewById(R.id.card_view);
        view = itemView.findViewById(R.id.viewFavForCard);
    }
}