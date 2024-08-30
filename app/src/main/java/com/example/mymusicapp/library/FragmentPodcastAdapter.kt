package com.example.mymusicapp.library

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.mymusicapp.R

data class PodcastItem(val podcastName : String, val podcastImage : Bitmap)
class FragmentPodcastAdapter(
    private val podcastList : ArrayList<PodcastItem>,
    private val onItemClickListener: OnItemClickListener? = null
) : Adapter<FragmentPodcastAdapter.ViewHolder>() {
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val itemText : TextView = itemView.findViewById(R.id.playlistTitle)
        val itemImage : ImageButton = itemView.findViewById(R.id.playlistImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.your_library_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return podcastList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = podcastList[position]
        holder.itemText.text = currentItem.podcastName
        holder.itemImage.setImageBitmap(currentItem.podcastImage)

        //Implement Listener
        holder.itemView.setOnClickListener {
            onItemClickListener?.setOnItemClickListener(currentItem)
        }
    }

    interface OnItemClickListener {
        fun setOnItemClickListener(item: PodcastItem)
    }
}