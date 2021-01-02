package com.airongomes.listadetarefas

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.airongomes.listadetarefas.database.Task
import kotlinx.android.synthetic.main.fragment_about.view.*
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("itemTitle")
fun TextView.itemTitle(task: Task?) {
    task?.let {
        text = task.title
    }
}

@BindingAdapter("itemDescription")
fun TextView.itemDescription(task: Task?) {
    task?.let {
        text = task.description
    }
}

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

@BindingAdapter("setTime")
fun TextView.setTime(task: Task?) {
    task?.let {
        if(task.date != null && task.allDay == false){
            //val myFormat = "hh:mm a"
            val myFormat = "HH:mm"
            val sdf = SimpleDateFormat(myFormat, Locale.getDefault()) //Locale.US
            text = sdf.format(task.date)
        }
        else if(task.date != null && task.allDay == true){
            text = "Dia todo"
        }
    }
}

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

@BindingAdapter("setPriorityImage")
fun ImageView.setPriorityImage(priorityValue: Int?) {
    if (priorityValue == null) {
        setImageResource(R.drawable.priority_icon_low)
    } else {
        setImageResource(
            when (priorityValue) {
                0 -> R.drawable.priority_icon_low
                1 -> R.drawable.priority_icon_medium
                else -> R.drawable.priority_icon_high
            }
        )
    }
}


// Task Detail -----------------------------------------------------------------------

@BindingAdapter("titleDetail")
fun TextView.titleDetail(task: Task?) {
    task?.let {
        text = task.title
    }
}

@BindingAdapter("descriptionDetail")
fun TextView.descriptionDetail(task: Task?) {
    task?.let {
        text = task.description
    }
}

//@BindingAdapter("dateDetail")
//fun TextView.dateDetail(task: Task?) {
//    task?.let {
//        if (task.day != null && task.month != null && task.year != null) {
//            val day = task.day
//            val month = task.month
//            val year = task.year
//            var monthString = ""
//            when (month) {
//                0 -> monthString = "Jan"
//                1 -> monthString = "Fev"
//                2 -> monthString = "Mar"
//                3 -> monthString = "Abr"
//                4 -> monthString = "Mai"
//                5 -> monthString = "Jun"
//                6 -> monthString = "Jul"
//                7 -> monthString = "Ago"
//                8 -> monthString = "Set"
//                9 -> monthString = "Out"
//                10 -> monthString = "Nov"
//                11 -> monthString = "Dez"
//            }
//            val dateString = "$day $monthString $year"
//            text = dateString
//        }
//    }
//}

//@BindingAdapter("dateDetail")
//fun Button.dateDetail(task: Task?) {
//    task?.let {
//        text = if(task.year != null && task.month != null && task.day != null) {
//            val cal = Calendar.getInstance()
//            cal.set(Calendar.YEAR, task.year!!)
//            cal.set(Calendar.MONTH, task.month!!)
//            cal.set(Calendar.DAY_OF_MONTH, task.day!!)
//
//            val myFormat = "dd/MM/yyyy"
//            val sdf = SimpleDateFormat(myFormat, Locale.getDefault()) //Locale.US
//            sdf.format(cal.time)
//
//        } else {resources.getText(R.string.button_defineDate)}
//
//    }
//}
