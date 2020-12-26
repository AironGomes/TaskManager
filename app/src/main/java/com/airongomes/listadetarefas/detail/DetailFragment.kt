package com.airongomes.listadetarefas.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.airongomes.listadetarefas.R
import com.airongomes.listadetarefas.database.TaskListDatabase
import com.airongomes.listadetarefas.databinding.FragmentDetailBinding
import com.airongomes.listadetarefas.newTask.NewTaskFragmentDirections


class DetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentDetailBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_detail, container,false)

        val application = requireNotNull(this.activity).application
        val dataSource = TaskListDatabase.getInstance(application).taskDatabaseDao
        val arguments = DetailFragmentArgs.fromBundle(requireArguments())

        val viewModelFactory = DetailViewModelFactory(dataSource, arguments.taskId)

        val viewModel = ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)

        binding.viewModel = viewModel

        viewModel.closeDetail.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                this.findNavController().navigate(
                    DetailFragmentDirections.actionDetailFragmentToOverviewFragment())
                viewModel.detailFragmentClosed()
            }
        })
        
        // Set LifeCyclerOwer that able the fragment to observe LiveData
        binding.setLifecycleOwner(this)

        return binding.root
    }

}