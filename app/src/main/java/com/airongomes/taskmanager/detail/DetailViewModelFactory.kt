package com.airongomes.taskmanager.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.airongomes.taskmanager.database.TaskListDao

/**
 * ViewModel Factory. Provides the dataSource and Task id to the ViewModel.
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