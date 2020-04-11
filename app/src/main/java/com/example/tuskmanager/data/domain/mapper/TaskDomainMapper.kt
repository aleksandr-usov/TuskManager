package com.example.tuskmanager.data.domain.mapper

import com.example.tuskmanager.data.domain.model.TaskDomainModel
import com.example.tuskmanager.data.repo.model.TaskRepoModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class TaskDomainMapper @Inject constructor() {

    fun toDomainModel(taskRepo: TaskRepoModel): TaskDomainModel {

        val patternDate = "dd.MM.yyyy"
        val patternTime = "HH:mm"
        val formatterDate = SimpleDateFormat(patternDate, Locale.ROOT)
        val formatterTime = SimpleDateFormat(patternTime, Locale.ROOT)

        return TaskDomainModel(
            id = taskRepo.uniqueTaskId,
            title = taskRepo.title,
            category = taskRepo.category,
            categoryIcon = taskRepo.categoryIcon,
            color = taskRepo.color,
            description = taskRepo.description,
            dateCreated = taskRepo.dateAndTimeCreated.toString(),
            dateDue = formatterDate.format(Date(taskRepo.dateAndTimeDue)),
            timeDue = formatterTime.format(Date(taskRepo.dateAndTimeDue))
        )
    }
}