package com.loskon.noteminimalism3.ui.snackbars;

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
import com.loskon.noteminimalism3.auxiliary.other.MyColor;
import com.loskon.noteminimalism3.model.Note;
import com.loskon.noteminimalism3.ui.recyclerview.MyRecyclerViewAdapter;

/**
 * Кастомный Snackbar с обратным таймером
 */

public class MySnackbarMain {

    private final Activity activity;
    private final MyRecyclerViewAdapter rvAdapter;
    private final FloatingActionButton fabMain;
    private final CoordinatorLayout coordinatorLayout;

    private Snackbar snackbarMain;
    private CountDownTimer countDownTimer;

    public MySnackbarMain(Activity activity, MyRecyclerViewAdapter rvAdapter,
                          FloatingActionButton fabMain, CoordinatorLayout coordinatorLayout) {
        this.activity = activity;
        this.rvAdapter = rvAdapter;
        this.fabMain = fabMain;
        this.coordinatorLayout = coordinatorLayout;
    }

    @SuppressLint("InflateParams")
    public void showSnackbar(Note note, int position) {
        if (countDownTimer != null) {
            countDownTimer.cancel(); // Сброс таймера
        }

        snackbarMain = Snackbar.make(coordinatorLayout, "", Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbarMain.getView();

        snackbarMain.setAnchorView(fabMain);

        snackbarMain.setBackgroundTint(MyColor.getColorBackgroundSnackbar(activity));

        LayoutInflater objLayoutInflater = (LayoutInflater)
                activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View snackView = objLayoutInflater.inflate(R.layout.snackbar_swipe, null);

        Button btnSnackbar =  snackView.findViewById(R.id.snackbar_btn);
        ProgressBar progressBar =  snackView.findViewById(R.id.snackbar_progress_bar);
        TextView textProgress =  snackView.findViewById(R.id.snackbar_text_progress);

        //progressBar.getProgressDrawable().setColorFilter(
        //Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);

        progressBar.setProgress(0);
        progressBar.setMax(10000);

        btnSnackbar.setTextColor(MyColor.getMyColor(activity));
        btnSnackbar.setOnClickListener(v -> {
            closeSnackbar();
            (new Handler()).postDelayed(() -> {
                rvAdapter.onResetItem(note, position);
            }, 100);
        });

        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar,
                "progress", 10000);
        animation.setDuration(4900); // 4 second
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();

        countDownTimer = new CountDownTimer(5 * 1000, 100) {
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

        snackbarMain.show();
    }

    public void closeSnackbar() {
        snackbarMain.dismiss();
        countDownTimer.cancel();
    }
}
