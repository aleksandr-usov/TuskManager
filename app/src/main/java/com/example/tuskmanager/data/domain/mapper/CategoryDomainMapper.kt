package com.example.tuskmanager.data.domain.mapper

import com.example.tuskmanager.data.domain.model.CategoryDomainModel
import com.example.tuskmanager.data.domain.model.TaskDomainModel
import com.example.tuskmanager.data.repo.model.CategoryRepoModel
import com.example.tuskmanager.data.repo.model.TaskRepoModel
import javax.inject.Inject

class CategoryDomainMapper @Inject constructor() {

    fun toDomainModel(categoryRepo: CategoryRepoModel): CategoryDomainModel {
        return CategoryDomainModel(
            id = categoryRepo.id,
            title = categoryRepo.title,
            icon = categoryRepo.icon,
            color = categoryRepo.color
        )
    }
}