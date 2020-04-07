package com.example.tuskmanager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tuskmanager.data.domain.model.CategoryDomainModel
import com.example.tuskmanager.data.repo.CategoryRepository
import com.example.tuskmanager.data.repo.TaskRepository
import com.example.tuskmanager.data.repo.model.CategoryRepoModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SharedViewModel constructor(
    private val taskRepository: TaskRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    val specialCategoryAdd: CategoryDomainModel = CategoryDomainModel(
        4,
        "Add",
        "R.drawable.ic_add",
        "#000000"
    )

    private val _screen = MutableLiveData<Screen>()
    val screen: LiveData<Screen> = _screen

    private val _toolBarText = MutableLiveData<ToolBarText>()
    val toolBarText: LiveData<ToolBarText> = _toolBarText

    private val _allCategories = MutableLiveData<List<CategoryDomainModel>>()
    val allCategories: LiveData<List<CategoryDomainModel>> = _allCategories

    private val disposables = CompositeDisposable()

    init {
        _screen.value = Screen.ALL_TASKS
        _toolBarText.value = ToolBarText.ALL_TASKS

        disposables.add(
            categoryRepository.getAllCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ newAllCategories ->
                    _allCategories.value = newAllCategories
                },
                    {
                        it.printStackTrace()
                    })
        )
    }

    fun onCategoryClicked(newlySelected: CategoryDomainModel) {

    }

    fun addCategory() {

        val newCategory = CategoryRepoModel(
            id = 0L,
            title = "Werk",
            icon = "drawable.ic_suit_case",
            color = "#FFFFFF"
        )

        disposables.add(
            categoryRepository.insertCategory(newCategory)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {

                    },
                    {
                        it.printStackTrace()
                    })
        )
    }

    fun fabClicked() {
     //   _screen.value = Screen.NEW_TASK
    }

    fun chooseCategoryClicked() {
        _screen.value = Screen.ALL_CATEGORIES
    }

    fun setToolBarText() {
        val newToolBarText = when (_screen.value ?: return) {
            Screen.ALL_TASKS -> ToolBarText.ALL_TASKS
            Screen.NEW_TASK -> ToolBarText.NEW_TASK
            Screen.ALL_CATEGORIES -> ToolBarText.CHOOSE_CATEGORY
        }
        _toolBarText.value = newToolBarText
    }

    fun onBackPressed() {
        val newScreen = when (_screen.value ?: return) {
            Screen.NEW_TASK -> Screen.ALL_TASKS
            Screen.ALL_CATEGORIES -> Screen.NEW_TASK
            else -> return
        }
        _screen.value = newScreen
    }

    enum class Screen {
        ALL_TASKS, NEW_TASK, ALL_CATEGORIES
    }

    enum class ToolBarText(val displayableName: String) {
        ALL_TASKS("All Tasks"),
        NEW_TASK("New Task"),
        CHOOSE_CATEGORY("Choose a category")
    }
}
