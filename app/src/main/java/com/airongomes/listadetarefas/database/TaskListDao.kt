package com.airongomes.listadetarefas.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskListDao {

    @Insert
    suspend fun insert(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Update
    suspend fun update(task: Task)

    @Query("SELECT * from taskList_table ORDER BY taskId DESC")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("DELETE from taskList_table")
    suspend fun deleteAllTasks()
}