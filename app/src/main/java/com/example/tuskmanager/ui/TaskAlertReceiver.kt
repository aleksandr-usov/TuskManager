package com.example.tuskmanager.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.text.SimpleDateFormat
import java.util.*

class TaskAlertReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("task_title")
        val date = intent.getStringExtra("date_due")
        val time = intent.getStringExtra("time_due")
        val dateDue = date + time
        val sdf = SimpleDateFormat("dd.MM.yyyyHH:mm", Locale.ROOT)
        val myDateDue = sdf.parse(dateDue)
        val notificationHelper = NotificationHelper(context)
        val id = (Date().time / 1000L % Int.MAX_VALUE).toInt()
        val notificationGroupBuilder = notificationHelper.getGroupBuilder()
        notificationHelper.getManager().notify(1, notificationGroupBuilder.build())
        val notificationBuilder = if (myDateDue.time > System.currentTimeMillis())
            notificationHelper.getChannelNotification(
                "Attention!",
                "$title is expiring soon"
            ) else {
            notificationHelper.getChannelNotification(
                "Attention!",
                "$title expired"
            )
        }
        notificationHelper.getManager().notify(id, notificationBuilder.build())
    }
}