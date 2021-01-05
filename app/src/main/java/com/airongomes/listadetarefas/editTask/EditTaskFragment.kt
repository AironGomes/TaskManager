package com.airongomes.listadetarefas.editTask

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.airongomes.listadetarefas.R
import com.airongomes.listadetarefas.SetCalendar
import com.airongomes.listadetarefas.database.TaskListDatabase
import com.airongomes.listadetarefas.databinding.FragmentDetailBinding
import com.airongomes.listadetarefas.databinding.FragmentEditTaskBinding
import com.airongomes.listadetarefas.detail.DetailFragmentArgs
import com.airongomes.listadetarefas.detail.DetailFragmentDirections
import com.airongomes.listadetarefas.detail.DetailViewModel
import com.airongomes.listadetarefas.detail.DetailViewModelFactory
import com.airongomes.listadetarefas.spinnerAdapter.PriorityModel
import com.airongomes.listadetarefas.spinnerAdapter.SpinnerPriorityAdapter
import com.google.android.material.snackbar.Snackbar
import java.util.*

/**
 * Show the Task's details and allows edit it
 */
class EditTaskFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var viewModel: EditTaskViewModel
    private lateinit var binding: FragmentEditTaskBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_task, container, false
        )

        val application = requireNotNull(this.activity).application
        val dataSource = TaskListDatabase.getInstance(application).taskDatabaseDao
        val arguments = EditTaskFragmentArgs.fromBundle(requireArguments())

        val viewModelFactory = EditTaskViewModelFactory(dataSource, arguments.taskId)

        viewModel = ViewModelProvider(this, viewModelFactory).get(EditTaskViewModel::class.java)

        binding.viewModel = viewModel

        // Set LifeCyclerOwer that able the fragment to observe LiveData
        binding.lifecycleOwner = this

        val spinner: Spinner = binding.spinner
        spinner.onItemSelectedListener = this
        //spinner.prompt = "Selecione a prioridade da tarefa"
        //spinner.gravity = Gravity.CENTER
        // Spinner Adapter with custom Adapter
        spinner.adapter = SpinnerPriorityAdapter(requireContext(),
            listOf(
                PriorityModel(R.drawable.priority_icon_low, resources.getString(R.string.low_priority)),
                PriorityModel(R.drawable.priority_icon_medium, resources.getString(R.string.medium_priority)),
                PriorityModel(R.drawable.priority_icon_high, resources.getString(R.string.high_priority)),
            ))

        // Observe the titleEmpty LiveData
        viewModel.emptyTitle.observe(viewLifecycleOwner, {
            if (it == true) {
                Snackbar.make(
                    binding.editConstraintLayout,
                    "Erro: Não é possível criar uma tarefa sem título.",
                    Snackbar.LENGTH_LONG
                ).show()
                viewModel.emptyTitleMessageShowed()
            }
        })

        // Initiate calendar when the task LiveData is initiated
        viewModel.task.observe(viewLifecycleOwner, {
            it?.let {
                viewModel.startCalendar(it)
                spinner.setSelection(it.priority)
            }
        })

        // Observe closeEditTask LiveData
        viewModel.closeEditTask.observe(viewLifecycleOwner, {
            if (it == true) {
                this.findNavController().navigate(
                    EditTaskFragmentDirections.actionEditTaskFragmentToDetailFragment(arguments.taskId)
                )
                viewModel.editTaskClosed()
            }
        })

        // Observe saved LiveData
        viewModel.saved.observe(viewLifecycleOwner, {
            if (it == true) {
                Snackbar.make(
                    binding.editConstraintLayout,
                    "Tarefa salva com sucesso.",
                    Snackbar.LENGTH_LONG
                ).show()
                viewModel.savedMessageShowed()
                viewModel.closeEditTask()
            }
        })

        // Call showDatePicker function
        binding.editButtonSetDate.setOnClickListener{
            showDatePicker()
        }

        // Call showTimePicker function
        binding.editButtonSetTime.setOnClickListener{
            showTimePicker()
        }

        // Listener for checkbox state
        binding.checkboxTodoODia.setOnCheckedChangeListener{ _, isChecked ->
            if(isChecked) {
                binding.editButtonSetTime.isEnabled = false
                binding.editButtonSetTime.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.material_on_background_disabled
                    )
                )
                viewModel.setAllDayToTrue()
            }
            else {
                binding.editButtonSetTime.isEnabled = true
                binding.editButtonSetTime.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.secondaryColor
                    )
                )
            }
        }
        return binding.root
    }

    /**
     * Use this Actions when the Spinner is selected
     */
    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        Log.i("Log", "onItemSelected called")
        // retrieve the selected item
        //val item = parent.getItemAtPosition(pos).toString()
        viewModel.setPriority(pos)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        Log.i("Log", "onNothingSelected called")
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
    private fun showTimePicker() {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            viewModel.getTime(cal)
        }
        SetCalendar().timePickerDialog(requireContext(), cal, timeSetListener)
    }

}