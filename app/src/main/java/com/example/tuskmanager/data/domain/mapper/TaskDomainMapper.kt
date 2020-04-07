package com.example.tuskmanager.data.domain.mapper

import com.example.tuskmanager.data.domain.model.TaskDomainModel
import com.example.tuskmanager.data.repo.model.TaskRepoModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class TaskDomainMapper @Inject constructor() {

    fun toDomainModel(taskRepo: TaskRepoModel): TaskDomainModel {

        val pattern = "MMMM d yyyy, KK:mm a"
        val formatter = SimpleDateFormat(pattern)

        return TaskDomainModel(
            id = taskRepo.uniqueTaskId,
            title = taskRepo.title,
            category = taskRepo.category,
            description = taskRepo.description,
            dateCreated = formatter.format(Date(taskRepo.dateCreated)),
            dateDue = taskRepo.dateDue.toString(),
            timeDue = taskRepo.timeDue.toString()
        )
    }
}