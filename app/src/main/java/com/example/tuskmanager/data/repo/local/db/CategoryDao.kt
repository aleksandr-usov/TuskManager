package com.example.tuskmanager.data.repo.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tuskmanager.data.repo.model.CategoryRepoModel
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(category: CategoryRepoModel): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategories(categories: List<CategoryRepoModel>): Completable

    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flowable<List<CategoryRepoModel>>

    @Query("SELECT * FROM categories WHERE id = :categoryId")
    fun getCategory(categoryId: Long): Single<CategoryRepoModel>

    @Query("DELETE FROM categories WHERE id = :categoryId")
    fun deleteCategory(categoryId: Long): Completable

    @Query("DELETE FROM categories WHERE id IN (:ids)")
    fun deleteCategories(ids: List<Long>): Completable

}