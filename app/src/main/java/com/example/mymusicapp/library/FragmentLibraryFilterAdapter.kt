package com.example.mymusicapp.library

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicapp.R

data class LibraryFilterItem(val name: String);
class FragmentLibraryFilterAdapter(private val filterItem: ArrayList<LibraryFilterItem>) :
    RecyclerView.Adapter<FragmentLibraryFilterAdapter.ViewHolder>() {

        private var selectedPosition = RecyclerView.NO_POSITION
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextViewItemFilter : TextView = itemView.findViewById(R.id.item_filter_text)
        var rectangleItemFilterBackground : ImageButton = itemView.findViewById(R.id.item_filter_background)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.library_filter_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = filterItem[position]
        holder.nameTextViewItemFilter.text = currentItem.name

        if(position == selectedPosition) {
            holder.rectangleItemFilterBackground.setBackgroundResource(R.drawable.rectangle_item_filter_color)
        }
        else {
            holder.rectangleItemFilterBackground.setBackgroundResource(R.drawable.rectangle_item_filter_nocolor)
        }

        holder.rectangleItemFilterBackground.setOnClickListener {
            if(selectedPosition != RecyclerView.NO_POSITION) {
                notifyItemChanged(selectedPosition)
            }

            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
        }

    }


    override fun getItemCount(): Int {
        return filterItem.size
    }
}