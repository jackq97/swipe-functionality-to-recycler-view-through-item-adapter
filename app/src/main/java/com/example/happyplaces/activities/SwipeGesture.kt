package com.example.happyplaces.activities



import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplaces.R
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


abstract class SwipeGesture(context: Context): ItemTouchHelper.SimpleCallback(0,
ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val deleteColor = ContextCompat.getColor(context, R.color.deletecolor)
    val editColor = ContextCompat.getColor(context, R.color.editcolor)
    private val deleteIcon = R.drawable.ic_baseline_delete_24
    val editIcon = R.drawable.ic_baseline_edit_24

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean,
    ) {

        RecyclerViewSwipeDecorator.Builder(c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive)
            .addSwipeRightBackgroundColor(deleteColor)
            .addSwipeRightActionIcon(deleteIcon)
            .addSwipeLeftBackgroundColor(editColor)
            .addSwipeLeftActionIcon(editIcon)
            .create()
            .decorate()

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}