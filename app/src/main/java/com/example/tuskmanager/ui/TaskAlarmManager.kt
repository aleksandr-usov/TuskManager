package com.example.tuskmanager.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.tuskmanager.data.domain.model.TaskDomainModel
import java.util.*

class TaskAlarmManager(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun startAlarm(time: Long, task: TaskDomainModel) {
        val intent = Intent(context, TaskAlertReceiver::class.java).apply {
            putExtra("task_title", task.title)
        }
        val id = (Date().time / 1000L % Int.MAX_VALUE).toInt()
        val pendingIntent =
            PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_ONE_SHOT)

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }

    fun cancelAlarm(task: TaskDomainModel) {
        val intent = Intent(context, TaskAlertReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0)
        alarmManager.cancel(pendingIntent)
    }
}