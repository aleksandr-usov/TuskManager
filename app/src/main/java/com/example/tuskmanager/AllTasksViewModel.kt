package com.example.tuskmanager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tuskmanager.data.domain.model.TaskDomainModel
import com.example.tuskmanager.data.repo.TaskRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class AllTasksViewModel constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _allTasks = MutableLiveData<List<TaskDomainModel>>()

    private val _tasksToDisplay = MutableLiveData<List<TaskDomainModel>>()
    val tasksToDisplay: LiveData<List<TaskDomainModel>> = _tasksToDisplay

    private val _swipedTask = MutableLiveData<TaskDomainModel>()

    private var _sortFlag = MutableLiveData(1)
    var sortFlag: LiveData<Int> = _sortFlag

    private val disposables = CompositeDisposable()

    init {
        sort()
    }

    fun searchTasks(query: String) {
        if (query.isEmpty()) {
            _tasksToDisplay.value = _allTasks.value
        } else {
            _tasksToDisplay.value = _allTasks.value?.filter {
                with(it) {
                    description.contains(query, true) ||
                            title.contains(query, true) ||
                            category.contains(query, true)
                }
            }
        }
    }

    fun sortByCategory(category: String) {
        _tasksToDisplay.value = _allTasks.value
        _tasksToDisplay.value = _tasksToDisplay.value?.filter {
            it.category == category
        }
    }

    fun sort() {
        disposables.add(
            taskRepository.getAllTasks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::sortTasks) { it.printStackTrace() }
        )
        if (_sortFlag.value!! >= 1) _sortFlag.value = 0
        else _sortFlag.value = _sortFlag.value?.plus(1)
    }

    private fun sortTasks(allTasks: List<TaskDomainModel>) {
        when (_sortFlag.value) {
            0 -> {
                _allTasks.value = allTasks.filter { !it.isComplete }
            }
            1 -> {
                _allTasks.value = allTasks.filter { it.isComplete }
            }
        }
        _allTasks.value =
            _allTasks.value?.sortedBy { convertDateAndTime(it.dateDue, it.timeDue) }
        _tasksToDisplay.value = _allTasks.value
    }

    fun getSwipedTask(task: TaskDomainModel?) {
        _swipedTask.value = task
    }

    private fun convertDateAndTime(date: String?, time: String?): Long {
        val myDateDue = date + time
        val sdf = SimpleDateFormat("dd.MM.yyyyHH:mm", Locale.ROOT)
        val dateDue = sdf.parse(myDateDue)
        return dateDue.time
    }

    fun completeOnSwipe() {
        val swiped = _swipedTask.value ?: return
        disposables.add(
            taskRepository.markTaskCompleted(swiped.id)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {},
                    {
                        it.printStackTrace()
                    }
                )
        )
        _allTasks.value = _allTasks.value
        _swipedTask.value = TaskDomainModel.TASK_ADD_NEW
    }

    fun delayOnSwipe() {
        disposables.add(
            taskRepository.delayTaskByDay(_swipedTask.value?.id ?: 0)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {},
                    {
                        it.printStackTrace()
                    }
                )
        )
        _swipedTask.value = TaskDomainModel.TASK_ADD_NEW
    }
}
