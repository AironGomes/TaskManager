package com.airongomes.listadetarefas.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airongomes.listadetarefas.database.Task
import com.airongomes.listadetarefas.database.TaskListDao
import kotlinx.coroutines.launch

class DetailViewModel(
    dataSource: TaskListDao,
    taskKey: Long  = 0L) : ViewModel() {

    val database = dataSource

    private val task: LiveData<Task>
    fun getTask() = task

    private val _closeDetail = MutableLiveData<Boolean>()
    val closeDetail: LiveData<Boolean>
        get() = _closeDetail


    init {
        task = database.getTask(taskKey)
    }

    fun deleteTask() {
        viewModelScope.launch {
            task.value?.let {
                database.delete(it) }
            closeDetailFragment()
        }
    }

    fun closeDetailFragment() {
        _closeDetail.value = true
    }

    fun detailFragmentClosed() {
        _closeDetail.value = false
    }
}