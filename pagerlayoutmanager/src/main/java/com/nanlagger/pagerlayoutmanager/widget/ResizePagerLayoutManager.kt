package com.nanlagger.pagerlayoutmanager.widget

import android.support.v7.widget.RecyclerView

class ResizePagerLayoutManager(private val maxHeight: Int, private val minHeight: Int) : PagerLayoutManager() {

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        val delta = super.scrollHorizontallyBy(dx, recycler, state)
        updateViewScale()
        return delta
    }

    override fun layout(recycler: RecyclerView.Recycler) {
        super.layout(recycler)
        updateViewScale()
    }

    private fun updateViewScale() {
        val diffHeight = maxHeight - minHeight
        val currentDiff = maxHeight - height
        val diffMultiplier = if (currentDiff.toFloat() / diffHeight.toFloat() > 1) 1f else currentDiff.toFloat() / diffHeight.toFloat()

        val childCount = childCount
        val width = width
        val thresholdPx = (width * 0.5).toInt()
        for (i in 0 until childCount) {
            var scale = 1f
            var translation = 0f
            val view = getChildAt(i)
            val viewLeft = getDecoratedLeft(view)
            val viewRight = getDecoratedRight(view)
            if (viewLeft >= thresholdPx) {
                val delta = viewLeft - thresholdPx
                scale = (width - delta) / width.toFloat()
                scale = Math.max(scale, 0f)
                translation = -delta.toFloat() * (1 - diffMultiplier)
            }
            if (viewRight <= thresholdPx) {
                val delta = thresholdPx - viewRight
                scale = (width - delta) / width.toFloat()
                scale = Math.max(scale, 0f)
                translation = delta.toFloat() * (1 - diffMultiplier)
            }
            view.translationX = translation
            view.scaleX = scale
            view.scaleY = scale
        }
    }
}