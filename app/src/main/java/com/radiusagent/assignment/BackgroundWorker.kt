package com.radiusagent.assignment

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.radiusagent.assignment.ui.MainActivity

class BackgroundWorker(private val context : Context, private val params : WorkerParameters) : Worker(context, params) {
    companion object {
        private val TAG = BackgroundWorker::class.java.name
    }
    override fun doWork(): Result {
        Log.d(TAG,"doWork() == Fetch facilities in background");
        sendNotification(title = "Radius Agent", message = "Fetch facilities...");
        return Result.success();
    }

    private fun sendNotification(title: String, message: String) {
        Log.d(TAG,"sendNotification() == title: $title message: $message");
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        val notification: NotificationCompat.Builder = NotificationCompat.Builder(
            applicationContext,
            "default"
        )
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(MainActivity.getPendingIntent(context = context, requestCode = 1))
            .setSmallIcon(R.mipmap.ic_launcher)
        notificationManager.notify(1, notification.build())
    }
}