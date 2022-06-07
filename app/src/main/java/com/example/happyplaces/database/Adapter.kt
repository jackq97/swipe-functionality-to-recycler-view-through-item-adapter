package com.example.happyplaces.database

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplaces.databinding.ListMainRecyclerViewBinding

class Adapter(
    private val items: ArrayList<PlaceEntity>,
    private val listener: OnItemClickListener
): RecyclerView.Adapter<Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListMainRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items[position]
        holder.tvTitle.text = item.title
        holder.tvDescription.text = item.description
        if (item.image.isEmpty()) {
            // TODO: set default image
        } else {
            holder.ivLocation.setImageURI(item.image.toUri())
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(binding: ListMainRecyclerViewBinding): RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {

        val ivLocation = binding.ivListLocation
        val tvTitle = binding.tvTitleList
        val tvDescription = binding.tvDescriptionList

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            val item = items[position]
            val id = item.id
            // good practice
            // Meaning we only be able to click if there is position
            // no position meaning we won't be able to click
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position, id)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, id: Int)
    }

    fun getId(index: Int): Int {

        val item = items[index]
        return item.id
    }

}

