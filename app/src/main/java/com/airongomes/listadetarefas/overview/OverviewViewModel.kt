package com.airongomes.listadetarefas.overview

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airongomes.listadetarefas.database.Task
import com.airongomes.listadetarefas.database.TaskListDao
import kotlinx.coroutines.launch

class OverviewViewModel(
        dataSource: TaskListDao,
        application: Application) : ViewModel() {

    val database = dataSource
    val taskList = database.getAllTasks()

    private var _createNewTask = MutableLiveData<Boolean>()
    val createNewTask: LiveData<Boolean>
        get() = _createNewTask

    private var _navigateToTaskDetail = MutableLiveData<Long>()
    val navigateToTaskDetail: LiveData<Long>
        get() = _navigateToTaskDetail

    fun createNewTask() {
        _createNewTask.value = true
    }

    fun createNewTaskStarted() {
        _createNewTask.value = false
    }

    /**
     * Delete all data from database
     */
    fun deleteAllFromDatabase() {
        viewModelScope.launch {
            database.deleteAllTasks()
        }
    }

    /**
     * When clicked set the navigateToTaskDetail LiveData
     */
    fun onTaskClicked(taskId: Long) {
        _navigateToTaskDetail.value = taskId
    }

    /**
     * Navigate to TaskDetail and set the LiveData to null
     */
    fun onTaskDetailNavigated() {
        _navigateToTaskDetail.value = null
    }
}