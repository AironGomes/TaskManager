package com.airongomes.listadetarefas.newTask

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.airongomes.listadetarefas.R
import com.airongomes.listadetarefas.database.TaskListDatabase
import com.airongomes.listadetarefas.databinding.FragmentNewTaskBinding
import com.google.android.material.snackbar.Snackbar

class NewTaskFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentNewTaskBinding

    private lateinit var viewModel: NewTaskViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_new_task, container,false)

        val application = requireNotNull(this.activity).application
        val dataSource = TaskListDatabase.getInstance(application).taskDatabaseDao
        // Instantiate the ViewModel
        val viewModelFactory = NewTaskViewModelFactory(dataSource)
        //val viewModel: NewTaskViewModel by activityViewModels{viewModelFactory}
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(NewTaskViewModel::class.java)

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

        viewModel.resetTask()

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

}

