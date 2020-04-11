package com.example.tuskmanager.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tuskmanager.AllCategoriesFragment
import com.example.tuskmanager.R
import com.example.tuskmanager.SharedViewModel
import com.example.tuskmanager.data.domain.model.CategoryDomainModel
import java.lang.ref.WeakReference

class AddCategoryAdapter(
    val listener: AllCategoriesFragment.OnChooseCategoryClickListener
) : RecyclerView.Adapter<AddCategoryAdapter.AddCategoryViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddCategoryViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.category_element_new, parent, false)
        return AddCategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddCategoryViewHolder, position: Int) {
        holder.bind(SharedViewModel.CATEGORY_ADD_NEW)
    }

    override fun getItemCount(): Int {
        return 1
    }

    inner class AddCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var item: WeakReference<CategoryDomainModel>

        fun bind(item: CategoryDomainModel) {
            this.item = WeakReference(item)

            itemView.setOnClickListener { listener.onAddClick() }
        }
    }
}