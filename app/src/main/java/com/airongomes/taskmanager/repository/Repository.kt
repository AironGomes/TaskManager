package com.airongomes.taskmanager.repository

import com.airongomes.taskmanager.database.TaskListDao

class Repository(val database: TaskListDao) {

    val listAllTasks = database.getAllTasks()
    val listLowTasks = database.getLowTasks()
    val listMediumTasks = database.getMediumTasks()
    val listHighTasks = database.getHighTasks()
}