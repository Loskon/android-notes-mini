package com.loskon.noteminimalism3.ui.recyclerview.fonts

import android.view.View
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * Snap для установки элемента по центру RecyclerView
 */

class FontSnapHelper : LinearSnapHelper() {

    private var recyclerView: RecyclerView? = null

    override fun attachToRecyclerView(recyclerView: RecyclerView?) {
        super.attachToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    fun setCenterPosition(position: Int) {
        recyclerView?.scrollToPosition(position)
        recyclerView?.visibility = View.INVISIBLE // Костыль
        recyclerView?.post {

            val layoutManager: RecyclerView.LayoutManager? = recyclerView?.layoutManager
            val view: View? = layoutManager?.findViewByPosition(position)

            if (layoutManager != null && view != null) {

                val snapDistance: IntArray? = calculateDistanceToFinalSnap(layoutManager, view)

                if (snapDistance != null) {

                    if (snapDistance[0] != 0 || snapDistance[1] != 0) {
                        recyclerView?.scrollBy(snapDistance[0], snapDistance[1])
                        recyclerView?.visibility = View.VISIBLE // Костыль
                    }
                }
            }
        }
    }
}