package com.example.tuskmanager

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.tuskmanager.data.domain.model.TaskDomainModel
import com.example.tuskmanager.ui.TaskItemTouchHelperCallback
import com.example.tuskmanager.ui.adapters.AllTasksAdapter
import com.example.tuskmanager.ui.viewmodel.AllTasksViewModelFactory
import kotlinx.android.synthetic.main.fragment_tasks_list.*
import javax.inject.Inject

class AllTasksFragment : Fragment() {
    @Inject
    lateinit var allTasksViewModelFactory: AllTasksViewModelFactory

    private val allTasksViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            allTasksViewModelFactory
        ).get(AllTasksViewModel::class.java)
    }

    private val listener: OnChooseTaskClickListener = object :
        OnChooseTaskClickListener {
        override fun onItemClick(newlySelected: TaskDomainModel) {
            allTasksViewModel.onTaskClicked(newlySelected)
            findNavController().navigate(
                AllTasksFragmentDirections.actionAllTasksFragmentToNewTaskFragment(
                    newlySelected
                )
            )
        }
    }

    private val allTasksAdapter = AllTasksAdapter(listener)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        TaskApplication.component.inject(this)
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_tasks_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initLiveData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.actionbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sort -> allTasksViewModel.sort()
            R.id.action_add -> findNavController().navigate(
                AllTasksFragmentDirections.actionAllTasksFragmentToNewTaskFragment(
                    TaskDomainModel.TASK_ADD_NEW
                )
            )
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initViews() {
        rv_tasks_list.adapter = allTasksAdapter

        val taskItemTouchHelperCallback =
            TaskItemTouchHelperCallback(requireContext(), allTasksViewModel)

        val taskItemTouchHelper = ItemTouchHelper(taskItemTouchHelperCallback)

        taskItemTouchHelper.attachToRecyclerView(rv_tasks_list)

        val dividerItemDecoration = DividerItemDecoration(context, RecyclerView.VERTICAL)
        rv_tasks_list.addItemDecoration(dividerItemDecoration)

        et_search_tasks.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                allTasksViewModel.searchTasks(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        })
    }

    private fun initLiveData() {
        allTasksViewModel.tasksToDisplay.observe(viewLifecycleOwner, Observer { allTasks ->
            allTasksAdapter.setItems(allTasks)
            if (allTasks.isNotEmpty()) {
                group_empty_state.visibility = View.GONE
            } else {
                group_empty_state.visibility = View.VISIBLE
                allTasksViewModel.sortFlag.observe(viewLifecycleOwner, Observer { sortFlag ->
                    if (sortFlag == 1) {
                        tv_empty_task_list.text = getString(R.string.empty_task_list_completed)
                        tv_empty_task_list_explained.text =
                            getString(R.string.empty_task_list_explained_completed)
                    } else {
                        tv_empty_task_list.text = getString(R.string.empty_task_list)
                        tv_empty_task_list_explained.text =
                            getString(R.string.empty_task_list_explained)
                    }
                })
            }
        })
    }

    interface OnChooseTaskClickListener {
        fun onItemClick(newlySelected: TaskDomainModel)
    }
}


