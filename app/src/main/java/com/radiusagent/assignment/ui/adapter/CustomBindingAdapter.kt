package com.radiusagent.assignment.ui.adapter

import android.annotation.SuppressLint
import android.content.res.Resources
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.radiusagent.assignment.R
import com.radiusagent.assignment.data.model.OptionsModel
import com.radiusagent.assignment.ui.ItemClickListener

@BindingAdapter(value = ["setOptions", "setOptionsListener"])
fun RecyclerView.setRowOption(options: List<OptionsModel>?, listener: ItemClickListener<OptionsModel>?) {
    if (options != null && listener != null) {
        val optionsAdapter = OptionsAdapter(listener)
        optionsAdapter.submitList(options)

        adapter = optionsAdapter
    }
}

@BindingAdapter(value = ["optionIcon"])
fun ImageView.setOptionIcon(icon: String?) {
    if (icon != null) {
        val resources: Resources = context.resources
        val resourceId: Int = resources.getIdentifier(
            icon.replace("-", "_"), "drawable",
            context.packageName
        )
        this.setImageResource(resourceId)
    } else {
        this.setImageResource(R.mipmap.ic_launcher_round)
    }
}