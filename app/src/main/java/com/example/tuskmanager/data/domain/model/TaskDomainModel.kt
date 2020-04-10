package com.example.tuskmanager.data.domain.model

data class TaskDomainModel(
    val id: Long = 0L,
    var title: String,
    var category: String,
    var categoryIcon: String,
    var color: String,
    var description: String,
    val dateCreated: String,
    var dateDue: String,
    var timeDue: String
) {
    fun isTaskValid(): Boolean {
        return title.isNotEmpty() && category.isNotEmpty() && description.isNotEmpty() && dateDue.isNotEmpty() &&
                timeDue.isNotEmpty()
    }
}