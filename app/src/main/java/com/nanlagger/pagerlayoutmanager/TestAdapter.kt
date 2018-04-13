package com.nanlagger.pagerlayoutmanager

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nanlagger.pagerlayoutmanager.R

class TestAdapter : RecyclerView.Adapter<TestAdapter.TestHolder>() {

    var items: List<String> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestHolder {
        return TestHolder(LayoutInflater.from(parent.context).inflate(R.layout.test_item_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: TestHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class TestHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val textTest: TextView = itemView.findViewById(R.id.textPage)

        fun bind(item: String) {
            textTest.text = item
        }
    }
}