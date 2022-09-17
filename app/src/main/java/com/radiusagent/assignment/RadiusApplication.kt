package com.radiusagent.assignment

import android.app.Application
import android.content.Context
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RadiusApplication : Application() {
    companion object {
        private val TAG = RadiusApplication::class.java.name
        internal lateinit var appInstance: RadiusApplication

        private fun getInstance(): RadiusApplication {
            return appInstance
        }

        fun getApplicationContext(): Context {
            return getInstance()
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate()")
        appInstance = this
    }
}