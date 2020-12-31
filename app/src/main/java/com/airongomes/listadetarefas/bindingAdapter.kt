package com.airongomes.listadetarefas

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.airongomes.listadetarefas.adapter.TaskAdapter
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
        if (task.day != null && task.month != null && task.year != null) {
            var monthString = ""
            when(task.month) {
                0 -> monthString = "Jan"
                1 -> monthString = "Fev"
                2 -> monthString = "Mar"
                3 -> monthString = "Abr"
                4 -> monthString = "Mai"
                5 -> monthString = "Jun"
                6 -> monthString = "Jul"
                7 -> monthString = "Ago"
                8 -> monthString = "Set"
                9 -> monthString = "Out"
                10 -> monthString = "Nov"
                11 -> monthString = "Dez"
            }
            val dateString = "${task.day} $monthString"
            text = dateString
        }
    }
}
/*
@BindingAdapter("showDateString")
fun TextView.showDateString(date: Calendar?) {
    date?.let {
        val day = date.get(Calendar.DAY_OF_MONTH)
        val month = date.get(Calendar.MONTH)
        val year = date.get(Calendar.YEAR)
        var monthString = ""
        when(month) {
            0 -> monthString = "Jan"
            1 -> monthString = "Fev"
            2 -> monthString = "Mar"
            3 -> monthString = "Abr"
            4 -> monthString = "Mai"
            5 -> monthString = "Jun"
            6 -> monthString = "Jul"
            7 -> monthString = "Ago"
            8 -> monthString = "Set"
            9 -> monthString = "Out"
            10 -> monthString = "Nov"
            11 -> monthString = "Dez"
        }
        val dateString = "$day $monthString $year"
        text = dateString
    }
}

 */

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

/*
@BindingAdapter("setPriority")
fun TextView.setPriority(task: Task?) {
    task?.let {
        text = task.priority
    }
}

 */
@BindingAdapter("setPriorityImage")
fun ImageView.setPriorityImage(priorityValue: Int?) {
    if(priorityValue == null) {
        setImageResource(R.drawable.priority_icon_low)
    }
    else{
        setImageResource(when(priorityValue){
            0 -> R.drawable.priority_icon_low
            1 -> R.drawable.priority_icon_medium
            else -> R.drawable.priority_icon_high
        })
    }
//    priorityValue?.let {
//        setImageResource(when(priorityValue){
//            0 -> R.drawable.priority_icon_low
//            1 -> R.drawable.priority_icon_medium
//            else -> R.drawable.priority_icon_high
//        })
//    }
}

/*
@BindingAdapter("setBackground")
fun setBackground(view: View, task: Task?) {
    task?.let {
        when(task.priority){
            "Baixa" -> view.setBackgroundColor(Color.GREEN)
            "Média" -> view.setBackgroundColor(Color.YELLOW)
            "Alta" -> view.setBackgroundColor(Color.RED)
        }
    }
}

 */

/*
@BindingAdapter("setIcon")
fun ImageView.setIcon(task: Task?) {
    task?.let {
        when(task.priority){
            "Baixa" -> setImageResource(R.drawable.priority_icon_low)
            "Média" -> setImageResource(R.drawable.priority_icon_medium)
            "Alta" -> setImageResource(R.drawable.priority_icon_high)
        }
    }
}

 */

// Task Detail -----------------------------------------------------------------------

/*
@BindingAdapter("imageDetail")
fun ImageView.imageDetail(task: Task?) {
    task?.let {
        when(task.priority){
            "Baixa" -> setImageResource(R.drawable.priority_icon_low)
            "Média" -> setImageResource(R.drawable.priority_icon_medium)
            "Alta" -> setImageResource(R.drawable.priority_icon_high)
        }
    }
}

 */

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

@BindingAdapter("dateDetail")
fun TextView.dateDetail(task: Task?) {
    task?.let {
        if (task.day != null && task.month != null && task.year != null) {
            val day = task.day
            val month = task.month
            val year = task.year
            var monthString = ""
            when (month) {
                0 -> monthString = "Jan"
                1 -> monthString = "Fev"
                2 -> monthString = "Mar"
                3 -> monthString = "Abr"
                4 -> monthString = "Mai"
                5 -> monthString = "Jun"
                6 -> monthString = "Jul"
                7 -> monthString = "Ago"
                8 -> monthString = "Set"
                9 -> monthString = "Out"
                10 -> monthString = "Nov"
                11 -> monthString = "Dez"
            }
            val dateString = "$day $monthString $year"
            text = dateString
        }
    }
}

@BindingAdapter("dateDetail")
fun Button.dateDetail(task: Task?) {
    task?.let {
        text = if(task.year != null && task.month != null && task.day != null) {
            val cal = Calendar.getInstance()
            cal.set(Calendar.YEAR, task.year!!)
            cal.set(Calendar.MONTH, task.month!!)
            cal.set(Calendar.DAY_OF_MONTH, task.day!!)

            val myFormat = "dd/MM/yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.getDefault()) //Locale.US
            sdf.format(cal.time)

        } else {resources.getText(R.string.button_defineDate)}

    }
}
