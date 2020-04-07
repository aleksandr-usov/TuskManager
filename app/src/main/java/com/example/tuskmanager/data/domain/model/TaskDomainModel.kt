package com.example.tuskmanager.data.domain.model

data class TaskDomainModel(
    val id: Long = 0L,
    val title: String,
    val category: String,
    val description: String,
    val dateCreated: String,
    val dateDue: String,
    val timeDue: String
)