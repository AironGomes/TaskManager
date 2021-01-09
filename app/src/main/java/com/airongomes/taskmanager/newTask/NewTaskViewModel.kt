package com.airongomes.taskmanager.newTask

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airongomes.taskmanager.database.Task
import com.airongomes.taskmanager.database.TaskListDao
import kotlinx.android.synthetic.main.fragment_new_task.view.*
import kotlinx.coroutines.launch
import java.util.*

class NewTaskViewModel(
    dataSource: TaskListDao) : ViewModel() {

    // Instance of database
    private val database = dataSource

    // Priority value
    private var _priorityValue = MutableLiveData<Int>()
    val priorityValue: LiveData<Int>
        get() = _priorityValue

    // LiveData to date information
    private var _dateTask = MutableLiveData<Calendar>()
    val dateTask: LiveData<Calendar>
        get() = _dateTask

    // LiveData to time information
    private var _timeTask = MutableLiveData<Calendar>()
    val timeTask: LiveData<Calendar>
        get() = _timeTask

    // LiveData used when the fragment is closed
    private var _closeFragment = MutableLiveData<Boolean>()
    val closeFragment: LiveData<Boolean>
        get() = _closeFragment

    // LiveData used when the button_setDate is pressed
    private var _chooseDate = MutableLiveData<Boolean>()
    val chooseDate: LiveData<Boolean>
        get() = _chooseDate

    // LiveData used to show error message of empty title
    private var _emptyTitle = MutableLiveData<Boolean>()
    val emptyTitle: LiveData<Boolean>
        get() = _emptyTitle

    // Task Saved
    private var _saved = MutableLiveData<Boolean>()
    val saved: LiveData<Boolean>
        get() = _saved

    // Set allday instead of time
    private var allDay: Boolean = true

    /**
     * This function is responsible to insert the task to the database.
     * @param: View: is the ViewGroup from the fragment_new_task
     */
    fun saveTask(view: View) {

        if (view.new_title.editText?.text?.isEmpty() == true) {
            _emptyTitle.value = true
        }
        else {
            val task = Task()
            task.title = view.new_title.editText?.text.toString()
            task.description = view.new_description.editText?.text.toString()
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
                _dateTask.value = calendar
            }

            task.date = dateTask.value?.timeInMillis
            task.allDay = allDay

            viewModelScope.launch {
                database.insert(task)
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
     * Button Cancel: Close the New Task Fragment and don't save this task.
     */
    fun cancelTask(){
        _closeFragment.value = true
    }

    /**
     * Resets the livedata when the fragment is closed.
     */
    fun fragmentClosed() {
        _closeFragment.value = false
    }

    /**
     * Set the date Picker
     */
    fun getDate(cal: Calendar) {
        _dateTask.value = cal
    }

    /**
     * Set the time Picker
     */
    fun getTime(cal: Calendar) {
        _timeTask.value = cal
        allDay = false
    }

    /**
     * This function is responsible to reset the _emptyTitle.value
     */
    fun emptyTitleMessageShowed() {
        _emptyTitle.value = false
    }

    /**
     * Spinner from layout define the Priority Level
     */
    fun setPriority(priority: Int){
        _priorityValue.value = priority
    }

    /**
     * Checkbox allDay true
     */
    fun setAllDayToTrue(){
        allDay = true
        _timeTask.value = null
    }

}