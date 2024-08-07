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

data class AlbumItem(val albumName : String, val albumImage : Bitmap)
class FragmentAlbumsAdapter(
    private val AlbumsList : ArrayList<AlbumItem>
) : Adapter<FragmentAlbumsAdapter.ViewHolder>() {
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val itemText : TextView = itemView.findViewById(R.id.playlistTitle)
        val itemImage : ImageButton = itemView.findViewById(R.id.playlistImage)
    }

    override fun getItemCount(): Int {
        return AlbumsList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.your_library_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = AlbumsList[position]
        holder.itemText.text = currentItem.albumName
        holder.itemImage.setImageBitmap(currentItem.albumImage)
    }
}