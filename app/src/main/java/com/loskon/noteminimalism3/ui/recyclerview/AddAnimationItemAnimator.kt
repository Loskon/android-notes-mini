package com.loskon.noteminimalism3.ui.recyclerview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

/**
 * ItemAnimator с избавлением от бага с ripple эффектом для CardView
 */
class AddAnimationItemAnimator : DefaultItemAnimator() {

    override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
        val view = holder.itemView

        endAnimation(holder)
        view.alpha = 0f

        val animation = view.animate()
        animation.alpha(1f).setDuration(0).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animator: Animator) {
                dispatchAddStarting(holder)
            }

            override fun onAnimationCancel(animator: Animator) {
                view.alpha = 1f
            }

            override fun onAnimationEnd(animator: Animator) {
                animation.setListener(null)
                dispatchAddFinished(holder)
            }
        }).start()

        return true
    }
}