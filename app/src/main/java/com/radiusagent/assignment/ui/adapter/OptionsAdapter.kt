package com.radiusagent.assignment.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.radiusagent.assignment.data.model.ExclusionsModel
import com.radiusagent.assignment.data.model.OptionsModel
import com.radiusagent.assignment.databinding.ItemOptionsBinding
import com.radiusagent.assignment.ui.ItemClickListener

class OptionsAdapter(private val facilityId: String, private val listener: ItemClickListener<String, OptionsModel>) : ListAdapter<OptionsModel, CustomViewHolder>(Companion) {

    private var exclusions: ArrayList<ExclusionsModel> = arrayListOf()
    private var selectedId: String? = null

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
        itemBinding.facilityId = facilityId
        itemBinding.isSelected = selectedId == currentOption.id
        Log.d("OptionsAdapter", "onBindViewHolder() == facilityId: $facilityId selectedId: $selectedId")
        itemBinding.isUnderExclusion = if (exclusions.isEmpty()) false
        else null != exclusions.find { it.optionsId == currentOption.id }

        itemBinding.executePendingBindings()
    }

    fun updateSelectedOption(selectedId: String?) {
        Log.d("OptionsAdapter", "updateSelectedOption() == selectedId: $selectedId")
        this.selectedId = selectedId
    }

    fun updateExclusions(exclusions: List<ExclusionsModel>?) {
        Log.d("OptionsAdapter", "updateExclusions() == exclusions: ${exclusions?.size}")
        this.exclusions.clear()
        this.exclusions.addAll(exclusions ?: arrayListOf())
    }
}