package com.airongomes.listadetarefas.overview

import OverviewViewMolderFactory
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.airongomes.listadetarefas.R
import com.airongomes.listadetarefas.adapter.TaskAdapter
import com.airongomes.listadetarefas.adapter.TaskClickListener
import com.airongomes.listadetarefas.database.TaskListDatabase
import com.airongomes.listadetarefas.databinding.FragmentOverviewBinding


class OverviewFragment : Fragment() {

    // Create an viewModel variable (Enable the viewModel be accessible from outside of onCreateView)
    private lateinit var viewModel: OverviewViewModel

    /**
     * onCreateView is responsible to inflate the fragment and instantiate the all the features which
     * needs to be started with it.
     * @param inflater
     * @param container
     * @param savedInstanceState
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentOverviewBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_overview, container, false
        )

        // Create a DataSource of the Database
        val application = requireNotNull(this.activity).application
        val dataSource = TaskListDatabase.getInstance(application).taskDatabaseDao
        // Create the ViewModel
        val viewModelFactory = OverviewViewMolderFactory(dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(OverviewViewModel::class.java)
        // Associate the ViewModel with the binding.viewModel variable
        binding.viewModel = viewModel

        // Set this fragment to LifecyclerOwner to enable binding observe LiveData
        binding.setLifecycleOwner(this)

        // Create an Adapter class of RecyclerView
        val adapter = TaskAdapter(TaskClickListener { taskId ->
            viewModel.onTaskClicked(taskId)
        })
        // Associate the RecyclerView with the Adapter
        binding.recyclerview.adapter = adapter
        // Create a LayoutManager of RecyclerView
        val layoutManager = LinearLayoutManager(activity)
        // Associate the RecycclerView with the LayoutManager
        binding.recyclerview.layoutManager = layoutManager
        // Create an ItemDecoration of RecyclerView (Divider)
        val mDividerItemDecoration = DividerItemDecoration(
            binding.recyclerview.context,
            layoutManager.orientation
        )
        // Associate the RecyclerView with the ItemDecoration
        binding.recyclerview.addItemDecoration(mDividerItemDecoration)

        // Check Change Listener from ChipGroup
        binding.chipGroup.setOnCheckedChangeListener{group, checkedId ->
            when(checkedId) {
                binding.chipLowPriority.id -> viewModel.updateFilterPriority("low")
                binding.chipMediumPriority.id -> viewModel.updateFilterPriority("medium")
                binding.chipHighPriority.id -> viewModel.updateFilterPriority("high")
                else -> viewModel.updateFilterPriority("all")
            }
        }

        // Navigate to NewTask Fragment
        viewModel.createNewTask.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    OverviewFragmentDirections.actionOverviewFragmentToNewTaskFragment()
                )
                viewModel.createNewTaskStarted()
            }
        })

        // Update the RecyclerView List
        viewModel.taskList.observe(viewLifecycleOwner, Observer {
            Log.i("TEST", "Submit List called")
            it?.let {
                adapter.submitList(it)
                binding.textWithoutWork.visibility = View.GONE
                binding.imageWithoutWork.visibility = View.GONE
            }
            // Show a image if the List is empty
            if(it.isNullOrEmpty()){
                binding.textWithoutWork.visibility = View.VISIBLE
                binding.imageWithoutWork.visibility = View.VISIBLE
            }
        })

        // Navigate to DetailFragment
        viewModel.navigateToTaskDetail.observe(viewLifecycleOwner, Observer { nightId ->
            nightId?.let {
                this.findNavController().navigate(
                    OverviewFragmentDirections.actionOverviewFragmentToDetailFragment(nightId)
                )
                viewModel.onTaskDetailNavigated()
            }
        })

        // Enable the use of Menu
        setHasOptionsMenu(true)
        return binding.root
    }

    /**
     * Inflate the OptionsMenu
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overview_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * Called when the items from OptionsMenu is pressed
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // Clear the database
            R.id.deleteAll -> viewModel.deleteAllFromDatabase()
            // Navigate to AboutFragment
            R.id.about -> { this.findNavController().navigate(
                OverviewFragmentDirections.actionOverviewFragmentToAboutFragment())
            }
        }
        return true
    }

}



