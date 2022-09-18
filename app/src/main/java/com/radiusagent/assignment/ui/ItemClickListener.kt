package com.radiusagent.assignment.ui

interface ItemClickListener<K, I> {
    fun onClickListener(key: K, item: I)
}