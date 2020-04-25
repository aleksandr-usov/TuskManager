package com.example.tuskmanager.data.repo.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tuskmanager.data.repo.model.CategoryRepoModel
import com.example.tuskmanager.data.repo.model.TaskRepoModel
import io.reactivex.Completable

@Database(
    entities = [TaskRepoModel::class, CategoryRepoModel::class],
    version = 1,
    exportSchema = false
)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun categoryDao(): CategoryDao
}