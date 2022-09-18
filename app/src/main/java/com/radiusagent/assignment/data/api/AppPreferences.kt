package com.radiusagent.assignment.data.api

import android.content.SharedPreferences
import android.util.Log

class AppPreferences(private val sharedPreferences: SharedPreferences) {
    companion object {
        private val TAG = AppPreferences::class.java.name
        private const val LAST_REFRESH_TIME = "lastRefreshTime"
    }

    var lastRefreshTime: Long
        get() = sharedPreferences.getLong(LAST_REFRESH_TIME, 0L)
        set(value) {
            Log.d(TAG, "lastRefreshTime: $value")
            with(sharedPreferences.edit()) {
                putLong(LAST_REFRESH_TIME, value)
                apply()
            }
        }
}