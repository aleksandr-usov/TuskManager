package com.example.tuskmanager.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.tuskmanager.AllTasksViewModel
import com.example.tuskmanager.R
import com.example.tuskmanager.ui.adapters.AllTasksAdapter

class TaskItemTouchHelperCallback constructor(
    context: Context,
    private val allTasksViewModel: AllTasksViewModel
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)) {

    private val delayIcon = ContextCompat.getDrawable(context, R.drawable.ic_update)!!
    private val completeIcon = ContextCompat.getDrawable(context, R.drawable.ic_check_big)!!
    private val background = ColorDrawable()
    private val backgroundColorLeft = Color.parseColor("#27AE60")
    private val backgroundColorRight = Color.parseColor("#E28839")

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        ItemTouchHelper.Callback.getDefaultUIUtil()
        if (viewHolder !is AllTasksAdapter.TaskViewHolder) return
        val task = viewHolder.item.get()
        allTasksViewModel.getSwipedTask(task)
        when (direction) {
            ItemTouchHelper.LEFT -> {
                allTasksViewModel.delayOnSwipe()
            }
            ItemTouchHelper.RIGHT -> {
                allTasksViewModel.completeOnSwipe()
            }
        }
    }

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val width: Int
        val height: Int
        val iconMargin: Int

        if (dX < 0) {
            with(background) {
                color = backgroundColorRight
                setBounds(
                    itemView.right + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )

                width = bounds.width()
                height = bounds.height()

                iconMargin = (bounds.height() - delayIcon.intrinsicHeight) / 2
            }

            if (width > 0 && height > 0) {
                val resultBitmap = background.toBitmap(width, height)
                val canvasBackground = Canvas(resultBitmap)
                val resultTop = (resultBitmap.height - delayIcon.intrinsicHeight) / 2
                val resultRight = resultBitmap.width - iconMargin
                val resultLeft = resultRight - delayIcon.intrinsicWidth
                val resultBottom = resultTop + delayIcon.intrinsicHeight

                with(delayIcon) {
                    setBounds(
                        resultLeft,
                        resultTop,
                        resultRight,
                        resultBottom
                    )
                    draw(canvasBackground)
                }

                canvas.drawBitmap(
                    resultBitmap,
                    null,
                    RectF(
                        background.bounds.left.toFloat(),
                        itemView.top.toFloat(),
                        itemView.right.toFloat(),
                        itemView.bottom.toFloat()
                    ),
                    null
                )
            }
        } else if (dX > 0) {
            with(background) {
                color = backgroundColorLeft
                setBounds(
                    itemView.left,
                    itemView.top,
                    itemView.left + dX.toInt(),
                    itemView.bottom
                )

                width = bounds.width()
                height = bounds.height()

                iconMargin = (bounds.height() - completeIcon.intrinsicHeight) / 2
            }

            if (width > 0 && height > 0) {
                val resultBitmap = background.toBitmap(width, height)
                val canvasBackground = Canvas(resultBitmap)
                val resultTop = (resultBitmap.height - completeIcon.intrinsicHeight) / 2
                val resultRight = completeIcon.intrinsicWidth + iconMargin
                val resultBottom = resultTop + completeIcon.intrinsicHeight

                with(completeIcon) {
                    setBounds(
                        iconMargin,
                        resultTop,
                        resultRight,
                        resultBottom
                    )
                    draw(canvasBackground)
                }

                canvas.drawBitmap(
                    resultBitmap,
                    null,
                    RectF(
                        itemView.left.toFloat(),
                        itemView.top.toFloat(),
                        background.bounds.right.toFloat(),
                        itemView.bottom.toFloat()
                    ),
                    null
                )
            }
        }
        super.onChildDraw(
            canvas,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
    }
}