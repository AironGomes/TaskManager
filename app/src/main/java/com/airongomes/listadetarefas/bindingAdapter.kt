package com.airongomes.listadetarefas

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import com.airongomes.listadetarefas.database.Task
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
            val dateString = "${task.day}/${task.month}/${task.year}"
            text = dateString
        }
    }
}

@BindingAdapter("showDateString")
fun TextView.showDateString(date: Calendar?) {
    date?.let {
        val day = date.get(Calendar.DAY_OF_MONTH)
        val month = date.get(Calendar.MONTH)
        val year = date.get(Calendar.YEAR)
        val dateString = "$day/$month/$year"
        text = dateString
    }
}
