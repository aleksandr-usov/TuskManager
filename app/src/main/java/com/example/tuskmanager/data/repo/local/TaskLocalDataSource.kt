package com.example.tuskmanager.data.repo.local

import com.example.tuskmanager.data.repo.local.db.TaskDatabase
import com.example.tuskmanager.data.repo.model.TaskRepoModel
import com.example.tuskmanager.plus
import io.reactivex.Completable
import io.reactivex.Flowable
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskLocalDataSource @Inject constructor(
    private val database: TaskDatabase
) {
    fun getAllTasks(): Flowable<List<TaskRepoModel>> =
        database.taskDao().getAllTasks()

    fun getTask(taskId: Long) = database.taskDao().getTask(taskId)

    fun insertTask(task: TaskRepoModel) = database.taskDao().insertTask(task)

    fun deleteTask(taskId: Long) = database.taskDao().deleteTask(taskId)

    fun deleteTasks(ids: List<Long>) = database.taskDao().deleteTasks(ids)

    fun delayTaskByDay(id: Long): Completable {
        return getTask(id).map {
            val currentDate = Date(it.dateAndTimeDue)
            val newDate = currentDate.plus(1, TimeUnit.DAYS)
            return@map it.copy(dateAndTimeDue = newDate.time)
        }
            .flatMap { insertTask(it) }
            .ignoreElement()
    }

    fun markTaskAsCompleted(taskId: Long): Completable {
        return database.taskDao().markTaskAsCompleted(taskId)
    }
}