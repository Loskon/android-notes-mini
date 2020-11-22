package com.loskon.noteminimalism3.rv;

import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.loskon.noteminimalism3.R;


// Унаследовали наш адаптер от RecyclerView.Adapter
// Здесь же указали наш собственный ViewHolder, который предоставит нам доступ к View-компонентам
public class NoteViewHolder extends   RecyclerView.ViewHolder {

    // Ваш ViewHolder должен содержать переменные для всех
    // View-компонентов, которым вы хотите задавать какие-либо свойства
    // в процессе работы пользователя со списком

    ConstraintLayout constraint;
    CardView cardView;
    SwipeLayout swipeLayout;
    TextView title;
    TextView date;
    ImageView imgFavorites;
    ImageView imgDelete;
    ImageView imgStarFavorites;

    // Мы также создали конструктор, который принимает на вход View-компонент строкИ
    // и ищет все дочерние компоненты
    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);

        title =  itemView.findViewById(R.id.title_item_note);
        date =  itemView.findViewById(R.id.date_item_note);

        constraint =  itemView.findViewById(R.id.constarint);
        cardView =  itemView.findViewById(R.id.card_view);
        swipeLayout =  itemView.findViewById(R.id.swipe_layout);
        imgFavorites =  itemView.findViewById(R.id.img_note_favorites);
        imgDelete =  itemView.findViewById(R.id.img_note_delete);
        imgStarFavorites =  itemView.findViewById(R.id.img__note_star_favorites);
    }

}