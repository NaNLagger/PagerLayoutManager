package com.nanlagger.pagerlayoutmanager

import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View

/**
 * Created by lyashenko_se on 19.03.2018.
 */
class ResizeBehavior : CoordinatorLayout.Behavior<PagerRecyclerView>() {

    private var flagUpdate = false

    override fun layoutDependsOn(parent: CoordinatorLayout?, child: PagerRecyclerView, dependency: View?): Boolean {
        return dependency is Toolbar
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout?, child: PagerRecyclerView, dependency: View?): Boolean {

        val currentY = dependency?.y ?: 0f

        val layoutParams = child.layoutParams
        Log.v("changedLayoutParams", "old: ${layoutParams.height}, new ${currentY.toInt()}")
        layoutParams.height = currentY.toInt()
        child.requestLayout()
        return true
    }

}