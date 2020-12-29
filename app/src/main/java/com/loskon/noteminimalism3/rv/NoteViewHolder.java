package com.loskon.noteminimalism3.rv;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.loskon.noteminimalism3.R;

/**
 * Наш собственный ViewHolder, который предоставляет доступ к View-компонентам
 */

public class NoteViewHolder extends RecyclerView.ViewHolder {

    LinearLayout linearLayoutCard;
    CardView cardView;
    TextView title;
    TextView date;
    View view;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.title_item_note);
        date = itemView.findViewById(R.id.date_item_note);

        linearLayoutCard = itemView.findViewById(R.id.linLayCard);
        cardView = itemView.findViewById(R.id.card_view);
        view = itemView.findViewById(R.id.view);
    }
}