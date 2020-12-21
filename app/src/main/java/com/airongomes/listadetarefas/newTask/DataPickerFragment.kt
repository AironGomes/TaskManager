package com.airongomes.listadetarefas.newTask

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.airongomes.listadetarefas.database.TaskListDatabase
import com.airongomes.listadetarefas.newTask.NewTaskFragmentDirections.actionNewTaskFragmentToOverviewFragment
import java.text.DateFormat
import java.util.*

class DataPickerFragment: DialogFragment(), DatePickerDialog.OnDateSetListener {

    /**
     * Send the date to NewTaskViewModel
     */
    override fun onDateSet(dataPicker: DatePicker?, year: Int, month: Int, day: Int) {
        val application = requireNotNull(this.activity).application
        val dataSource = TaskListDatabase.getInstance(application).taskDatabaseDao
        // Instantiate the ViewModel
        val viewModelFactory = NewTaskViewModelFactory(dataSource, application)
        val viewModel: NewTaskViewModel by activityViewModels{viewModelFactory}

        // Create a calendar with the input parameters
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        // Call the getDate function from viewModel
        viewModel.getDate(calendar)
    }

    /**
     * Receive the Calendar's Information
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(requireActivity(), this, year, month, day)
    }

}