package com.airongomes.listadetarefas.detail

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.airongomes.listadetarefas.R
import com.airongomes.listadetarefas.SetCalendar
import com.airongomes.listadetarefas.database.TaskListDatabase
import com.airongomes.listadetarefas.databinding.FragmentDetailBinding
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
                binding.buttonSetTime.isEnabled = true
                binding.buttonSetTime.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondaryColor))
                binding.editableIcon.visibility = View.GONE
                binding.saveIcon.visibility = View.VISIBLE
                binding.buttonCancel.text = getString(R.string.button_cancel)
                binding.buttonCancel.contentDescription = getString(R.string.contDesc_button_cancel)
                binding.checkboxTodoODia.isEnabled = true
                viewModel.editTaskConfirmed()
            }
        })

        viewModel.saved.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                binding.editTitle.isEnabled = false
                binding.editDescription.isEnabled = false
                binding.buttonSetDate.isEnabled = false
                binding.buttonSetTime.isEnabled = false
                binding.buttonSetTime.setTextColor(ContextCompat.getColor(requireContext(), R.color.material_on_background_disabled))
                binding.editableIcon.visibility = View.VISIBLE
                binding.saveIcon.visibility = View.GONE
                binding.buttonCancel.text = getString(R.string.button_ok)
                binding.buttonCancel.contentDescription = getString(R.string.contDesc_buttonOk)
                binding.checkboxTodoODia.isEnabled = false
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


        // Initiate calendar when the task LiveData is initiated
        viewModel.task.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.startCalendar(it)
            }
        })

        // Call showDatePicker function
        binding.buttonSetDate.setOnClickListener{
            showDatePicker()
        }

        // Call showTimePicker function
        binding.buttonSetTime.setOnClickListener{
            showTimePicker()
        }

        // Listener for checkbox state
        binding.checkboxTodoODia.setOnCheckedChangeListener{_, isChecked ->
            if(isChecked) {
                binding.buttonSetTime.isEnabled = false
                binding.buttonSetTime.setTextColor(ContextCompat.getColor(requireContext(), R.color.material_on_background_disabled))
                viewModel.setAllDayToTrue()
            }
            else {
                binding.buttonSetTime.isEnabled = true
                binding.buttonSetTime.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondaryColor))
            }
        }
        return binding.root
    }

    /**
     * DatePicker Dialog
     */
    private fun showDatePicker() {
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            cal.set(Calendar.HOUR_OF_DAY, 23)
            cal.set(Calendar.MINUTE, 59)
            cal.set(Calendar.SECOND, 59)
            viewModel.getDate(cal)
        }
        SetCalendar().datePickerDialog(requireContext(), cal, dateSetListener)
    }

    /**
     * TimePicker Dialog
     */
    fun showTimePicker() {

        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener {view, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            viewModel.getTime(cal)
        }
        SetCalendar().timePickerDialog(requireContext(), cal, timeSetListener)
    }
}