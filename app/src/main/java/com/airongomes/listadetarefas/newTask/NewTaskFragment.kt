package com.airongomes.listadetarefas.newTask

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.airongomes.listadetarefas.R
import com.airongomes.listadetarefas.database.TaskListDatabase
import com.airongomes.listadetarefas.databinding.FragmentNewTaskBinding
import com.google.android.material.snackbar.Snackbar

class NewTaskFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding : FragmentNewTaskBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_new_task, container,false)

        val application = requireNotNull(this.activity).application
        val dataSource = TaskListDatabase.getInstance(application).taskDatabaseDao
        // Instantiate the ViewModel
        val viewModelFactory = NewTaskViewModelFactory(dataSource, application)
        val viewModel: NewTaskViewModel by activityViewModels{viewModelFactory}
        // Associate the dataBinding with the viewModel
        binding.viewModel = viewModel
        // Set LifeCyclerOwer that able the fragment to observe LiveData
        binding.setLifecycleOwner(this)

        // Observe the closeFragment LiveData
        viewModel.closeFragment.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                this.findNavController().navigate(
                    NewTaskFragmentDirections.actionNewTaskFragmentToOverviewFragment())
                viewModel.fragmentClosed()
            }
        })

        // Observe the chooseDate LiveData
        viewModel.chooseDate.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                val dialogFragment: DialogFragment = DataPickerFragment()
                dialogFragment.show(childFragmentManager, "datapicker")
                viewModel.chosenDate()
            }
        })

        // Observe the titleEmpty LiveData
        viewModel.emptyTitle.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                Snackbar.make(
                    binding.constraintLayout,
                    "Erro: Não é possível criar uma tarefa sem título.",
                    Snackbar.LENGTH_LONG).show()
                viewModel.emptyTitleMessageShowed()
            }
        })

        viewModel.resetDateTask()

        return binding.root
    }
    /*
    private fun View.hideKeyboard(){
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
    
     */

}