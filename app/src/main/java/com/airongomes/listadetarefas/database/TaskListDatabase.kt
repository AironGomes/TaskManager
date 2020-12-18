package com.airongomes.listadetarefas.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 4, exportSchema = false)
abstract class TaskListDatabase: RoomDatabase() {

    abstract val taskDatabaseDao: TaskListDao

    companion object {

        private lateinit var INSTANCE: TaskListDatabase

        fun getInstance(context: Context): TaskListDatabase {
            synchronized(this) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        TaskListDatabase::class.java,
                        "taskDatabase")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }
    }
}