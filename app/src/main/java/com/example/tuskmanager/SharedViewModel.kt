package com.example.tuskmanager

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

    private val _allCategories = MutableLiveData<List<CategoryDomainModel>>()
    val allCategories: LiveData<List<CategoryDomainModel>> = _allCategories

    private val _allTasks = MutableLiveData<List<TaskDomainModel>>()
    val allTasks: LiveData<List<TaskDomainModel>> = _allTasks

    private val _currentCategory = MutableLiveData<CategoryDomainModel>()
    val currentCategory: MutableLiveData<CategoryDomainModel> = _currentCategory

    private val _currentTask = MutableLiveData<TaskDomainModel>()
    val currentTask: LiveData<TaskDomainModel> = _currentTask

    private val disposables = CompositeDisposable()

    init {
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
                .subscribe(
                    { newAllTasks -> _allTasks.value = newAllTasks },
                    { it.printStackTrace() }
                )
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

    companion object {
        val CATEGORY_ADD_NEW = CategoryDomainModel(
            0,
            "Pick a category",
            "ic_add",
            "#179AB7"
        )

        val TASK_ADD_NEW: TaskDomainModel
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
