package com.example.tuskmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.tuskmanager.data.domain.model.TaskDomainModel
import com.example.tuskmanager.ui.adapters.AllTasksAdapter
import kotlinx.android.synthetic.main.fragment_tasks_list.*

class AllTasksFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    private val listener: OnChooseTaskClickListener = object :
        OnChooseTaskClickListener {
        override fun onItemClick(newlySelected: TaskDomainModel) {
            viewModel.onTaskClicked(newlySelected)
        }
    }

    private val adapter = AllTasksAdapter(listener)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tasks_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dividerItemDecoration = DividerItemDecoration(context, RecyclerView.VERTICAL)
        rv_tasks_list.addItemDecoration(dividerItemDecoration)

        rv_tasks_list.adapter = adapter

        viewModel.allTasks.observe(viewLifecycleOwner, Observer {
            adapter.setItems(it)
            if (it.isNotEmpty()) {
                group_empty_state.visibility = View.GONE
            } else {
                group_empty_state.visibility = View.VISIBLE
            }
        })

       // val navController = Navigation.findNavController(view)

        fab.setOnClickListener {
           findNavController().navigate(R.id.action_allTasksFragment_to_newTaskFragment)
        }
    }

    interface OnChooseTaskClickListener {
        fun onItemClick(newlySelected: TaskDomainModel)
    }
}


