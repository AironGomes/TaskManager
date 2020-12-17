package com.airongomes.listadetarefas.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "taskList_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    var taskId: Long = 0L,
    var title: String = "",
    var description: String = "",
    var priority: String = "")