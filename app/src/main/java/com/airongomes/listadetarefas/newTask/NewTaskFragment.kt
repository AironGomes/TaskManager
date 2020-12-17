package com.airongomes.listadetarefas.newTask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.airongomes.listadetarefas.R
import com.airongomes.listadetarefas.database.Task
import com.airongomes.listadetarefas.database.TaskListDatabase
import com.airongomes.listadetarefas.databinding.FragmentNewTaskBinding
import com.airongomes.listadetarefas.overview.OverviewViewModel
import kotlinx.android.synthetic.main.fragment_new_task.*

class NewTaskFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding : FragmentNewTaskBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_new_task, container,false)

        val application = requireNotNull(this.activity).application
        val dataSource = TaskListDatabase.getInstance(application).taskDatabaseDao

        val viewModelFactory = NewTaskViewModelFactory(dataSource, application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(NewTaskViewModel::class.java)

        binding.viewModel = viewModel

        binding.setLifecycleOwner(this)

        viewModel.closeFragment.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                this.findNavController().navigate(
                    NewTaskFragmentDirections.actionNewTaskFragmentToOverviewFragment())
                viewModel.fragmentClosed()
            }
        })


        binding.buttonOk.setOnClickListener{
            val task = Task()
            task.title = edit_title.text.toString()
            task.description = edit_description.text.toString()
            viewModel.saveTask(task)
        }


        return binding.root
    }
}