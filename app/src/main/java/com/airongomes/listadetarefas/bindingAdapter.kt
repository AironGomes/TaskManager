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
