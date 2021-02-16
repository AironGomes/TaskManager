package com.airongomes.taskmanager.editTask

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.airongomes.taskmanager.*
import com.airongomes.taskmanager.database.TaskListDatabase
import com.airongomes.taskmanager.databinding.FragmentEditTaskBinding
import com.airongomes.taskmanager.spinnerAdapter.PriorityModel
import com.airongomes.taskmanager.spinnerAdapter.SpinnerPriorityAdapter
import com.airongomes.taskmanager.util.datePickerDialog
import com.airongomes.taskmanager.util.hideKeyboard
import com.airongomes.taskmanager.util.timePickerDialog
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

        val spinner: Spinner = binding.spinnerEditTask
        spinner.onItemSelectedListener = this
        //spinner.prompt = "Selecione a prioridade da tarefa"
        //spinner.gravity = Gravity.CENTER
        // Spinner Adapter with custom Adapter
        spinner.adapter = SpinnerPriorityAdapter(requireContext(),
            listOf(
                PriorityModel(R.drawable.priority_icon_low, resources.getString(R.string.low_priority)),
                PriorityModel(R.drawable.priority_icon_medium, resources.getString(R.string.medium_priority)),
                PriorityModel(R.drawable.priority_icon_high, resources.getString(R.string.high_priority)))
        )

        // Observe the titleEmpty LiveData
        viewModel.emptyTitle.observe(viewLifecycleOwner, {
            if (it == true) {
                Snackbar.make(
                    binding.editConstraintLayout,
                    getString(R.string.error_withoutTitle),
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
                    getString(R.string.msg_taskSaved),
                    Snackbar.LENGTH_LONG
                ).show()
                viewModel.savedMessageShowed()
                viewModel.closeEditTask()
            }
        })

        // Call showDatePicker function
        binding.editButtonSetDate.setOnClickListener{
            hideKeyboard(it, requireContext())
            showDatePicker()
        }

        // Call showTimePicker function
        binding.editButtonSetTime.setOnClickListener{
            hideKeyboard(it, requireContext())
            showTimePicker()
        }

        // Listener for checkbox state
        binding.checkboxTodoODiaEditTask.setOnCheckedChangeListener{ view, isChecked ->
            hideKeyboard(view, requireContext())
            if(isChecked) {
                binding.editButtonSetTime.isEnabled = false
                binding.editButtonSetTime.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.item_disabled))
                viewModel.setAllDayToTrue()
            }
            else {
                binding.editButtonSetTime.isEnabled = true
                binding.editButtonSetTime.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.secondaryColor))
            }
        }

        // Focus change listener to the viewGroup
        binding.editConstraintLayout.setOnFocusChangeListener { view, hasFocus ->
            if(hasFocus){
                hideKeyboard(view, requireContext())
            }}

        // Enable the use of Menu
        setHasOptionsMenu(true)
        return binding.root
    }

    /**
     * Use this Actions when the Spinner is selected
     */
    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // retrieve the selected item
        //val item = parent.getItemAtPosition(pos).toString()
        viewModel.setPriority(pos)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        Log.i("Log", "onNothingSelected called")
    }

    /**
     * Use onOptionsItemSelected to call hideKeyboard
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        hideKeyboard(requireView(), requireContext())
        return super.onOptionsItemSelected(item)
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
            cal.set(Calendar.MILLISECOND, 999)
            viewModel.getDate(cal)
        }
        datePickerDialog(requireContext(), cal, dateSetListener)
    }

    /**
     * TimePicker Dialog
     */
    private fun showTimePicker() {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            viewModel.getTime(cal)
        }
        timePickerDialog(requireContext(), cal, timeSetListener)
    }
}