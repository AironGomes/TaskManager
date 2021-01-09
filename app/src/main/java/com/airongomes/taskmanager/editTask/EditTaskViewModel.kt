package com.airongomes.taskmanager.editTask

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airongomes.taskmanager.database.Task
import com.airongomes.taskmanager.database.TaskListDao
import kotlinx.android.synthetic.main.fragment_edit_task.view.*
import kotlinx.coroutines.launch
import java.util.*


/**
 * ViewModel from DetailFragment
 */
class EditTaskViewModel(
    dataSource: TaskListDao,
    private val taskKey: Long  = 0L) : ViewModel() {

    // Reference to Dao database
    val database = dataSource

    // LiveData of task
    val task: LiveData<Task>
//    fun getTask() = task.value

    // Priority value
    // LiveData to observe when this fragment is closed
    private val _priorityValue = MutableLiveData<Int>()
    val priorityValue: LiveData<Int>
        get() = _priorityValue

    // LiveData to observe when this fragment is closed
    private val _closeEditTask = MutableLiveData<Boolean>()
    val closeEditTask: LiveData<Boolean>
        get() = _closeEditTask

    // LiveData used to show error message of empty title
    private var _emptyTitle = MutableLiveData<Boolean>()
    val emptyTitle: LiveData<Boolean>
        get() = _emptyTitle

    // LiveData to date information
    private var _dateTask = MutableLiveData<Calendar>()
    val dateTask: LiveData<Calendar>
        get() = _dateTask

    // LiveData to time information
    private var _timeTask = MutableLiveData<Calendar>()
    val timeTask: LiveData<Calendar>
        get() = _timeTask

    // Set allday instead of time
    private var _allDay = MutableLiveData<Boolean>()
    val allDay: LiveData<Boolean>
        get() = _allDay

    // Task Saved
    private var _saved = MutableLiveData<Boolean>()
    val saved: LiveData<Boolean>
        get() = _saved


    /**
     * Initiate task liveData with database
     */
    init {
        task = database.getTask(taskKey)
        _priorityValue.value = task.value?.priority
    }

    /**
     * Initiate calendar from Task database
     */
    fun startCalendar(task: Task) {
        if(task.date != null) {
            val cal = Calendar.getInstance()
            cal.timeInMillis = task.date!!
            _dateTask.value = cal
            if(!task.allDay) {
                _timeTask.value = cal
            }
        }
        _allDay.value = task.allDay
//        if(task.time != null) {
//            val cal = Calendar.getInstance()
//            cal.timeInMillis = task.time!!
//            _timeTask.value = cal
//        }
    }

    /**
     * Close this fragment
     */
    fun closeEditTask() {
        _closeEditTask.value = true
    }

    /**
     * Update the LiveData when closed
     */
    fun editTaskClosed() {
        _closeEditTask.value = false
    }

    /**
     * Spinner from layout define the Priority Level
     */
    fun setPriority(priority: Int){
        _priorityValue.value = priority
    }

    /**
     * Update the task in database
     */
    fun updateTask(view: View) {
        if (view.edit_title.editText?.text?.isEmpty() == true) {
            _emptyTitle.value = true
        }
        else {
            val task = Task()
            task.taskId = taskKey
            task.title = view.edit_title.editText?.text.toString()
            task.description = view.edit_description.editText?.text.toString()
            allDay.value?.let {task.allDay = it}
            task.priority = priorityValue.value!!
            timeTask.value?.let {
                val calendar = Calendar.getInstance()
                dateTask.value?.get(Calendar.YEAR)?.let { calendar.set(Calendar.YEAR, it) }
                dateTask.value?.get(Calendar.MONTH)?.let { calendar.set(Calendar.MONTH, it) }
                dateTask.value?.get(Calendar.DAY_OF_MONTH)?.let { calendar.set(Calendar.DAY_OF_MONTH, it) }
                timeTask.value?.get(Calendar.HOUR_OF_DAY)?.let { calendar.set(Calendar.HOUR_OF_DAY, it)}
                timeTask.value?.get(Calendar.MINUTE)?.let { calendar.set(Calendar.MINUTE, it)}
                timeTask.value?.get(Calendar.SECOND)?.let { calendar.set(Calendar.SECOND, it)}
                timeTask.value?.get(Calendar.MILLISECOND)?.let { calendar.set(Calendar.MILLISECOND, it)}
                calendar.set(Calendar.SECOND, 0)
                _dateTask.value = calendar
            }
            task.date = dateTask.value?.timeInMillis

            viewModelScope.launch {
                database.update(task)
                Log.i("TEST", "SALVO")
            }
            _saved.value = true
        }
    }

    /**
     * Reset the saved LiveData
     */
    fun savedMessageShowed() {
        _saved.value = false
    }

    /**
     * This function is responsible to reset the _emptyTitle.value
     */
    fun emptyTitleMessageShowed() {
        _emptyTitle.value = false
    }

    /**
     * Update dateTask LiveData
     */
    fun getDate(cal: Calendar) {
        _dateTask.value = cal
    }

    /**
     * Set the time Picker
     */
    fun getTime(cal: Calendar) {
        _timeTask.value = cal
        _allDay.value = false
    }

    /**
     * Checkbox allDay true
     */
    fun setAllDayToTrue(){
        _allDay.value = true
        _timeTask.value = null
    }
}