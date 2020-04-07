package com.example.tuskmanager.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tuskmanager.AllCategoriesFragment
import com.example.tuskmanager.R
import com.example.tuskmanager.data.domain.model.CategoryDomainModel
import kotlinx.android.synthetic.main.category_element.view.*
import java.lang.ref.WeakReference

class AllCategoriesAdapter(
    val listener: AllCategoriesFragment.OnChooseCategoryClickListener
) : RecyclerView.Adapter<AllCategoriesAdapter.DefaultCategoriesViewHolder>() {

    private val defaultCategories = mutableListOf<CategoryDomainModel>()

    fun setItems(newItems: List<CategoryDomainModel>) {
        defaultCategories.clear()
        defaultCategories.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DefaultCategoriesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.category_element, parent, false)
        return DefaultCategoriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: DefaultCategoriesViewHolder, position: Int) {
        val item = defaultCategories[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return defaultCategories.size
    }

    inner class DefaultCategoriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var item: WeakReference<CategoryDomainModel>
        private val textViewTitle = itemView.tv_title
        private val imageViewIcon = itemView.iv_task_icon

        fun bind(item: CategoryDomainModel) {
            this.item = WeakReference(item)
            val id = itemView.resources.getIdentifier(item.icon, "drawable", "com.example.tuskmanager")

            textViewTitle.text = item.title
            textViewTitle.setTextColor(Color.parseColor(item.color))
            imageViewIcon.setImageResource(id)
            imageViewIcon.setColorFilter(Color.parseColor(item.color))

            itemView.setOnClickListener { listener.onItemClick(item) }
        }
    }
}