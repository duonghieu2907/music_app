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

data class PlaylistListItem(val name : String, val imageBit : Bitmap)
class PlaylistListAdapter(
    private val playlistListItem : ArrayList<PlaylistListItem>,
    private val setItemOnClickListener: FragmentPlaylistItemOnClickListener? = null
) : Adapter<PlaylistListAdapter.ViewHolder>() {
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val titleText : TextView = itemView.findViewById(R.id.playlistTitle)
        val titleImage : ImageButton = itemView.findViewById(R.id.playlistImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.your_library_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playlistListItem.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = playlistListItem[position]
        holder.titleText.text = currentItem.name
        holder.titleImage.setImageBitmap(currentItem.imageBit)

        //Implement interface
        holder.itemView.setOnClickListener {
            setItemOnClickListener?.itemSelectionClickListener(currentItem)
        }

        holder.titleImage.setOnClickListener {
            setItemOnClickListener?.itemSelectionClickListener(currentItem)
        }
    }

    interface FragmentPlaylistItemOnClickListener {
        fun itemSelectionClickListener(item: PlaylistListItem?)
    }
}