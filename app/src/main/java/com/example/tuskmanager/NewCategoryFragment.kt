package com.example.tuskmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tuskmanager.ui.adapters.CategoryColorAdapter
import com.example.tuskmanager.ui.adapters.CategoryIconAdapter
import com.example.tuskmanager.ui.viewmodel.NewCategoryViewModelFactory
import kotlinx.android.synthetic.main.fragment_new_category.*
import javax.inject.Inject

class NewCategoryFragment : Fragment() {
    @Inject
    lateinit var newCategoryViewModelFactory: NewCategoryViewModelFactory

    private val newCategoryViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            newCategoryViewModelFactory
        ).get(NewCategoryViewModel::class.java)
    }

    private val listenerIcon: OnChooseCategoryIconClickListener = object :
        OnChooseCategoryIconClickListener {
        override fun onItemClick(newlySelected: String) {
            newCategoryViewModel.onIconClicked(newlySelected)
        }
    }

    private val listenerColor: OnChooseCategoryColorClickListener = object :
        OnChooseCategoryColorClickListener {
        override fun onItemClick(newlySelected: String) {
            newCategoryViewModel.onColorClicked(newlySelected)
        }
    }

    private val categoryIconAdapter = CategoryIconAdapter(listenerIcon)
    private val categoryColorAdapter = CategoryColorAdapter(listenerColor)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        TaskApplication.component.inject(this)
        return inflater.inflate(R.layout.fragment_new_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initLiveData()
    }

    private fun initViews() {
        rv_category.adapter = categoryIconAdapter
        rv_category_color.adapter = categoryColorAdapter

        et_category_name.addTextChangedListener(
            afterTextChanged = {
                newCategoryViewModel.newCategoryTitleEntered(it.toString())
            }
        )

        fab.setOnClickListener {
            newCategoryViewModel.addCategory()
        }
    }

    private fun initLiveData() {
        newCategoryViewModel.newCategory.observe(viewLifecycleOwner, Observer {
            it ?: return@Observer
            categoryIconAdapter.setColor(it.color)
        })

        newCategoryViewModel.error.observe(viewLifecycleOwner, Observer {
            it ?: return@Observer
            Toast.makeText(
                requireContext(), "Choose category title, icon and color!",
                Toast.LENGTH_SHORT
            )
                .show()
        })

        newCategoryViewModel.categoryCreated.observe(viewLifecycleOwner, Observer {
            it ?: return@Observer
            findNavController().navigate(R.id.action_newCategoryFragment_to_allCategoriesFragment)
        })
    }

    interface OnChooseCategoryIconClickListener {
        fun onItemClick(newlySelected: String)
    }

    interface OnChooseCategoryColorClickListener {
        fun onItemClick(newlySelected: String)
    }
}




