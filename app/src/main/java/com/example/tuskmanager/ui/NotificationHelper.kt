package com.example.tuskmanager.ui

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.tuskmanager.R
import com.example.tuskmanager.ui.NotificationHelper.Constants.CHANNEL_1_ID
import com.example.tuskmanager.ui.NotificationHelper.Constants.CHANNEL_1_NAME

class NotificationHelper(base: Context) : ContextWrapper(base) {

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannels() {
        val channel1 = NotificationChannel(CHANNEL_1_ID, CHANNEL_1_NAME, NotificationManager.IMPORTANCE_HIGH)
        channel1.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

        getManager().createNotificationChannel(channel1)
    }

    fun getManager(): NotificationManager {
       return getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun getGroupBuilder(): NotificationCompat.Builder {
      return NotificationCompat.Builder(applicationContext, CHANNEL_1_ID)
            .setGroupSummary(true)
            .setGroup("TASKS_ABOUT_TO_EXPIRE")
            .setSmallIcon(R.drawable.ic_attention)
    }

    fun getChannelNotification(title: String, message: String): NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, CHANNEL_1_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setGroup("TASKS_ABOUT_TO_EXPIRE")
            .setSmallIcon(R.drawable.ic_attention)
    }

    object Constants {
        const val CHANNEL_1_ID = "Channel1ID"
        const val CHANNEL_1_NAME = "Channel1Name"
    }
}