package com.example.tuskmanager.data.repo.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tuskmanager.data.repo.model.TaskRepoModel
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: TaskRepoModel): Single<Long>

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flowable<List<TaskRepoModel>>

    @Query("SELECT * FROM tasks WHERE uniqueTaskId = :taskId")
    fun getTask(taskId: Long): Single<TaskRepoModel>

    @Query("DELETE FROM tasks WHERE uniqueTaskId = :taskId")
    fun deleteTask(taskId: Long): Completable

    @Query("DELETE FROM tasks WHERE uniqueTaskId IN (:ids)")
    fun deleteTasks(ids: List<Long>): Completable

}