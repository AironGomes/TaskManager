package com.airongomes.listadetarefas.detail

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.airongomes.listadetarefas.R
import com.airongomes.listadetarefas.database.TaskListDatabase
import com.airongomes.listadetarefas.databinding.FragmentDetailBinding
import com.airongomes.listadetarefas.newTask.NewTaskFragmentDirections
import com.google.android.material.snackbar.Snackbar
import java.util.*

/**
 * Show the Task's details and allows edit it
 */
class DetailFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel

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

        viewModel = ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)

        binding.viewModel = viewModel

        viewModel.closeDetail.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                this.findNavController().navigate(
                    DetailFragmentDirections.actionDetailFragmentToOverviewFragment())
                viewModel.detailFragmentClosed()
            }
        })

        viewModel.editable.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                binding.editTitle.isEnabled = true
                binding.editDescription.isEnabled = true
                binding.buttonSetDate.isEnabled = true
                binding.editableIcon.visibility = View.GONE
                binding.saveIcon.visibility = View.VISIBLE
                viewModel.editTaskConfirmed()
            }
        })

        viewModel.saved.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                binding.editTitle.isEnabled = false
                binding.editDescription.isEnabled = false
                binding.buttonSetDate.isEnabled = false
                binding.editableIcon.visibility = View.VISIBLE
                binding.saveIcon.visibility = View.GONE
                viewModel.enableEditOptions()
            }
        })

        // Observe the titleEmpty LiveData
        viewModel.emptyTitle.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                Snackbar.make(
                    binding.editConstraintLayout,
                    "Erro: Não é possível criar uma tarefa sem título.",
                    Snackbar.LENGTH_LONG).show()
                viewModel.emptyTitleMessageShowed()
            }
        })
        
        // Set LifeCyclerOwer that able the fragment to observe LiveData
        binding.setLifecycleOwner(this)

        // Call showDatePicker function
        binding.buttonSetDate.setOnClickListener{
            showDatePicker()
        }

        // Initiate dateTask Calendar When the task LiveData is initiated
        viewModel.task.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.startDateCalendar(it)
            }
        })
        return binding.root
    }

    /**
     * Show the DatePicker when the Date button is clicked
     */
    fun showDatePicker() {
        val cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            viewModel.getDate(cal)
        }

        val dialog = DatePickerDialog(requireContext(), dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH))

        dialog.show()
    }

}