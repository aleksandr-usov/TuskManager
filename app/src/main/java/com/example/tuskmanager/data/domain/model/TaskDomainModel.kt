package com.example.tuskmanager.data.domain.model

import java.io.Serializable

data class TaskDomainModel(
    val id: Long = 0L,
    var title: String,
    var category: String,
    var categoryIcon: String,
    var color: String,
    var description: String,
    val dateCreated: Long,
    var dateDue: String,
    var timeDue: String,
    var isComplete: Boolean
) : Serializable {
    fun isTaskValid(): Boolean {
        return title.isNotEmpty() && category.isNotEmpty() && description.isNotEmpty() && dateDue.isNotEmpty() &&
                timeDue.isNotEmpty()
    }

    companion object {
        val TASK_ADD_NEW: TaskDomainModel
            get() {
                return TaskDomainModel(
                    0,
                    "",
                    "Pick a category",
                    "ic_add",
                    "#179AB7",
                    "",
                    0,
                    "",
                    "",
                    false
                )
            }
    }
}