package com.example.tuskmanager.data.repo.local

import com.example.tuskmanager.data.repo.local.db.TaskDatabase
import com.example.tuskmanager.data.repo.model.TaskRepoModel
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskLocalDataSource @Inject constructor(
    private val database: TaskDatabase
){

    fun getAllTasks(): Flowable<List<TaskRepoModel>> =
        database.taskDao().getAllTasks()

    fun getTask(taskId: Long) = database.taskDao().getTask(taskId)

    fun insertTask(task: TaskRepoModel) = database.taskDao().insertTask(task)

    fun deleteTask(taskId: Long) = database.taskDao().deleteTask(taskId)

    fun deleteTasks(ids: List<Long>) = database.taskDao().deleteTasks(ids)
}