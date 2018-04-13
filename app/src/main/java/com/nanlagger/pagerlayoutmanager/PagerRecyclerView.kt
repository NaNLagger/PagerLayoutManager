package com.nanlagger.pagerlayoutmanager

import android.content.Context
import android.database.DataSetObserver
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet

@CoordinatorLayout.DefaultBehavior(ResizeBehavior::class)
class PagerRecyclerView : RecyclerView {
//    private var pageScrollHandling: (Int) -> Unit = {}
//    private var currentViewPager: ViewPager? = null
//    private var currentPagerAdapter: MlmListPagerAdapter? = null
//    private var pageChangeListener: ViewPager.OnPageChangeListener? = null
//    private var adapterChangeListener: ViewPager.OnAdapterChangeListener? = null
//    private var pagerAdapterObserver: DataSetObserver? = null
//
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)
//
//    fun setupViewPager(viewPager: ViewPager) {
//        if (currentViewPager != null) {
//            // If we've already been setup with a ViewPager, remove us from it
//            if (pageChangeListener != null) {
//                currentViewPager?.removeOnPageChangeListener(pageChangeListener!!)
//            }
//            if (adapterChangeListener != null) {
//                currentViewPager?.removeOnAdapterChangeListener(adapterChangeListener!!)
//            }
//        }
//
//        currentViewPager = viewPager
//        val pagerAdapter = viewPager.adapter
//        if (pagerAdapter is MlmListPagerAdapter) {
//            setPagerAdapter(pagerAdapter)
//
//            pageChangeListener = object : ViewPager.SimpleOnPageChangeListener() {
//                override fun onPageSelected(position: Int) {
//                    smoothScrollToPosition(position)
//                }
//            }
//            viewPager.addOnPageChangeListener(pageChangeListener!!)
//
//            pageScrollHandling = { position ->
//                if (viewPager.currentItem != position)
//                    viewPager.currentItem = position
//            }
//
//            if(layoutManager is PagerLayoutManager) {
//                val pagerLayoutManager = layoutManager as PagerLayoutManager
//                pagerLayoutManager.changePositionListener = pageScrollHandling
//            }
//        }
//    }
//
//    private fun setPagerAdapter(adapter: MlmListPagerAdapter) {
//        if (currentPagerAdapter != null && pagerAdapterObserver != null) {
//            // If we already have a PagerAdapter, unregister our observer
//            currentPagerAdapter!!.unregisterDataSetObserver(pagerAdapterObserver!!)
//        }
//
//        currentPagerAdapter = adapter
//
//        // Register our observer on the new adapter
//        if (pagerAdapterObserver == null) {
//            pagerAdapterObserver = PagerAdapterObserver()
//        }
//        adapter.registerDataSetObserver(pagerAdapterObserver!!)
//
//        // Finally make sure we reflect the new adapter
//        populateFromPagerAdapter()
//    }
//
//    private inner class PagerAdapterObserver internal constructor() : DataSetObserver() {
//
//        override fun onChanged() {
//            populateFromPagerAdapter()
//        }
//
//        override fun onInvalidated() {
//            populateFromPagerAdapter()
//        }
//    }
//
//    private fun populateFromPagerAdapter() {
//        currentPagerAdapter?.let { pagerAdapter ->
//            val currentAdapter = adapter
//            if (currentAdapter is CategoryAdapter) {
//                currentAdapter.items = pagerAdapter.items
//                currentAdapter.notifyDataSetChanged()
//                val index = currentViewPager?.currentItem ?: 0
//                scrollToPosition(index)
//            }
//        }
//    }


}
