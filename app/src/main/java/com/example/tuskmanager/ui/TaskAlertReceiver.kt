package com.example.tuskmanager.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.util.*

class TaskAlertReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("task_title")
        val notificationHelper = NotificationHelper(context)
        val id = (Date().time / 1000L % Int.MAX_VALUE).toInt()
        val notificationGroupBuilder = notificationHelper.getGroupBuilder()
        notificationHelper.getManager().notify(1, notificationGroupBuilder.build())
        val notificationBuilder = notificationHelper.getChannelNotification(
            "Attention!",
            "$title is going to expire in an hour"
        )
        notificationHelper.getManager().notify(id, notificationBuilder.build())
    }
}