package com.airongomes.taskmanager.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airongomes.taskmanager.database.Task
import com.airongomes.taskmanager.database.TaskListDao
import kotlinx.coroutines.launch

/**
 * ViewModel from DetailFragment
 */
class DetailViewModel(
    dataSource: TaskListDao,
    taskKey: Long  = 0L) : ViewModel() {

    // Reference to Dao database
    val database = dataSource

    // LiveData of task
    val task: LiveData<Task>
//    fun getTask() = task.value

    // LiveData to observe when this fragment is closed
    private val _closeDetail = MutableLiveData<Boolean>()
    val closeDetail: LiveData<Boolean>
        get() = _closeDetail

    // LiveData to observe when the EditTask Fragment is called
    private val _editTask = MutableLiveData<Boolean>()
    val editTask: LiveData<Boolean>
        get() = _editTask

    /**
     * Initiate task liveData with database
     */
    init {
        task = database.getTask(taskKey)
    }

    /**
     * Delete the actual task
     */
    fun deleteTask() {
        viewModelScope.launch {
            task.value?.let {
                database.deleteItem(it.taskId) }
            closeDetailFragment()
        }
    }

    /**
     * Close this fragment
     */
    fun closeDetailFragment() {
        _closeDetail.value = true
    }

    /**
     * Update the LiveData when closed
     */
    fun detailFragmentClosed() {
        _closeDetail.value = false
    }

    /**
     * Called by 'editable_icon' ImageView
     */
    fun editTask() {
        _editTask.value = true
    }

    /**
     * Update the LiveData when editTask is called
     */
    fun editTaskCalled() {
        _editTask.value = false
    }

}