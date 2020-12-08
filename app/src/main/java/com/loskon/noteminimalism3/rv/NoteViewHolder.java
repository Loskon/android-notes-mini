package com.loskon.noteminimalism3.rv;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.loskon.noteminimalism3.R;

/**
 * Наш собственный ViewHolder, который предоставляет доступ к View-компонентам
 */

public class NoteViewHolder extends RecyclerView.ViewHolder {

    LinearLayout constraint;
    CardView cardView;
    TextView title;
    TextView date;
    //ImageView imgStarFavorites;
    View view;

    // Мы также создали конструктор, который принимает на вход View-компонент строкИ
    // и ищет все дочерние компоненты
    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.title_item_note);
        date = itemView.findViewById(R.id.date_item_note);

        constraint = itemView.findViewById(R.id.linInCard);
        cardView = itemView.findViewById(R.id.card_view);
        view = itemView.findViewById(R.id.view);
        //imgStarFavorites =  itemView.findViewById(R.id.img__note_star_favorites);
    }

}