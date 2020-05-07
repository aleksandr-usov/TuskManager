package com.example.tuskmanager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tuskmanager.data.domain.model.CategoryDomainModel
import com.example.tuskmanager.data.repo.CategoryRepository
import com.example.tuskmanager.data.repo.model.CategoryRepoModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class NewCategoryViewModel constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    private val _newCategory = MutableLiveData<CategoryDomainModel>()
    val newCategory: MutableLiveData<CategoryDomainModel> = _newCategory

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _categoryCreated = MutableLiveData<CategoryDomainModel>()
    val categoryCreated: LiveData<CategoryDomainModel> = _categoryCreated

    private val disposables = CompositeDisposable()

    init {
        _newCategory.value = CategoryDomainModel.CATEGORY_ADD_NEW
    }

    fun onIconClicked(newlySelected: String) {
        _newCategory.value?.icon = newlySelected
    }

    fun onColorClicked(newlySelected: String) {
        _newCategory.value = newCategory.value?.copy(color = newlySelected)
    }

    fun newCategoryTitleEntered(newlySelected: String) {
        _newCategory.value?.title = newlySelected
    }

    fun addCategory() {
        _error.value = null

        val currentCategory = _newCategory.value ?: return
        val title = currentCategory.title
        val icon = currentCategory.icon
        val color = currentCategory.color

        if (title.isEmpty() || icon.isEmpty() || color == "#179AB7") {
            _error.value = "error"
        } else {
            _error.value = null
            val newCategory = CategoryRepoModel(
                id = 0,
                title = _newCategory.value?.title ?: "",
                icon = _newCategory.value?.icon ?: "",
                color = _newCategory.value?.color ?: ""
            )
            disposables.add(
                categoryRepository.insertCategory(newCategory)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            _categoryCreated.value = it
                            _categoryCreated.value = null
                        },
                        {
                            it.printStackTrace()
                        })
            )
        }
    }
}
