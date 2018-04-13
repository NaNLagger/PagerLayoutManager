package com.nanlagger.pagerlayoutmanager

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.nanlagger.pagerlayoutmanager.widget.PagerLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerCategory.layoutManager = PagerLayoutManager().also { it.changePositionListener = { position -> Log.v("position changed", "position $position")} }
        val testAdapter = TestAdapter()
        testAdapter.items = List(10, {i -> "$i page"})
        recyclerCategory.adapter = testAdapter
        testAdapter.notifyDataSetChanged()

        recyclerCategory.scrollToPosition(3)

        recyclerTest.layoutManager = LinearLayoutManager(this)
        val testAdapter1 = TestAdapter()
        testAdapter1.items = List(50, {i -> "$i page"})
        recyclerTest.adapter = testAdapter1
        testAdapter1.notifyDataSetChanged()

    }
}
