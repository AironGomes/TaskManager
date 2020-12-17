package com.airongomes.listadetarefas.overview

import OverviewViewMolderFactory
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
import com.airongomes.listadetarefas.adapter.TaskAdapter
import com.airongomes.listadetarefas.database.TaskListDatabase
import com.airongomes.listadetarefas.databinding.FragmentOverviewBinding


class OverviewFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding : FragmentOverviewBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_overview, container,false)

        val application = requireNotNull(this.activity).application
        val dataSource = TaskListDatabase.getInstance(application).taskDatabaseDao

        val viewModelFactory = OverviewViewMolderFactory(dataSource, application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(OverviewViewModel::class.java)

        binding.viewModel = viewModel

        binding.setLifecycleOwner(this)

        viewModel.createNewTask.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                this.findNavController().navigate(
                    OverviewFragmentDirections.actionOverviewFragmentToNewTaskFragment()
                )
                viewModel.createNewTaskStarted()
            }
        })

        val adapter = TaskAdapter()
        binding.recyclerview.adapter = adapter

        viewModel.taskList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }
}



