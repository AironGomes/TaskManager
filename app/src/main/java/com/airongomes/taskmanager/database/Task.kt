package com.airongomes.taskmanager.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "taskList_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    var taskId: Long = 0L,
    var title: String = "",
    var description: String = "",
    var priority: Int = 0,
    var date: Long? = null,
    var allDay: Boolean = true)