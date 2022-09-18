package com.radiusagent.assignment.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.radiusagent.assignment.data.model.OptionsModel
import com.radiusagent.assignment.databinding.ItemOptionsBinding
import com.radiusagent.assignment.ui.ItemClickListener

class OptionsAdapter(val listener: ItemClickListener<OptionsModel>) : ListAdapter<OptionsModel, CustomViewHolder>(Companion) {
    companion object : DiffUtil.ItemCallback<OptionsModel>() {
        override fun areItemsTheSame(oldItem: OptionsModel, newItem: OptionsModel): Boolean {
            return  oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: OptionsModel, newItem: OptionsModel): Boolean {
            return  oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemOptionsBinding.inflate(inflater, parent, false)

        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val currentOption = getItem(position)
        val itemBinding = holder.binding as ItemOptionsBinding

        itemBinding.optionsListener = listener
        itemBinding.option = currentOption
        itemBinding.executePendingBindings()
    }
}