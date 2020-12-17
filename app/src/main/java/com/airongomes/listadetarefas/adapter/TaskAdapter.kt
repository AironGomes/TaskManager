package com.airongomes.listadetarefas.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.airongomes.listadetarefas.database.Task
import com.airongomes.listadetarefas.databinding.ListItemBinding

/**
 * This class create the adapter to be used with the recyclerview
 */
class TaskAdapter: androidx.recyclerview.widget.ListAdapter<Task, TaskViewHolder>(TaskDiffCalback()) {

    /**
     * Inflate the ListItemView fragment and return the TaskViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder.from(parent)
    }

    /**
     * Get the task from the position and associate the dataBinding with it
     */
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

/**
 * RecyclerViewHolder is responsible to manage the properties from onBindingVIewHolder and onCreateViewHolder
 */
class TaskViewHolder private constructor(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Task) {
        binding.task = item
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): TaskViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemBinding.inflate(layoutInflater, parent, false)
            return TaskViewHolder(binding)
        }
    }
}

/**
 * Use the ListAdapter to compare each item of the old list with the new list and accomplish the required changes
 */
class TaskDiffCalback: DiffUtil.ItemCallback<Task>() {
    /**
     * Use the task.id to know if it is the same task
     */
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.taskId == newItem.taskId
    }

    /**
     * Compare all properties of each task to know if it have updated
     */
    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}