package com.airongomes.listadetarefas

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.airongomes.listadetarefas.database.Task

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