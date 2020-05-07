package com.example.tuskmanager

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tuskmanager.data.domain.model.CategoryDomainModel
import com.example.tuskmanager.ui.adapters.AllCategoriesAdapter
import kotlinx.android.synthetic.main.fragment_category_list.*

class AllCategoriesFragment : Fragment() {
    private val newTaskViewModel by lazy {
        ViewModelProvider(requireActivity()).get(NewTaskViewModel::class.java)
    }

    private val listener: OnChooseCategoryClickListener = object :
        OnChooseCategoryClickListener {
        override fun onItemClick(newlySelected: CategoryDomainModel) {
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                "newlySelectedCategory",
                newlySelected
            )
            findNavController().navigateUp()
        }
    }

    private var root: View? = null

    private val allCategoriesAdapter = AllCategoriesAdapter(listener)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return root
            ?: run {
                root = inflater.inflate(R.layout.fragment_category_list, container, false)
                root
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initLiveData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.actionbar_menu, menu)
        menu.findItem(R.id.action_sort).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().popBackStack()
                true
            }
            R.id.action_add -> {
                findNavController().navigate(R.id.action_allCategoriesFragment_to_newCategoryFragment)
                true
            }
            else -> false
        }
    }

    private fun initViews() {
        rv_categories_list.adapter = allCategoriesAdapter
    }

    private fun initLiveData() {
        newTaskViewModel.allCategories.observe(viewLifecycleOwner, Observer {
            allCategoriesAdapter.setItems(it)
        })
    }

    interface OnChooseCategoryClickListener {
        fun onItemClick(newlySelected: CategoryDomainModel)
    }
}


