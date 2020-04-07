package com.example.tuskmanager.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tuskmanager.AllCategoriesFragment
import com.example.tuskmanager.R
import com.example.tuskmanager.data.domain.model.CategoryDomainModel
import java.lang.ref.WeakReference

class AddCategoryAdapter(
    val listener: AllCategoriesFragment.OnChooseCategoryClickListener
) : RecyclerView.Adapter<AddCategoryAdapter.AddCategoryViewHolder>() {

    private val addCategory = mutableListOf<CategoryDomainModel>()

    fun setItems(item: CategoryDomainModel) {
        addCategory.add(item)
        notifyDataSetChanged()
    }

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
        val item = addCategory[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return addCategory.size
    }

    inner class AddCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var item: WeakReference<CategoryDomainModel>

        fun bind(item: CategoryDomainModel) {
            this.item = WeakReference(item)

            itemView.setOnClickListener { listener.onItemClick(item) }
        }
    }
}