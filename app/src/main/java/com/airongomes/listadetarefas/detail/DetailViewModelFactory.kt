package com.airongomes.listadetarefas.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.airongomes.listadetarefas.database.TaskListDao

/**
 * This is pretty much boiler plate code for a ViewModel Factory.
 *
 * Provides the TaskDatabaseDao and context to the ViewModel.
 */
class DetailViewModelFactory(
    private val dataSource: TaskListDao,
    private val taskKey: Long
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(dataSource, taskKey) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}