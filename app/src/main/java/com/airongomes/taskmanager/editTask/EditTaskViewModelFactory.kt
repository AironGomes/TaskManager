package com.airongomes.taskmanager.editTask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.airongomes.taskmanager.database.TaskListDao

/**
 * ViewModel Factory. Provides the dataSource and Task id to the ViewModel.
 */
class EditTaskViewModelFactory(
    private val dataSource: TaskListDao,
    private val taskKey: Long
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditTaskViewModel::class.java)) {
            return EditTaskViewModel(dataSource, taskKey) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}