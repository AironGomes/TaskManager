package com.airongomes.listadetarefas.newTask

import android.app.Application
import android.view.View
import androidx.core.view.get
import androidx.core.view.isEmpty
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airongomes.listadetarefas.database.Task
import com.airongomes.listadetarefas.database.TaskListDao
import kotlinx.android.synthetic.main.fragment_new_task.view.*
import kotlinx.coroutines.launch
import java.text.DateFormat
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

    // Set allday instead of time
    private var allDay: Boolean = true

    /**
     * This function is responsible to insert the task to the database.
     * @param: View: is the ViewGroup from the fragment_new_task
     */
    fun saveTask(view: View) {

        if (view.edit_title.editText?.text?.isEmpty() == true) {
            _emptyTitle.value = true
        }
        else {
            val task = Task()
            task.title = view.edit_title.editText?.text.toString()
            task.description = view.edit_description.editText?.text.toString()
            task.priority = priorityValue.value!!

            timeTask.value?.let {
                val calendar = Calendar.getInstance()
                dateTask.value?.get(Calendar.YEAR)?.let { calendar.set(Calendar.YEAR, it) }
                dateTask.value?.get(Calendar.MONTH)?.let { calendar.set(Calendar.MONTH, it) }
                dateTask.value?.get(Calendar.DAY_OF_MONTH)?.let { calendar.set(Calendar.DAY_OF_MONTH, it) }
                timeTask.value?.get(Calendar.HOUR_OF_DAY)?.let { calendar.set(Calendar.HOUR_OF_DAY, it)}
                timeTask.value?.get(Calendar.MINUTE)?.let { calendar.set(Calendar.MINUTE, it)}
                calendar.set(Calendar.SECOND, 0)
                _dateTask.value = calendar
            }

            task.date = dateTask.value?.timeInMillis
            //task.time = timeTask.value?.timeInMillis
            task.allDay = allDay

            viewModelScope.launch {
                database.insert(task)
            }
            _closeFragment.value = true
        }
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
//        date.set(Calendar.YEAR, cal.get(Calendar.YEAR))
//        date.set(Calendar.MONTH, cal.get(Calendar.MONTH))
//        date.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH))
        _dateTask.value = cal
    }

    /**
     * Set the time Picker
     */
    fun getTime(cal: Calendar) {
//        time.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY))
//        time.set(Calendar.MINUTE, cal.get(Calendar.MINUTE))

        _timeTask.value = cal
        allDay = false
        //_dateTask.value = calendar
    }

    /**
     * This function is responsible to reset the _emptyTitle.value
     */
    fun emptyTitleMessageShowed() {
        _emptyTitle.value = false
    }

    fun setPriority(priority: Int){
        _priorityValue.value = priority
    }

    /**
     * Checkbox allDay true
     */
    fun setAllDayToTrue(){
        //_allDay.value = true
        allDay = true
        _timeTask.value = null
    }

}