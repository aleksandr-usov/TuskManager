package com.example.tuskmanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tuskmanager.NewCategoryViewModel
import com.example.tuskmanager.data.repo.CategoryRepository
import javax.inject.Inject

class NewCategoryViewModelFactory @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewCategoryViewModel(categoryRepository) as T
    }
}