package com.airongomes.listadetarefas.newTask

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airongomes.listadetarefas.database.Task
import com.airongomes.listadetarefas.database.TaskListDao
import kotlinx.coroutines.launch

class NewTaskViewModel(
    dataSource: TaskListDao,
    application: Application) : ViewModel() {

    val database = dataSource

    private var _closeFragment = MutableLiveData<Boolean>()
    val closeFragment: LiveData<Boolean>
        get() = _closeFragment

    fun saveTask(task: Task) {

        viewModelScope.launch {
            database.insert(task)
        }


        _closeFragment.value = true
    }


    fun cancelTask(){
        _closeFragment.value = true
    }

    fun fragmentClosed() {
        _closeFragment.value = false
    }

}