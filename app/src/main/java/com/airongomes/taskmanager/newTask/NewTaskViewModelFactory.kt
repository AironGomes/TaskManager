package com.airongomes.taskmanager.newTask

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.airongomes.taskmanager.database.TaskListDao

/**
 * This is pretty much boiler plate code for a ViewModel Factory.
 *
 * Provides the TaskDatabaseDao to the ViewModel.
 */
class NewTaskViewModelFactory(
        private val dataSource: TaskListDao,
        val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewTaskViewModel::class.java)) {
            return NewTaskViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}