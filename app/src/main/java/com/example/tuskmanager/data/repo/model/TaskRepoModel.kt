package com.example.tuskmanager.data.repo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskRepoModel(
    @PrimaryKey(autoGenerate = true)

    val uniqueTaskId: Long = 0L,
    val title: String,
    val category: String,
    val categoryIcon: String,
    val color: String,
    val description: String,
    val dateAndTimeCreated: Long,
    val dateAndTimeDue: Long
)