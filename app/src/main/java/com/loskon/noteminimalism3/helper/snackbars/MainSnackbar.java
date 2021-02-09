package com.loskon.noteminimalism3.helper.snackbars;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.helper.MyColor;
import com.loskon.noteminimalism3.model.Note;
import com.loskon.noteminimalism3.rv.MyRecyclerViewAdapter;

public class MainSnackbar {

    private final Activity activity;
    private final MyRecyclerViewAdapter rvAdapter;
    private final FloatingActionButton fabMain;

    private Snackbar snackbarMain;
    private CountDownTimer countDownTimer;

    public MainSnackbar(Activity activity,
                        MyRecyclerViewAdapter rvAdapter, FloatingActionButton fabMain) {
        this.activity = activity;
        this.rvAdapter = rvAdapter;
        this.fabMain = fabMain;
    }

    @SuppressLint("InflateParams")
    public Snackbar showSnackbar(Note note, int position) {
        if (countDownTimer != null) {
            countDownTimer.cancel(); // Сброс таймера
        }

        CoordinatorLayout coordinatorLayout = activity.findViewById(R.id.coord_layout_main);

        snackbarMain = Snackbar.make(coordinatorLayout, "", Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbarMain.getView();

        snackbarMain.setAnchorView(fabMain);

        snackbarMain.setBackgroundTint(MyColor.getColorBackgroundSnackbar(activity));

        LayoutInflater objLayoutInflater = (LayoutInflater)
                activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View snackView = objLayoutInflater.inflate(R.layout.custom_snackbar, null);
        snackView.setBackgroundColor(MyColor.getColorBackgroundSnackbar(activity));

        Button btnSnackbar =  snackView.findViewById(R.id.snackbar_btn);
        TextView textSnackbar =  snackView.findViewById(R.id.snackbar_text_title);
        ProgressBar progressBar =  snackView.findViewById(R.id.snackbar_progress_bar);
        TextView textProgress =  snackView.findViewById(R.id.snackbar_text_progress);

        //progressBar.getProgressDrawable().setColorFilter(
        //Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);

        textSnackbar.setText(activity.getString(R.string.snackbar_main_text_add_trash));
        progressBar.setProgress(0);
        progressBar.setMax(10000);

        btnSnackbar.setTextColor(MyColor.getColorCustom(activity));
        btnSnackbar.setOnClickListener(v -> {
            closeSnackbar();
            (new Handler()).postDelayed(() -> {
                rvAdapter.resetItem(note, position);
            }, 100);
        });

        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar,
                "progress", 10000);
        animation.setDuration(3900); // 4 second
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();

        countDownTimer = new CountDownTimer(4 * 1000, 100) {
            @Override
            public void onTick(long leftTimeInMilliseconds) {
                long seconds = leftTimeInMilliseconds / 1000;
                textProgress.setText(String.valueOf(seconds));
            }

            @Override
            public void onFinish() {
                snackbarMain.dismiss();
            }
        }.start();

        layout.addView(snackView, 0);

        return snackbarMain;
    }

    public void closeSnackbar() {
        snackbarMain.dismiss();
        countDownTimer.cancel();
    }
}
