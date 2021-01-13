package com.airongomes.taskmanager.overview

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import com.airongomes.taskmanager.adapter.MyDetailsLookup
import com.airongomes.taskmanager.database.Task
import com.airongomes.taskmanager.database.TaskListDao
import com.airongomes.taskmanager.repository.Repository
import kotlinx.coroutines.launch

/**
 * ViewModel from OverviewFragment
 * @param: dataSource = reference of TaskListDao
 */
class OverviewViewModel(
        dataSource: TaskListDao,
        application: Application) : ViewModel() {

    // Reference to Dao database
    val database = dataSource
    // Create an instance of Repository class
    private val repository = Repository(database)

    // Take the list of Tasks with correct filter
    val taskList = MediatorLiveData<List<Task>>()

    // Variable of filter
    private var filter = "all"

    // Observe the calling for NewTaskFragment
    private var _createNewTask = MutableLiveData<Boolean>()
    val createNewTask: LiveData<Boolean>
        get() = _createNewTask

    // Observe the calling for DetailFragment
    private var _navigateToTaskDetail = MutableLiveData<Long>()
    val navigateToTaskDetail: LiveData<Long>
        get() = _navigateToTaskDetail

    // Observe the calling after delete selection tasks
    private var _deletedTasks = MutableLiveData<Boolean>()
    val deletedTasks: LiveData<Boolean>
        get() = _deletedTasks

    /**
     * Add all sources to taskList MediatorLiveData and initialize it
      */
    init {
        taskList.addSource(repository.listAllTasks) { result ->
            if (filter == "all") {
                result?.let { taskList.value = it }
            }
        }
        taskList.addSource(repository.listLowTasks) { result ->
            if (filter == "low") {
                result?.let { taskList.value = it }
            }
        }
        taskList.addSource(repository.listMediumTasks) { result ->
            if (filter == "medium") {
                result?.let { taskList.value = it }
            }
        }
        taskList.addSource(repository.listHighTasks) { result ->
            if (filter == "high") {
                result?.let { taskList.value = it }
            }
        }
    }

    /**
     * Called by 'fab_newTask' button in fragment_overview.xml
      */
    fun createNewTask() {
        // Observable from OverviewFragment
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
     * Delete all data from database
     */
    fun deleteSelected(listOfKey: MutableList<Long>) {
        viewModelScope.launch {
            for(key in listOfKey){
                database.deleteItem(key)
            }
            _deletedTasks.value = true
        }
    }

    /**
     * Clear deletedTasks liveData
     */
    fun selectionCleared() {
        _deletedTasks.value = false
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


    fun updateFilterPriority(setfilter: String){
        Log.i("TEST", "UpdateFilterPriority called $filter")
        when(setfilter){
            "low" -> repository.listLowTasks.value?.let { taskList.value = it }
            "medium" -> repository.listMediumTasks.value?.let { taskList.value = it }
            "high" -> repository.listHighTasks.value?.let { taskList.value = it }
            else -> repository.listAllTasks.value?.let { taskList.value = it }
        }.also {
            filter = setfilter
        }
    }

}