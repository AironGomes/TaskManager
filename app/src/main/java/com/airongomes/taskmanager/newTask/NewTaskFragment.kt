package com.airongomes.taskmanager.newTask

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.airongomes.taskmanager.R
import com.airongomes.taskmanager.SetCalendar
import com.airongomes.taskmanager.database.TaskListDatabase
import com.airongomes.taskmanager.databinding.FragmentNewTaskBinding
import com.airongomes.taskmanager.spinnerAdapter.PriorityModel
import com.airongomes.taskmanager.spinnerAdapter.SpinnerPriorityAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_new_task.*
import kotlinx.android.synthetic.main.fragment_overview.*
import java.util.*


class NewTaskFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentNewTaskBinding

    private lateinit var viewModel: NewTaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_new_task, container, false
        )

        val application = requireNotNull(this.activity).application
        val dataSource = TaskListDatabase.getInstance(application).taskDatabaseDao
        // Instantiate the ViewModel
        val viewModelFactory = NewTaskViewModelFactory(dataSource)
        viewModel = ViewModelProvider(this, viewModelFactory).get(NewTaskViewModel::class.java)

        // Associate the dataBinding with the viewModel
        binding.viewModel = viewModel
        // Set LifeCyclerOwer that able the fragment to observe LiveData
        binding.setLifecycleOwner(this)

        // Observe the closeFragment LiveData
        viewModel.closeFragment.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    NewTaskFragmentDirections.actionNewTaskFragmentToOverviewFragment()
                )
                viewModel.fragmentClosed()
            }
        })

        // Observe the saved LiveData
        viewModel.saved.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                Snackbar.make(
                    binding.constraintLayout,
                    "Tarefa salva com sucesso.",
                    Snackbar.LENGTH_LONG
                ).show()
                viewModel.savedMessageShowed()
                viewModel.cancelTask()
            }
        })

        // Observe the titleEmpty LiveData
        viewModel.emptyTitle.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                Snackbar.make(
                    binding.constraintLayout,
                    "Erro: Não é possível criar uma tarefa sem título.",
                    Snackbar.LENGTH_LONG
                ).show()
                viewModel.emptyTitleMessageShowed()
            }
        })

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
     * Use this Actions when the Spinner is selected
     */
    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        Log.i("Log", "onItemSelected called: $pos")
        // retrieve the selected item
        //val item = parent.getItemAtPosition(pos).toString()
        viewModel.setPriority(pos)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        Log.i("Log", "onNothingSelected called")
    }

    /**
     * Open Dialog DatePicker
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
            binding.checkboxTodoODia.isEnabled = true
        }
        SetCalendar().datePickerDialog(requireContext(), cal, dateSetListener)
    }

    /**
     * Open Dialog TimePicker
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

