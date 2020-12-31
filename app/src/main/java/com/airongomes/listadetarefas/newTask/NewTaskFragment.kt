package com.airongomes.listadetarefas.newTask

import android.app.Application
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.airongomes.listadetarefas.R
import com.airongomes.listadetarefas.database.TaskListDatabase
import com.airongomes.listadetarefas.databinding.FragmentNewTaskBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_new_task.*
import kotlinx.android.synthetic.main.fragment_overview.*
import java.text.SimpleDateFormat
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
        //val viewModel: NewTaskViewModel by activityViewModels{viewModelFactory}
        //viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(NewTaskViewModel::class.java)
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

//        // Observe the chooseDate LiveData
//        viewModel.chooseDate.observe(viewLifecycleOwner, Observer {
//            if (it == true) {
//                //val dialogFragment: DialogFragment = DataPickerFragment()
//                val dialogFragment = DataPickerFragment()
//                dialogFragment.show(childFragmentManager, "datapicker")
//                viewModel.chosenDate()
//
//            }
//        })

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
        spinner.prompt = "Selecione a prioridade da tarefa"
        spinner.gravity = Gravity.CENTER
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.priority_list,
            R.layout.custom_layout_text
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.custom_layout_text)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        //viewModel.resetTask()

        binding.buttonSetDate.setOnClickListener{
            showDatePicker()
        }

        binding.buttonSetTime.setOnClickListener{
            showTimePicker()
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


    fun showDatePicker() {

        //binding.editTest.setText(SimpleDateFormat("dd/MM/yyyy").format(System.currentTimeMillis()))
        //binding.editTest.editText?.setText(SimpleDateFormat("dd/MM/yyyy").format(System.currentTimeMillis()))

        val cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

//            val myFormat = "dd/MM/yyyy"
//            val sdf = SimpleDateFormat(myFormat, Locale.getDefault()) //Locale.US
//            binding.buttonSetDate.text = sdf.format(cal.time)
            viewModel.getDate(cal)
        }

        val dialog = DatePickerDialog(requireContext(), dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH))
        //dialog.datePicker.maxDate = Calendar.getInstance().timeInMillis
        dialog.show()
        //viewModel.getDate(cal)
    }

    fun showTimePicker() {

        val cal = Calendar.getInstance()

        val timeSetListener = TimePickerDialog.OnTimeSetListener {view, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            viewModel.getTime(cal)
        }

        val dialog = TimePickerDialog(requireContext(), timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE), true)
        dialog.show()
    }

}

