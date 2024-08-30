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

data class ArtistItem(val name: String, val imageBit : Bitmap)
class FragmentArtistAdapter(
    private val artistsList : ArrayList<ArtistItem>,
    private val onItemClickListener: OnItemClickListener? = null
) : Adapter<FragmentArtistAdapter.ViewHolder>() {
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val artistName : TextView = itemView.findViewById(R.id.playlistTitle)
        val artistImage : ImageButton = itemView.findViewById(R.id.playlistImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.your_library_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return artistsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = artistsList[position]
        holder.artistName.text = currentItem.name
        holder.artistImage.setImageBitmap(currentItem.imageBit)

        //Implement Listener
        holder.itemView.setOnClickListener {
            onItemClickListener?.setOnItemClickListener(currentItem)
        }
    }

    interface OnItemClickListener {
        fun setOnItemClickListener(item: ArtistItem?)
    }
}