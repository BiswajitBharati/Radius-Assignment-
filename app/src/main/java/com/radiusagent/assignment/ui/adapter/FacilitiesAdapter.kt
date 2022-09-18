package com.radiusagent.assignment.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.radiusagent.assignment.data.model.FacilitiesModel
import com.radiusagent.assignment.data.model.OptionsModel
import com.radiusagent.assignment.databinding.ItemFacilitiesBinding
import com.radiusagent.assignment.ui.ItemClickListener

class FacilitiesAdapter(private val listener: ItemClickListener<OptionsModel>) : ListAdapter<FacilitiesModel, CustomViewHolder>(Companion) {
    private val viewPool = RecyclerView.RecycledViewPool()

    companion object : DiffUtil.ItemCallback<FacilitiesModel>() {
        override fun areItemsTheSame(oldItem: FacilitiesModel, newItem: FacilitiesModel): Boolean {
            return  oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: FacilitiesModel, newItem: FacilitiesModel): Boolean {
            return  oldItem.facilityId == newItem.facilityId
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFacilitiesBinding.inflate(inflater, parent, false)

        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val currentFacility = getItem(position)
        val itemBinding = holder.binding as ItemFacilitiesBinding

        itemBinding.optionsListener = listener
        itemBinding.facilities = currentFacility
        itemBinding.nestedRecyclerView.setRecycledViewPool(viewPool)
        itemBinding.executePendingBindings()
    }
}