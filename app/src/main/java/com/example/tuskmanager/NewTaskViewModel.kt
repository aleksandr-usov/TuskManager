package com.example.tuskmanager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tuskmanager.data.domain.model.CategoryDomainModel
import com.example.tuskmanager.data.domain.model.TaskDomainModel
import com.example.tuskmanager.data.repo.CategoryRepository
import com.example.tuskmanager.data.repo.TaskRepository
import com.example.tuskmanager.data.repo.model.TaskRepoModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class NewTaskViewModel constructor(
    private val taskRepository: TaskRepository,
    categoryRepository: CategoryRepository
) : ViewModel() {
    private val _currentTask = MutableLiveData<TaskDomainModel>()
    val currentTask: LiveData<TaskDomainModel> = _currentTask

    private val _currentCategory = MutableLiveData<CategoryDomainModel>()
    val currentCategory: MutableLiveData<CategoryDomainModel> = _currentCategory

    private val _allCategories = MutableLiveData<List<CategoryDomainModel>>()
    val allCategories: LiveData<List<CategoryDomainModel>> = _allCategories

    private val _taskCreated = MutableLiveData<TaskDomainModel>()
    val taskCreated: LiveData<TaskDomainModel> = _taskCreated

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val disposables = CompositeDisposable()

    init {
        _currentTask.value = TaskDomainModel.TASK_ADD_NEW
        _currentCategory.value = CategoryDomainModel.CATEGORY_ADD_NEW

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

    fun gotTask(clickedTask: TaskDomainModel?) {
        clickedTask ?: return
        _currentTask.value = clickedTask
        _currentCategory.value = _currentCategory.value?.copy(
            id = 0,
            title = clickedTask.category,
            icon = clickedTask.categoryIcon,
            color = clickedTask.color
        )
    }

    private fun convertDateAndTime(date: String?, time: String?): Long {
        val myDateDue = date + time
        val sdf = SimpleDateFormat("dd.MM.yyyyHH:mm", Locale.ROOT)
        val dateDue = sdf.parse(myDateDue)
        return dateDue.time
    }

    fun addTask() {
        val currentTask = currentTask.value ?: return
        val title = currentTask.title
        val description = currentTask.description
        val category = currentTask.category
        val categoryIcon = currentTask.categoryIcon

        val color = currentTask.color

        _error.value = null

        when {
            title.isEmpty() -> {
                _error.value = "Empty title!"
            }
            description.isEmpty() -> {
                _error.value = "Empty description!"
            }
            category == "Pick a category" -> {
                _error.value = "Pick a category!"
            }
            currentTask.dateDue.isEmpty() -> {
                _error.value = "Empty date!"
            }
            currentTask.timeDue.isEmpty() -> {
                _error.value = "Empty time!"
            }
            else -> {
                val millis = convertDateAndTime(currentTask.dateDue, currentTask.timeDue)
                val newRepoTask = TaskRepoModel(
                    uniqueTaskId = currentTask.id,
                    title = title,
                    category = category,
                    categoryIcon = categoryIcon,
                    color = color,
                    dateAndTimeCreated = System.currentTimeMillis(),
                    dateAndTimeDue = millis,
                    description = description,
                    completedFlag = 0
                )

                disposables.add(
                    taskRepository.insertTask(newRepoTask)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            {
                                _taskCreated.value = it
                                _taskCreated.value = null
                            },
                            {
                                it.printStackTrace()
                            }
                        )
                )
            }
        }
    }

    fun onCategoryClicked(newlySelected: CategoryDomainModel) {
        _currentCategory.value = newlySelected
        _currentTask.value?.category = newlySelected.title
        _currentTask.value?.categoryIcon = newlySelected.icon
        _currentTask.value?.color = newlySelected.color
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
}
