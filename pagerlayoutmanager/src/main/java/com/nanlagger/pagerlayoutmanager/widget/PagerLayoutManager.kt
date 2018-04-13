package com.nanlagger.pagerlayoutmanager.widget

import android.graphics.PointF
import android.graphics.Rect
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup


open class PagerLayoutManager : RecyclerView.LayoutManager() {

    private var mPosition: Int = 0
        set(value) {
            field = when {
                value < 0 -> 0
                value >= itemCount -> itemCount - 1
                else -> value
            }
            changePositionListener(field)
        }
    private var scrollOffset: Int = 0
    private val offsetPageLimit: Int = 2
    private val viewCache = SparseArray<View>()
    private var recyclerView: RecyclerView? = null

    var changePositionListener: (Int) -> Unit = {}

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun canScrollHorizontally(): Boolean {
        return true
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        if (recycler == null || state == null)
            return 0
        val delta = scrollHorizontallyInternal(dx)
        offsetChildrenHorizontal(-delta)
        scrollOffset += delta
        if (Math.abs(scrollOffset) >= width) {
            mPosition += if (scrollOffset < 0) -1 else 1
            scrollOffset += if (scrollOffset < 0) width else -width
            layout(recycler)
        }
        return delta
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        super.onLayoutChildren(recycler, state)
        if (recycler == null || state == null)
            return
        layout(recycler)
    }

    override fun scrollToPosition(position: Int) {
        mPosition = position
        scrollOffset = 0

        requestLayout()
    }

    override fun smoothScrollToPosition(recyclerView: RecyclerView?, state: RecyclerView.State?, position: Int) {
        if (position >= itemCount) {
            return
        }

        val scroller = object : LinearSmoothScroller(recyclerView!!.context) {
            override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
                return this@PagerLayoutManager.computeScrollVectorForPosition(targetPosition)
            }

            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
        scroller.targetPosition = position
        startSmoothScroll(scroller)
    }

    override fun onAttachedToWindow(view: RecyclerView?) {
        super.onAttachedToWindow(view)
        recyclerView = view
        view?.onFlingListener = object : RecyclerView.OnFlingListener() {
            override fun onFling(velocityX: Int, velocityY: Int): Boolean {
                if (Math.abs(velocityX) < Math.abs(velocityY)) {
                    return false
                }

                val anchorView = getAttachedViewForPosition(mPosition) ?: return false
                val leftEdge = anchorView.left
                val rightEdge = getDecoratedRight(anchorView)

                Log.v("fling", "velocity: $velocityX")
                if (Math.abs(velocityX) < 1000) {
                    if (leftEdge != 0) {
                        recyclerView?.smoothScrollBy(leftEdge, 0)
                    }
                    return true
                } else {
                    if (velocityX > 0)
                        recyclerView?.smoothScrollBy(leftEdge + width, 0)
                    if (velocityX < 0)
                        recyclerView?.smoothScrollBy(leftEdge - width, 0)
                    return true

                }
            }
        }
    }

    override fun onDetachedFromWindow(view: RecyclerView?, recycler: RecyclerView.Recycler?) {
        super.onDetachedFromWindow(view, recycler)
        recyclerView?.onFlingListener = null
        recyclerView = null
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)

