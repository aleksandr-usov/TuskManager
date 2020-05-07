package com.example.tuskmanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tuskmanager.AllTasksViewModel
import com.example.tuskmanager.data.repo.TaskRepository
import javax.inject.Inject

class AllTasksViewModelFactory @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AllTasksViewModel(taskRepository) as T
    }
}