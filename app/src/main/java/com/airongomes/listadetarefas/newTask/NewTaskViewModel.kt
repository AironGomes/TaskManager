package com.airongomes.listadetarefas.newTask

import android.app.Application
import android.view.View
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
    dataSource: TaskListDao,
    application: Application) : ViewModel() {

    // Instance of database
    val database = dataSource

    // LiveData to date information
    private var _dateTask = MutableLiveData<Calendar>()
    val dateTask: LiveData<Calendar>
        get() = _dateTask

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

    /**
     * This function is responsible to insert the task to the database.
     * @param: View: is the ViewGroup from the fragment_new_task
     */
    fun saveTask(view: View) {
        if (view.edit_title.text.isEmpty()) {
            _emptyTitle.value = true
        }
        else {
            val task = Task()
            task.title = view.edit_title.text.toString()
            task.description = view.edit_description.text.toString()
            task.year = dateTask.value?.get(Calendar.YEAR)
            task.month = dateTask.value?.get(Calendar.MONTH)
            task.day = dateTask.value?.get(Calendar.DAY_OF_MONTH)


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
     * This function is activated by button_setDate from fragment_new_task
     */
    fun chooseDate() {
        _chooseDate.value = true
    }

    /**
     * This function is responsible to reset the _chooseDate.value
     */
    fun chosenDate() {
        _chooseDate.value = false
    }

    /**
     * Set the date from DataPickerFragment into the dateTask LiveData
     */
    fun getDate(date: Calendar) {
        _dateTask.value = date
    }

    /**
     * This function is responsible to reset the _emptyTitle.value
     */
    fun emptyTitleMessageShowed() {
        _emptyTitle.value = false
    }

    /**
     * This function is responsible to reset the LiveData dateTask
     */
    fun resetDateTask() {
        _dateTask.value = null
    }

}