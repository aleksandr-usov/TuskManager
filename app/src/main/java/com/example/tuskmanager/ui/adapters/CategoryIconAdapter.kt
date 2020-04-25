package com.example.tuskmanager.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tuskmanager.AllCategoriesFragment
import com.example.tuskmanager.NewCategoryFragment
import com.example.tuskmanager.R
import com.example.tuskmanager.data.domain.model.CategoryDomainModel
import kotlinx.android.synthetic.main.category_element.view.*
import kotlinx.android.synthetic.main.category_icon_element.view.*
import java.lang.ref.WeakReference

class CategoryIconAdapter(
    val listener: NewCategoryFragment.OnChooseCategoryIconClickListener
) : RecyclerView.Adapter<CategoryIconAdapter.ChooseCategoryIconViewHolder>() {

    private var checkedPosition = -1
    private var currentColor = Color.parseColor("#179AB7")

    fun setColor(color: String) {
        val newColor =  Color.parseColor(color)
        if (currentColor == newColor) return
        else currentColor = newColor

                notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChooseCategoryIconViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.category_icon_element, parent, false)
        return ChooseCategoryIconViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChooseCategoryIconViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return 3
    }

    inner class ChooseCategoryIconViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            if (checkedPosition != -1) {
                if (checkedPosition == position) {
                    itemView.v_category_choose_icon.setBackgroundColor(Color.parseColor("#179AB7"))
                    itemView.v_category_choose_icon.background.alpha = 50
                } else {
                    itemView.v_category_choose_icon.setBackgroundColor(Color.WHITE)
                }
            } else {
                itemView.v_category_choose_icon.setBackgroundColor(Color.WHITE)
            }
            itemView.setOnClickListener {
                listener.onItemClick(CATEGORY_ICONS[position])
                itemView.v_category_choose_icon.setBackgroundColor(Color.parseColor("#179AB7"))
                itemView.v_category_choose_icon.background.alpha = 50
                if (checkedPosition != position) {
                    notifyItemChanged(checkedPosition)
                    checkedPosition = position
                }
            }
            val icon = itemView.resources.getIdentifier(
                CATEGORY_ICONS[position],
                "drawable",
                "com.example.tuskmanager"
            )
            itemView.iv_category_choose_icon.setImageResource(icon)
            itemView.iv_category_choose_icon.setColorFilter(currentColor)
        }
    }

    companion object{
        private val CATEGORY_ICONS = listOf(
            "ic_suit_case",
            "ic_house",
            "ic_clock"
        )
    }
}