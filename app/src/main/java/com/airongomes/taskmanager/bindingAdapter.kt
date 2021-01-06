package com.airongomes.taskmanager

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.airongomes.taskmanager.database.Task
import java.text.SimpleDateFormat
import java.util.*

/** -------------------- New Task & Edit Task -------------------------------------- **/

// Show text in button_setDate from New Task && edit_button_setDate from Edit Task
@BindingAdapter("buttonSetDate")
fun Button.buttonSetDate(cal: Calendar?) {
    cal?.let {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault()) //Locale.US
        text = sdf.format(cal.time)
    }
    if(cal == null) {
        text = resources.getText(R.string.button_defineDate)
    }
}

// Show text in button_setTime from New Task && edit_button_setTime from Edit Task
@BindingAdapter("buttonSetTime")
fun Button.buttonSetTime(cal: Calendar?) {
    cal?.let {
        val myFormat = "HH:mm"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault()) //Locale.US
        text = sdf.format(cal.time)
    }
    if(cal == null) {
        text = resources.getText(R.string.button_defineTime)
    }
}

/** -------------------- List Item ----------------------------------------------- **/

// Set text in item_date
@BindingAdapter("setDate")
fun TextView.setDate(task: Task?) {
    task?.let {
        if(task.date != null){
            val myFormat = "dd/MM"
            val sdf = SimpleDateFormat(myFormat, Locale.getDefault()) //Locale.US
            text = sdf.format(task.date)
        }
    }
}

// Set text in item_time
@BindingAdapter("setTime")
fun TextView.setTime(task: Task?) {
    task?.let {
        if(task.date != null && !task.allDay){
            //val myFormat = "hh:mm a"
            val myFormat = "HH:mm"
            val sdf = SimpleDateFormat(myFormat, Locale.getDefault()) //Locale.US
            text = sdf.format(task.date)
        }
        else if(task.date != null && task.allDay){
            text = "Todo o dia"
        }
    }
}

/** -------------------- List Item && Task Detail ---------------------------------- **/

// Set image in priority_icon from Task Detail && iconPriority from List Item layout
@BindingAdapter("priorityIcon")
fun ImageView.priorityIcon(task: Task?) {
    task?.let {
        setImageResource(when(task.priority){
            0 -> R.drawable.priority_icon_low
            1 -> R.drawable.priority_icon_medium
            else -> R.drawable.priority_icon_high
        })
    }
}

/** -------------------- Task Detail && Edit Task ------------------------------------- **/

// Set text in view_title text from Task Detail && edit_title from Edit Task
@BindingAdapter("titleDetail")
fun TextView.titleDetail(task: Task?) {
    task?.let {
        text = task.title
    }
}

// Set text in view_description from Task Detal && edit_description from Edit Task
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
            text = "Não há data"
        }
        else {
            val myFormat = "EEE, d MMM yyy"
            val sdf = SimpleDateFormat(myFormat, Locale.getDefault()) //Locale.US
            text = sdf.format(task.date)
        }
    }

}

// Set text in view_time text
@BindingAdapter("viewSetTime")
fun TextView.viewSetTime(task: Task?) {
    task?.let {
        if(task.date != null && task.allDay) {
            //text = resources.getText(R.string.button_defineTime)
            text = "Todo o dia"
        }
        else if(task.date != null && !task.allDay) {
            val myFormat = "hh:mm aaa"
            val sdf = SimpleDateFormat(myFormat, Locale.getDefault()) //Locale.US
            text = sdf.format(task.date)
        }
    }
}

// Show label if task.date isn't null
@BindingAdapter("showLabel")
fun TextView.showLabel(task: Task?) {
    task?.let {
        if(task.date != null) text = "Horário"
    }
}

/** ------------------------------------------------------------------------------ **/