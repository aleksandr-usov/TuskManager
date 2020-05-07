package com.example.tuskmanager.data.repo

import com.example.tuskmanager.data.domain.mapper.TaskDomainMapper
import com.example.tuskmanager.data.domain.model.TaskDomainModel
import com.example.tuskmanager.data.repo.local.TaskLocalDataSource
import com.example.tuskmanager.data.repo.model.TaskRepoModel
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository
@Inject constructor(
    private val localDataSource: TaskLocalDataSource,
    private val domainMapper: TaskDomainMapper
) {

    fun getAllTasks(): Flowable<List<TaskDomainModel>> {
        return localDataSource.getAllTasks()
            .map { listFromLocal ->
                listFromLocal.map { taskRepoModel -> domainMapper.toDomainModel(taskRepoModel) }
            }
    }

    fun getTask(taskId: Long): Single<TaskDomainModel> {
        return localDataSource.getTask(taskId).map { taskRepoModel ->
            domainMapper.toDomainModel(taskRepoModel)
        }
    }

    fun delayTaskByDay(id: Long): Completable {
        return localDataSource.delayTaskByDay(id)
    }

    fun insertTask(task: TaskRepoModel): Single<TaskDomainModel> {
        return localDataSource.insertTask(task)
            .map { domainMapper.toDomainModel(task) }
    }

    fun markTaskCompleted(taskId: Long): Completable {
        return localDataSource.markTaskAsCompleted(taskId)
    }

    fun deleteTask(taskId: Long): Completable {
        return localDataSource.deleteTask(taskId)
    }

    fun deleteTasks(ids: List<Long>): Completable {
        return localDataSource.deleteTasks(ids)
    }
}