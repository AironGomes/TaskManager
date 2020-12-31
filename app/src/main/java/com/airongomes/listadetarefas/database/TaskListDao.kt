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

    @Query("SELECT * from taskList_table WHERE taskId = :taskId")
    fun getTask(taskId: Long): LiveData<Task>

    @Query("DELETE from taskList_table")
    suspend fun deleteAllTasks()

    @Query("SELECT * from taskList_table ORDER BY priority DESC, year is null, year ASC, month is null, month ASC, day is null, day ASC")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("SELECT * from taskList_table Where priority = 0 ORDER BY priority DESC, year is null, year ASC, month is null, month ASC, day is null, day ASC")
    fun getLowTasks(): LiveData<List<Task>>

    @Query("SELECT * from taskList_table Where priority = 1 ORDER BY priority DESC, year is null, year ASC, month is null, month ASC, day is null, day ASC")
    fun getMediumTasks(): LiveData<List<Task>>

    @Query("SELECT * from taskList_table Where priority = 2 ORDER BY priority DESC, year is null, year ASC, month is null, month ASC, day is null, day ASC")
    fun getHighTasks(): LiveData<List<Task>>

    //    @Query("SELECT * from taskList_table WHERE priority IN (:values) ORDER BY priority DESC, year is null, year ASC, month is null, month ASC, day is null, day ASC")
//    fun getAllTasks2(values: List<Int>): LiveData<List<Task>>
}