        if (state == RecyclerView.SCROLL_STATE_IDLE) {

            val anchorView = getAttachedViewForPosition(mPosition) ?: return
            val leftEdge = getDecoratedLeft(anchorView)
            val rightEdge = getDecoratedRight(anchorView)

            if (leftEdge == 0)
                return

            when {
                leftEdge < -width / 2 -> {
                    recyclerView?.smoothScrollBy(rightEdge, 0)
                }
                leftEdge > width / 2 -> {
                    recyclerView?.smoothScrollBy(-leftEdge, 0)
                }
                else -> recyclerView?.smoothScrollBy(leftEdge, 0)
            }
        }
    }

    open fun layout(recycler: RecyclerView.Recycler) {
        viewCache.clear()

        //Помещаем вьюшки в кэш и...
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            val pos = getPosition(view)
            viewCache.put(pos, view)
        }

        //... и удалям из лэйаута
        for (i in 0 until viewCache.size()) {
            detachView(viewCache.valueAt(i))
        }

        fill(recycler)

        //отправляем в корзину всё, что не потребовалось в этом цикле лэйаута
        //эти вьюшки или ушли за экран или не понадобились, потому что соответствующие элементы
        //удалились из адаптера
        for (i in 0 until viewCache.size()) {
            recycler.recycleView(viewCache.valueAt(i))
        }
    }

    private fun fill(recycler: RecyclerView.Recycler) {
        val startPos = (mPosition - offsetPageLimit).let { if (it < 0) 0 else it }
        val endPos = (mPosition + offsetPageLimit).let { if (it >= itemCount) itemCount - 1 else it }

        val widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)

        for (pos in startPos..endPos) {
            var view = viewCache.get(pos)
            val viewLeft = (pos - mPosition) * width - scrollOffset
            if (view == null) {
                view = recycler.getViewForPosition(pos)
                addView(view!!)
                measureChildWithDecorationsAndMargin(view, widthSpec, heightSpec)
                layoutDecorated(view, viewLeft, 0, viewLeft + width, height)
            } else {
                measureChildWithDecorationsAndMargin(view, widthSpec, heightSpec)
                layoutDecorated(view, viewLeft, 0, viewLeft + width, height)
                attachView(view)
                recycler.bindViewToPosition(view, pos)
                viewCache.remove(pos)
            }
        }
    }

    private fun measureChildWithDecorationsAndMargin(child: View, widthSpec: Int, heightSpec: Int) {
        val decorRect = Rect()
        calculateItemDecorationsForChild(child, decorRect)
        val lp = child.layoutParams as RecyclerView.LayoutParams
        val nWidthSpec = updateSpecWithExtra(widthSpec, lp.leftMargin + decorRect.left,
                lp.rightMargin + decorRect.right)
        val nHeightSpec = updateSpecWithExtra(heightSpec, lp.topMargin + decorRect.top,
                lp.bottomMargin + decorRect.bottom)
        child.measure(nWidthSpec, nHeightSpec)
    }

    private fun updateSpecWithExtra(spec: Int, startInset: Int, endInset: Int): Int {
        if (startInset == 0 && endInset == 0) {
            return spec
        }
        val mode = View.MeasureSpec.getMode(spec)
        return if (mode == View.MeasureSpec.AT_MOST || mode == View.MeasureSpec.EXACTLY) {
            View.MeasureSpec.makeMeasureSpec(
                    View.MeasureSpec.getSize(spec) - startInset - endInset, mode)
        } else spec
    }

    private fun scrollHorizontallyInternal(dx: Int): Int {
        if (childCount == 0) return 0

        var delta = 0

        val anchorView = getAttachedViewForPosition(mPosition) ?: return 0
        //если контент уезжает вправо
        if (dx < 0) {
            delta = if (mPosition > 0) { //если левая вюшка не самая первая в адаптере
                dx
            } else { //если левая вьюшка самая первая в адаптере и левее вьюшек больше быть не может
                val viewLeft = getDecoratedLeft(anchorView)
                Math.max(viewLeft, dx)
            }
        } else if (dx > 0) { //если контент уезжает влево
            delta = if (mPosition < itemCount - 1) { //если правая вюшка не самая последняя в адаптере
                dx
            } else { //если правая вьюшка самая последняя в адаптере и правее вьюшек больше быть не может
                val viewRight = getDecoratedRight(anchorView)
                val parentRight = width
                Math.min(viewRight - parentRight, dx)
            }
        }
        return delta
    }


    private fun getAttachedViewForPosition(position: Int): View? {
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            if (getPosition(view) == position) {
                return view
            }
        }
        return null
    }

    private fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
        if (childCount == 0) {
            return null
        }
        val firstChildPos = getPosition(getChildAt(0))
        val direction = if (targetPosition < firstChildPos) -1 else 1
        return PointF(direction.toFloat(), 0f)
    }
}