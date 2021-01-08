package com.airongomes.taskmanager.detail

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.airongomes.taskmanager.R
import com.airongomes.taskmanager.database.TaskListDatabase
import com.airongomes.taskmanager.databinding.FragmentDetailBinding
import com.airongomes.taskmanager.overview.OverviewFragmentDirections


/**
 * Show the Task's details and allows edit it
 */
class DetailFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel
    private lateinit var binding: FragmentDetailBinding
    private lateinit var arguments: DetailFragmentArgs


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_detail, container, false
        )

        val application = requireNotNull(this.activity).application
        val dataSource = TaskListDatabase.getInstance(application).taskDatabaseDao
        arguments = DetailFragmentArgs.fromBundle(requireArguments())

        val viewModelFactory = DetailViewModelFactory(dataSource, arguments.taskId)

        viewModel = ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)

        binding.viewModel = viewModel

        // Set LifeCyclerOwer that able the fragment to observe LiveData
        binding.setLifecycleOwner(this)

        binding.viewDescription.movementMethod = ScrollingMovementMethod()


        viewModel.closeDetail.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    DetailFragmentDirections.actionDetailFragmentToOverviewFragment()
                )
                viewModel.detailFragmentClosed()
            }
        })

        // Call EditTask Fragment and send the Task Id
        viewModel.editTask.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    DetailFragmentDirections.actionDetailFragmentToEditTaskFragment(arguments.taskId))
                viewModel.editTaskCalled()
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
        inflater.inflate(R.menu.detail_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * Called when the items from OptionsMenu is pressed
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // Navigate to EditTask Fragment
            R.id.editTask -> {
                this.findNavController().navigate(
                DetailFragmentDirections.actionDetailFragmentToEditTaskFragment(arguments.taskId))
                viewModel.editTaskCalled()
            }
            else -> this.findNavController().navigate(
                DetailFragmentDirections.actionDetailFragmentToOverviewFragment()
            )
        }
        return true
    }
}