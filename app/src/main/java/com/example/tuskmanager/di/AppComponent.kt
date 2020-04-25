package com.example.tuskmanager.di

import com.example.tuskmanager.*
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        AppModule::class
    ]
)

@Singleton
interface AppComponent {
    fun inject(AllTasksFragment: AllTasksFragment)
    fun inject(NewTaskFragment: NewTaskFragment)
    fun inject(NewCategoryFragment: NewCategoryFragment)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: TaskApplication): Builder

        fun build(): AppComponent
    }
}