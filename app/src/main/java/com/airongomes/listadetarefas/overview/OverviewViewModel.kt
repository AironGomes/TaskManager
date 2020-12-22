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
}