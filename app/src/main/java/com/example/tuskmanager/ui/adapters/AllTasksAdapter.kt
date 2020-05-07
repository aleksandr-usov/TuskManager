package com.example.tuskmanager.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tuskmanager.AllTasksFragment
import com.example.tuskmanager.R
import com.example.tuskmanager.data.domain.model.TaskDomainModel
import kotlinx.android.synthetic.main.task_element.view.*
import java.lang.ref.WeakReference

class AllTasksAdapter(
    val listener: AllTasksFragment.OnChooseTaskClickListener
) : RecyclerView.Adapter<AllTasksAdapter.TaskViewHolder>() {

    private val allTasks = mutableListOf<TaskDomainModel>()

    fun setItems(newItems: List<TaskDomainModel>) {
        allTasks.clear()
        allTasks.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.task_element, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = allTasks[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return allTasks.size
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var item: WeakReference<TaskDomainModel>
        private val textViewCategory = itemView.tv_task_element_category
        private val textViewTitle = itemView.tv_task_element_title
        private val textViewTime = itemView.tv_task_element_time
        private val textViewDescription = itemView.tv_task_element_description
        private val imageViewIcon = itemView.iv_task_element_icon

        fun bind(item: TaskDomainModel) {
            this.item = WeakReference(item)
            val icon = itemView.resources.getIdentifier(
                item.categoryIcon,
                "drawable",
                "com.example.tuskmanager"
            )
            if (item.isComplete) {
                itemView.iv_star.visibility = View.VISIBLE
                itemView.iv_star.alpha = .5F
                itemView.tv_task_element_time.text = "Completed!"
                itemView.tv_task_element_time.setTextColor(Color.parseColor("#10D53B"))
                itemView.tv_task_element_time.alpha = .5F
            } else {
                itemView.iv_star.visibility = View.INVISIBLE
                textViewTime.text = "${item.dateDue}, ${item.timeDue}"
                itemView.tv_task_element_time.setTextColor(Color.parseColor("#DE000000"))
                itemView.tv_task_element_time.alpha = .5F
            }
            textViewTitle.text = item.title
            textViewTitle.setTextColor(Color.parseColor(item.color))
            textViewCategory.text = item.category
            textViewCategory.setTextColor(Color.parseColor(item.color))
            textViewDescription.text = item.description
            imageViewIcon.setImageResource(icon)
            imageViewIcon.setColorFilter(Color.parseColor(item.color))

            itemView.setOnClickListener { listener.onItemClick(item) }
        }
    }
}