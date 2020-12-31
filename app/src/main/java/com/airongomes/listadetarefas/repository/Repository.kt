package com.airongomes.listadetarefas.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.airongomes.listadetarefas.database.Task
import com.airongomes.listadetarefas.database.TaskListDao

class Repository(val database: TaskListDao) {

    val listAllTasks = database.getAllTasks()
    val listLowTasks = database.getLowTasks()
    val listMediumTasks = database.getMediumTasks()
    val listHighTasks = database.getHighTasks()
}