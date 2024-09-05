package com.example.mymusicapp.library

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.example.mymusicapp.R
import com.example.mymusicapp.models.Artist

class FragmentArtistAdapter(
    private val artistsList : ArrayList<Artist>,
    private val onItemClickListener: OnItemClickListener? = null
) : Adapter<FragmentArtistAdapter.ViewHolder>() {
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val artistName : TextView = itemView.findViewById(R.id.playlistTitle)
        val subTextGenre : TextView = itemView.findViewById(R.id.subText)
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
        holder.subTextGenre.text = currentItem.genre
        Glide.with(holder.artistImage.context)
            .load(currentItem.image)
            .placeholder(R.drawable.blacker_gradient)
            .error(R.drawable.blacker_gradient)
            .into(holder.artistImage)

        //Implement Listener
        holder.itemView.setOnClickListener {
            onItemClickListener?.setOnItemClickListener(currentItem)
        }
    }

    interface OnItemClickListener {
        fun setOnItemClickListener(item: Artist?)
    }
}