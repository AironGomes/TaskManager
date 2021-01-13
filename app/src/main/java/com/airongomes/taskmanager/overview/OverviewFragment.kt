package com.airongomes.taskmanager.overview

import OverviewViewMolderFactory
import android.app.ActionBar
import android.os.Bundle
import android.view.*
import android.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.airongomes.taskmanager.R
import com.airongomes.taskmanager.adapter.*
import com.airongomes.taskmanager.database.TaskListDatabase
import com.airongomes.taskmanager.databinding.FragmentOverviewBinding
import kotlinx.android.synthetic.main.fragment_overview.*


class OverviewFragment : Fragment() {

    // Create an viewModel variable (Enable the viewModel be accessible from outside of onCreateView)
    private lateinit var viewModel: OverviewViewModel
    private lateinit var binding: FragmentOverviewBinding
    private lateinit var tracker: SelectionTracker<Long>
    private var listOfKeys = mutableListOf<Long>()
    private var actionMode: ActionMode? = null

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
        binding = DataBindingUtil.inflate(
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

        // Allow select items in RecyclerView
        tracker = SelectionTracker.Builder(
            "mySelection",
            binding.recyclerview,
            //StableIdKeyProvider(binding.recyclerview),
            MyItemKeyProvider(binding.recyclerview),
            MyDetailsLookup(binding.recyclerview),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()

        adapter.tracker = tracker

        // Observe when item is selected
        tracker.addObserver(
            object : SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {
                    super.onSelectionChanged()
                    if (tracker.hasSelection()) {
                        binding.fabNewTask.visibility = View.GONE
                        listOfKeys = tracker.selection.toMutableList()
                        updateActionMode()

                    } else {
                        binding.fabNewTask.visibility = View.VISIBLE
                        listOfKeys.clear()
                        actionMode?.finish()
                    }
                }
            }
        )
        // Observe deletedTasks LiveData to use clear selection
        viewModel.deletedTasks.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                tracker.clearSelection()
                viewModel.selectionCleared()
            }

        })

        // Check Change Listener from ChipGroup
        binding.chipGroup.setOnCheckedChangeListener{ group, checkedId ->
            tracker.clearSelection()
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
            it?.let {
                adapter.submitList(it)
                binding.textWithoutWork.visibility = View.GONE
                binding.imageWithoutWork.visibility = View.GONE
            }
            // Show a image if the List is empty
            if (it.isNullOrEmpty()) {
                binding.textWithoutWork.visibility = View.VISIBLE
                binding.imageWithoutWork.visibility = View.VISIBLE
            }
        })

        // Navigate to DetailFragment
        viewModel.navigateToTaskDetail.observe(viewLifecycleOwner, Observer { taskId ->
            taskId?.let {
                this.findNavController().navigate(
                    OverviewFragmentDirections.actionOverviewFragmentToDetailFragment(taskId)
                )
                viewModel.onTaskDetailNavigated()
            }
        })

        // Enable the use of Menu
        setHasOptionsMenu(true)

        // Restores the tracker states
        if( savedInstanceState != null ){
            tracker.onRestoreInstanceState(savedInstanceState)
            if (tracker.hasSelection()) {
                binding.fabNewTask.visibility = View.GONE
                listOfKeys = tracker.selection.toMutableList()
                updateActionMode()
            }
        }
        return binding.root
    }

    /**
     * Save InstanceState of tracker selection
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(tracker.hasSelection()){
            tracker.onSaveInstanceState(outState)
        }
    }

    /**
     * Update Action Mode
     */
    fun updateActionMode(){
        actionMode?.invalidate()
        // Called when the user long-clicks on recyclerview item
        if(actionMode == null){
            actionMode = activity?.startActionMode(actionModeCallback)
        }
        actionMode?.title = "${tracker.selection.size()} ${getText(R.string.lb_selected)}"
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
            R.id.deleteAll -> {
                viewModel.deleteAllFromDatabase()
            }
            // Navigate to AboutFragment
            R.id.about -> {
                this.findNavController().navigate(
                    OverviewFragmentDirections.actionOverviewFragmentToAboutFragment()
                )
            }
        }
        return true
    }


    private val actionModeCallback = object : ActionMode.Callback {
        /**
         * Called when Action Mode is created
         */
        override fun onCreateActionMode(mode: ActionMode, menu: Menu?): Boolean {
            // Inflate a menu resource providing context menu items
            val inflater: MenuInflater = mode.menuInflater
            inflater.inflate(R.menu.contextual_menu, menu)
            return true
        }

        /**
         * Called when the action mode is show or invalidated
         */
        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            val itemInfo = menu.findItem(R.id.action_info)
            itemInfo.isVisible = listOfKeys.size == 1
            return false
        }

        /**
         * Called when an item is selected from Action Mode
         */
        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when(item.itemId){
                // Navigate to DetailFragment
                R.id.action_info -> {
                    this@OverviewFragment.findNavController().navigate(
                        OverviewFragmentDirections.actionOverviewFragmentToDetailFragment(listOfKeys[0]))
                    actionMode?.finish()
                    true
                }
                // Clear the selected tasks
                R.id.action_delete -> {
                    viewModel.deleteSelected(listOfKeys)
                    true
                }
                else -> false
            }
        }

        /**
         * Called when exists the Action Mode
         */
        override fun onDestroyActionMode(mode: ActionMode?) {
            actionMode = null
            tracker.clearSelection()
        }

    }
}



