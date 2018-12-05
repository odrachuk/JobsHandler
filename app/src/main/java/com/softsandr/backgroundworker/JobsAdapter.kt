package com.softsandr.backgroundworker

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

data class JobItem(val id: String, val duration: String)

class JobsAdapter : RecyclerView.Adapter<JobsAdapter.JobItemViewHolder>() {

    private val items = mutableListOf<JobItem>()

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
        return JobItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JobItemViewHolder, position: Int) {
        val jobItem = getItemForPosition(position)
        holder.bind(jobItem)
    }

    override fun getItemViewType(position: Int): Int = getLayoutIdForPosition(position)

    private fun getItemForPosition(position: Int): JobItem = items[position]

    @LayoutRes
    private fun getLayoutIdForPosition(position: Int): Int =
        if (position % 2 == 0) {
            R.layout.recycler_item_white
        } else {
            R.layout.recycler_item_gray
        }

    fun addItem(item: JobItem) {
        this.items.add(item)
        notifyDataSetChanged()
    }

    inner class JobItemViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(jobItem: JobItem) {
            binding.setVariable(BR.data, jobItem)
            binding.executePendingBindings()
        }
    }
}