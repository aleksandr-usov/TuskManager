package com.example.tuskmanager

import android.util.Log
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
        val millis = convertDateAndTime(_currentTask.value?.dateDue, _currentTask.value?.timeDue)

        val newRepoTask = TaskRepoModel(
            uniqueTaskId = _currentTask.value?.id ?: 0,
            title = _currentTask.value?.title ?: "",
            category = _currentTask.value?.category ?: "",
            categoryIcon = _currentTask.value?.categoryIcon ?: "",
            color = _currentTask.value?.color ?: "",
            dateAndTimeCreated = System.currentTimeMillis(),
            dateAndTimeDue = millis,
            description = _currentTask.value?.description ?: "",
            completedFlag = 0
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
