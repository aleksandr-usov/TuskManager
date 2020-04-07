package com.example.tuskmanager.data.repo

import com.example.tuskmanager.data.domain.mapper.CategoryDomainMapper
import com.example.tuskmanager.data.domain.model.CategoryDomainModel
import com.example.tuskmanager.data.domain.model.TaskDomainModel
import com.example.tuskmanager.data.repo.local.CategoryLocalDataSource
import com.example.tuskmanager.data.repo.model.CategoryRepoModel
import com.example.tuskmanager.data.repo.model.TaskRepoModel
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository
@Inject constructor(
    private val localDataSource: CategoryLocalDataSource,
    private val domainMapper: CategoryDomainMapper
) {

    fun getAllCategories(): Flowable<List<CategoryDomainModel>> {
        return localDataSource.getAllCategories()
            .map { listFromLocal ->
                listFromLocal.map { categoryRepoModel -> domainMapper.toDomainModel(categoryRepoModel) }
            }
    }

    fun getCategory(categoryId: Long): Single<CategoryDomainModel> {
        return localDataSource.getCategory(categoryId).map { categoryRepoModel ->
            domainMapper.toDomainModel(categoryRepoModel)
        }
    }

    fun insertCategory(category: CategoryRepoModel): Single<Long> {
        return localDataSource.insertCategory(category)
    }

    fun deleteCategory(categoryId: Long): Completable {
        return localDataSource.deleteCategory(categoryId)
    }

    fun deleteTCategories(ids: List<Long>): Completable {
        return localDataSource.deleteCategories(ids)
    }
}