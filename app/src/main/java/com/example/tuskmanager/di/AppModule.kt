package com.example.tuskmanager.di

import android.app.Application
import androidx.room.Room
import com.example.tuskmanager.TaskApplication
import com.example.tuskmanager.data.repo.local.db.TaskDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class AppModule {

    @Binds
    abstract fun provideDaggerApplication(application: TaskApplication): Application

    @Module
    companion object {
        @Provides
        @Singleton
        @JvmStatic
        fun provideDatabase(context: Application): TaskDatabase {
            val builder = Room.databaseBuilder(
                context,
                TaskDatabase::class.java,
                "task_database"
            )
                .createFromAsset("defaultCategories.db")
            return builder.build()
        }
    }
}