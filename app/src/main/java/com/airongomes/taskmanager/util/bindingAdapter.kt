package com.airongomes.taskmanager.util

import android.os.Build
import android.text.format.DateFormat.getDateFormat
import android.text.format.DateUtils
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.airongomes.taskmanager.R
import com.airongomes.taskmanager.database.Task
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

/** -------------------- New Task & Edit Task -------------------------------------- **/

// Show text in button_setDate from New Task && edit_button_setDate from Edit Task
@BindingAdapter("buttonSetDate")
fun Button.buttonSetDate(cal: Calendar?) {
    cal?.let {
        val dateFormatted = formatDateStyleShort(it, context)
        text = dateFormatted
        contentDescription = dateFormatted

    }
    if(cal == null) {
        text = resources.getText(R.string.button_defineDate)
        contentDescription = resources.getText(R.string.button_defineDate)
    }
}

// Show text in button_setTime from New Task && edit_button_setTime from Edit Task
@BindingAdapter("buttonSetTime")
fun Button.buttonSetTime(cal: Calendar?) {
    cal?.let {
        val timeFormatted = formatTime(it, context)
        text = timeFormatted
        contentDescription = timeFormatted
    }
    if(cal == null) {
        text = resources.getText(R.string.button_defineTime)
        contentDescription = resources.getText(R.string.button_defineTime)
    }
}

/** -------------------- List Item ----------------------------------------------- **/

// Set text in item_date
@BindingAdapter("setDate")
fun TextView.setDate(task: Task?) {
    task?.let {
        if(it.date != null){
            when {
                DateUtils.isToday(it.date!!) -> {
                    text = resources.getText(R.string.today)
                }
                DateUtils.isToday(it.date!! + DateUtils.DAY_IN_MILLIS) -> {
                    text = resources.getText(R.string.yesterday)
                }
                DateUtils.isToday(it.date!! - DateUtils.DAY_IN_MILLIS) -> {
                    text = resources.getText(R.string.tomorrow)
                }
                else -> {
                    val calendar = Calendar.getInstance()
                    calendar.time = Date(it.date!!)
                    val dateFormatted = formatDateStyleShort(calendar, context)
                    text = dateFormatted
                    contentDescription = dateFormatted

                }
            }
        }
        else{
            text = ""
        }
    }
}

// Set text in item_time
@BindingAdapter("setTime")
fun TextView.setTime(task: Task?) {
    task?.let {
        if(task.date != null && !task.allDay){
            val calendar = Calendar.getInstance()
            calendar.time = Date(it.date!!)
            val timeFormatted = formatTime(calendar, context)
            text = timeFormatted
            contentDescription = timeFormatted
        }
        else if(task.date != null && task.allDay){
            text = resources.getText(R.string.lb_allDay)
            contentDescription = resources.getText(R.string.lb_allDay)
        }
        else{
            text = ""
        }
    }
}

/** -------------------- List Item && Task Detail ---------------------------------- **/

// Set image in priority_icon from Task Detail && iconPriority from List Item layout
@BindingAdapter("priorityIcon")
fun ImageView.priorityIcon(task: Task?) {
    task?.let {
        when(task.priority){
            0 -> {
                setImageResource(R.drawable.priority_icon_low)
                contentDescription = resources.getText(R.string.low_priority)
            }
            1 -> {
                setImageResource(R.drawable.priority_icon_medium)
                contentDescription = resources.getText(R.string.medium_priority)
            }
            else -> {
                setImageResource(R.drawable.priority_icon_high)
                contentDescription = resources.getText(R.string.high_priority)
            }
        }
    }
}

// Set image in priority_icon from Task Detail && iconPriority from ListItem layout in Tablet Layout
@BindingAdapter("priorityIconTablet")
fun ImageView.priorityIconTablet(task: Task?) {
    task?.let {
        when(task.priority){
            0 -> {
                setImageResource(R.drawable.priority_icon_big_low)
                contentDescription = resources.getText(R.string.low_priority)
            }
            1 -> {
                setImageResource(R.drawable.priority_icon_big_medium)
                contentDescription = resources.getText(R.string.medium_priority)
            }
            else -> {
                setImageResource(R.drawable.priority_icon_big_high)
                contentDescription = resources.getText(R.string.high_priority)
            }
        }
    }
}

/** -------------------- Task Detail && Edit Task ------------------------------------- **/

// Set text in view_title text from Task Detail && edit_title from Edit Task
@BindingAdapter("titleDetail")
fun TextView.titleDetail(task: Task?) {
    task?.let {
        text = task.title
        contentDescription = task.title
    }
}

// Set text in view_description from Task Detail && edit_description from Edit Task
@BindingAdapter("descriptionDetail")
fun TextView.descriptionDetail(task: Task?) {
    task?.let {
        text = task.description
    }
}

/** -------------------- Task Detail  ----------------------------------------------- **/

// Set text in view_date text
@BindingAdapter("viewSetDate")
fun TextView.viewSetDate(task: Task?) {
    task?.let {
        if(task.date == null) {
            //text = resources.getText(R.string.button_defineDate)
            text = resources.getText(R.string.withoutDate)
            contentDescription = resources.getText(R.string.withoutDate)
        }
        else {
            val calendar = Calendar.getInstance()
            calendar.time = Date(it.date!!)
            val dateFormatted = formatDateStyleLong(calendar, context)
            var dateString = ""
            dateString = when {
                DateUtils.isToday(task.date!!) -> {
                    context.getString(R.string.today)
                }
                DateUtils.isToday(task.date!! + DateUtils.DAY_IN_MILLIS) -> {
                    context.getString(R.string.yesterday)
                }
                DateUtils.isToday(task.date!! - DateUtils.DAY_IN_MILLIS) -> {
                    context.getString(R.string.tomorrow)
                }
                else -> {
                    dateFormatted
                }
            }
            text = dateString
            contentDescription = dateFormatted
        }
    }

}

// Set text in view_time text
@BindingAdapter("viewSetTime")
fun TextView.viewSetTime(task: Task?) {
    task?.let {
        if(task.date != null && task.allDay) {
            //text = resources.getText(R.string.button_defineTime)
            text = resources.getText(R.string.lb_allDay)
            contentDescription = resources.getText(R.string.lb_allDay)
        }
        else if(task.date != null && !task.allDay) {
            val calendar = Calendar.getInstance()
            calendar.time = Date(it.date!!)
            val timeFormatted = formatTime(calendar, context)
            text = timeFormatted
            contentDescription = timeFormatted
        }
    }
}

// Show label if task.date isn't null
@BindingAdapter("showLabel")
fun TextView.showLabel(task: Task?) {
    task?.let {
        if(task.date != null) {
            text = resources.getText(R.string.lb_time)
            contentDescription = resources.getText(R.string.lb_time)
        }
    }
}

/** -------------------- Edit Task  ------------------------------------------------- **/

@BindingAdapter("enableCheckbox")
fun CheckBox.enableCheckbox(cal: Calendar?) {
    isEnabled = cal != null
}