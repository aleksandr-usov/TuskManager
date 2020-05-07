package com.example.tuskmanager.data.repo.local

import com.example.tuskmanager.data.repo.local.db.TaskDatabase
import com.example.tuskmanager.data.repo.model.CategoryRepoModel
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryLocalDataSource @Inject constructor(
    private val database: TaskDatabase
) {
    fun getAllCategories(): Flowable<List<CategoryRepoModel>> =
        database.categoryDao().getAllCategories()

    fun getCategory(categoryId: Long) = database.categoryDao().getCategory(categoryId)

    fun insertCategory(category: CategoryRepoModel) =
        database.categoryDao().insertCategory(category)

    fun deleteCategory(categoryId: Long) = database.categoryDao().deleteCategory(categoryId)

    fun deleteCategories(ids: List<Long>) = database.categoryDao().deleteCategories(ids)
}