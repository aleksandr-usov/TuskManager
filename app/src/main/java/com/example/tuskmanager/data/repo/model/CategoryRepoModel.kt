package com.example.tuskmanager.data.repo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryRepoModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val icon: String,
    val color: String
)

