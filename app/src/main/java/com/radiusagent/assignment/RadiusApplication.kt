package com.radiusagent.assignment

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit

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

        setUpWorkManager()
    }

    private fun setUpWorkManager() {
        Log.d(TAG, "setUpWorkManager()")
        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<BackgroundWorker>(15, TimeUnit.MINUTES)
                /*.setInitialDelay(15, TimeUnit.MINUTES)*/.build()

        val workManager = WorkManager.getInstance(this)
        workManager.enqueue(periodicWorkRequest)
        workManager.getWorkInfoByIdLiveData(periodicWorkRequest.id).observeForever { workInfo ->
            if (null != workInfo) {
                Log.d(TAG, "setUpWorkManager() == Status changed to ${workInfo.state.isFinished}")
            }
        }
    }
}