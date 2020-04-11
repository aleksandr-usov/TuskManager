package com.example.tuskmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.MergeAdapter
import com.example.tuskmanager.data.domain.model.CategoryDomainModel
import com.example.tuskmanager.ui.adapters.AddCategoryAdapter
import com.example.tuskmanager.ui.adapters.AllCategoriesAdapter
import kotlinx.android.synthetic.main.fragment_category_list.*

class AllCategoriesFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    private val listener: OnChooseCategoryClickListener = object :
        OnChooseCategoryClickListener {
        override fun onItemClick(newlySelected: CategoryDomainModel) {
            viewModel.onCategoryClicked(newlySelected)
            findNavController().navigate(R.id.action_allCategoriesFragment_to_newTaskFragment)
        }

        override fun onAddClick() {
            viewModel.onAddCategoryClicked()
            findNavController().navigate(R.id.action_allCategoriesFragment_to_newCategoryFragment)
        }

    }

    private var root: View? = null

    private val allCategoriesAdapter = AllCategoriesAdapter(listener)
    private val addCategoryAdapter = AddCategoryAdapter(listener)
    private val mergeAdapter = MergeAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

    private fun initViews() {
        fab.hide()

        mergeAdapter.addAdapter(allCategoriesAdapter)
        mergeAdapter.addAdapter(addCategoryAdapter)

        rv_categories_list.adapter = mergeAdapter
    }

    private fun initLiveData() {
        viewModel.allCategories.observe(viewLifecycleOwner, Observer {
            allCategoriesAdapter.setItems(it)
        })
    }

    interface OnChooseCategoryClickListener {
        fun onItemClick(newlySelected: CategoryDomainModel)
        fun onAddClick()
    }
}


