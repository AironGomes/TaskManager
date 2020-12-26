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

    @Query("SELECT * from taskList_table ORDER BY priority DESC, year is null, year ASC, month is null, month ASC, day is null, day ASC")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("SELECT * from taskList_table WHERE taskId = :taskId")
    fun getTask(taskId: Long): LiveData<Task>

    @Query("DELETE from taskList_table")
    suspend fun deleteAllTasks()


}