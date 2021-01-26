package com.airongomes.taskmanager.newTask

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import android.view.View
import androidx.core.app.AlarmManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.airongomes.taskmanager.database.Task
import com.airongomes.taskmanager.database.TaskListDao
import com.airongomes.taskmanager.notification.AlarmReceiver
import com.airongomes.taskmanager.notification.sendNotification
import com.airongomes.taskmanager.util.formatDateStyleLong
import com.airongomes.taskmanager.util.formatDateStyleShort
import com.airongomes.taskmanager.util.formatDateTimeStyleLong
import kotlinx.android.synthetic.main.fragment_new_task.view.*
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class NewTaskViewModel(
        dataSource: TaskListDao,
        application: Application) : AndroidViewModel(application) {

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

    private val alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val notifyIntent = Intent(application, AlarmReceiver::class.java)
    private val REQUEST_CODE = 0
    private val notifyPendingIntent: PendingIntent = PendingIntent.getBroadcast(
    getApplication(),
    REQUEST_CODE,
    notifyIntent,
    PendingIntent.FLAG_UPDATE_CURRENT
    )


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
            val calendar = Calendar.getInstance()
            timeTask.value?.let {
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

            // Create notification - TODO: Implement Alarm Receiver
//            task.date?.let {
//                val message = if(task.allDay){
//                    val timeFormatted = formatDateStyleLong(calendar, getApplication())
//                    "${task.title} - $timeFormatted"
//                } else{
//                    val timeFormatted = formatDateTimeStyleLong(calendar, getApplication())
//                    "${task.title} - $timeFormatted"
//                }
//
//                createNotification(message)
//            }
            _saved.value = true
        }
    }

    private fun createNotification(message: String) {
        val notificationManager = ContextCompat.getSystemService(
                getApplication(),
                NotificationManager::class.java
        ) as NotificationManager
        notificationManager.sendNotification(message, getApplication())
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