package com.example.tuskmanager.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.recyclerview.widget.RecyclerView
import com.example.tuskmanager.NewCategoryFragment
import com.example.tuskmanager.R
import kotlinx.android.synthetic.main.category_color_element.view.*
import kotlinx.android.synthetic.main.category_icon_element.view.*
import kotlinx.android.synthetic.main.fragment_new_category.*

class CategoryColorAdapter(
    val listener: NewCategoryFragment.OnChooseCategoryColorClickListener
) : RecyclerView.Adapter<CategoryColorAdapter.ChooseCategoryColorViewHolder>() {

    private var checkedPosition = -1

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChooseCategoryColorViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.category_color_element, parent, false)
        return ChooseCategoryColorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChooseCategoryColorViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return 6
    }

    inner class ChooseCategoryColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            if (checkedPosition != -1) {
                if (checkedPosition == position) {
                    itemView.iv_color_check.visibility = View.VISIBLE
                } else {
                    itemView.iv_color_check.visibility = View.INVISIBLE
                }
            } else {
                itemView.iv_color_check.visibility = View.INVISIBLE
            }
            itemView.setOnClickListener {
                listener.onItemClick(CATEGORY_COLORS[position])
                itemView.iv_color_check.visibility = View.VISIBLE
                if (checkedPosition != position) {
                    notifyItemChanged(checkedPosition)
                    checkedPosition = position
                }
            }
            itemView.iv_category_choose_color_element.setColorFilter(
                Color.parseColor(
                    CATEGORY_COLORS[position]
                )
            )
        }
    }

    companion object {
        private val CATEGORY_COLORS = listOf(
            "#B78A17",
            "#1744B7",
            "#549700",
            "#E28839",
            "#EB5757",
            "#9B51E0"
        )
    }
}