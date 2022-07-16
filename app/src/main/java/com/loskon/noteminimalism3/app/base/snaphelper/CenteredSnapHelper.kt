package com.loskon.noteminimalism3.app.base.snaphelper

import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * Snap to set the element in the center of the RecyclerView
 */
class CenteredSnapHelper : PagerSnapHelper() {

    private var recyclerView: RecyclerView? = null

    override fun attachToRecyclerView(recyclerView: RecyclerView?) {
        this.recyclerView = recyclerView
        super.attachToRecyclerView(recyclerView)
    }

    fun setCenterPosition(position: Int) {
        recyclerView?.layoutManager?.scrollToPosition(position)

        recyclerView?.doOnPreDraw {
            val layoutManager: RecyclerView.LayoutManager? = recyclerView?.layoutManager
            val view: View? = layoutManager?.findViewByPosition(position)

            if (layoutManager != null && view != null) {
                val snapDistance: IntArray? = calculateDistanceToFinalSnap(layoutManager, view)

                if (snapDistance != null) {

                    if (snapDistance[0] != 0 || snapDistance[1] != 0)
                        recyclerView?.scrollBy(snapDistance[0], snapDistance[1])
                }
            }
        }
    }
}
