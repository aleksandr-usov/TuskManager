package com.example.tuskmanager

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tuskmanager.data.domain.model.CategoryDomainModel
import com.example.tuskmanager.data.domain.model.TaskDomainModel
import com.example.tuskmanager.data.repo.CategoryRepository
import com.example.tuskmanager.data.repo.TaskRepository
import com.example.tuskmanager.data.repo.model.CategoryRepoModel
import com.example.tuskmanager.data.repo.model.TaskRepoModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class SharedViewModel constructor(
    private val taskRepository: TaskRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _screen = MutableLiveData<Screen>()
    val screen: LiveData<Screen> = _screen

    private val _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String> = _selectedDate

    private val _selectedTime = MutableLiveData<String>()
    val selectedTime: LiveData<String> = _selectedTime

    private val _toolBarText = MutableLiveData<ToolBarText>()
    val toolBarText: LiveData<ToolBarText> = _toolBarText

    private val _allCategories = MutableLiveData<List<CategoryDomainModel>>()
    val allCategories: LiveData<List<CategoryDomainModel>> = _allCategories

    private val _allTasks= MutableLiveData<List<TaskDomainModel>>()
    val allTasks: LiveData<List<TaskDomainModel>> = _allTasks

    private val _currentCategory = MutableLiveData<CategoryDomainModel>()
    val currentCategory: MutableLiveData<CategoryDomainModel> = _currentCategory

    private val _newTaskTitle = MutableLiveData<String>()
    val newTaskTitle: LiveData<String> = _newTaskTitle

    private val _newTaskDescription = MutableLiveData<String>()
    val newTaskDescription: LiveData<String> = _newTaskDescription

    private val _currentTask = MutableLiveData<TaskDomainModel>()
    val currentTask: LiveData<TaskDomainModel> = _currentTask

    private val disposables = CompositeDisposable()

    init {
        _screen.value = Screen.ALL_TASKS
        _toolBarText.value = ToolBarText.ALL_TASKS
        _currentTask.value = TASK_ADD_NEW

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

        disposables.add(
            taskRepository.getAllTasks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ newAllTasks ->
                    _allTasks.value = newAllTasks
                },
                    {
                        it.printStackTrace()
                    })
        )
    }

    fun addTask() {

        val myDate = _currentTask.value?.dateDue + _currentTask.value?.timeDue
        val sdf = SimpleDateFormat("dd.MM.yyyyHH:mm", Locale.ROOT)
        val date = sdf.parse(myDate)
        val millis: Long = date.time

       val newRepoTask = TaskRepoModel(
           uniqueTaskId = _currentTask.value?.id ?: 0,
           title = _currentTask.value?.title ?: "",
           category = _currentTask.value?.category ?: "",
           categoryIcon = _currentTask.value?.categoryIcon ?: "",
           color = _currentTask.value?.color ?: "",
           dateAndTimeCreated = System.currentTimeMillis(),
           dateAndTimeDue = millis,
           description = _currentTask.value?.description ?: ""
       )

        disposables.add(
            taskRepository.insertTask(newRepoTask)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {},
                    {
                        it.printStackTrace()
                    }
                )
        )
    }

    fun fabShow(): Boolean {
        return _currentTask.value?.dateDue != "" && _currentTask.value?.timeDue != "" &&
                _currentTask.value?.title != "" && _currentTask.value?.description != ""
                && _currentTask.value?.category != ""
    }

    fun onTaskClicked(newlySelected: TaskDomainModel) {

    }

    fun dateSelected(selectedDate: String) {
        _currentTask.value?.dateDue = selectedDate
    }

    fun timeSelected(selectedTime: String) {
        _currentTask.value?.timeDue = selectedTime
    }

    fun newTitleEntered(newTaskTitleEntered: String) {
        _currentTask.value?.title = newTaskTitleEntered
    }

    fun newDescriptionEntered(newDescriptionEntered: String) {
        _currentTask.value?.description = newDescriptionEntered
    }

    fun onCategoryClicked(newlySelected: CategoryDomainModel) {
        _currentCategory.value = newlySelected
        _currentTask.value?.category = newlySelected.title
        _currentTask.value?.categoryIcon = newlySelected.icon
        _currentTask.value?.color = newlySelected.color
        _screen.value = Screen.NEW_TASK
    }

    fun onAddCategoryClicked() {
        _screen.value = Screen.NEW_CATEGORY
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
        when (_screen.value) {
            Screen.ALL_TASKS -> _screen.value = Screen.NEW_TASK
            Screen.NEW_TASK -> {
                _screen.value = Screen.ALL_TASKS
                addTask()
            }
            Screen.NEW_CATEGORY -> _screen.value = Screen.NEW_TASK
        }
    }

    fun chooseCategoryClicked() {
        _screen.value = Screen.ALL_CATEGORIES
    }

    fun setToolBarText() {
        val newToolBarText = when (_screen.value ?: return) {
            Screen.ALL_TASKS -> ToolBarText.ALL_TASKS
            Screen.NEW_TASK -> ToolBarText.NEW_TASK
            Screen.ALL_CATEGORIES -> ToolBarText.CHOOSE_CATEGORY
            Screen.NEW_CATEGORY -> ToolBarText.NEW_CATEGORY
        }
        _toolBarText.value = newToolBarText
    }

    fun onBackPressed() {
        val newScreen = when (_screen.value ?: return) {
            Screen.NEW_TASK -> Screen.ALL_TASKS
            Screen.ALL_CATEGORIES -> Screen.NEW_TASK
            Screen.NEW_CATEGORY -> Screen.ALL_CATEGORIES
            else -> return
        }
        _screen.value = newScreen
        if (_screen.value == Screen.ALL_TASKS) {
            _currentCategory.value = CATEGORY_ADD_NEW
            _currentTask.value = TASK_ADD_NEW
        }
    }

    enum class Screen {
        ALL_TASKS, NEW_TASK, ALL_CATEGORIES, NEW_CATEGORY
    }

    enum class ToolBarText(val displayableName: String) {
        ALL_TASKS("All Tasks"),
        NEW_TASK("New Task"),
        CHOOSE_CATEGORY("Choose a category"),
        NEW_CATEGORY("New Category")
    }

    companion object {
        val CATEGORY_ADD_NEW = CategoryDomainModel(
            0,
            "Pick a category",
            "ic_add",
            "#179AB7"
        )

        val TASK_ADD_NEW : TaskDomainModel
            get() {
               return TaskDomainModel(
                    0,
                    "",
                    "",
                    "",
                    "#ffffff",
                    "",
                    "",
                    "",
                    ""
                )
            }
    }
}
