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
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.models.Album

class FragmentAlbumsAdapter(
    private val albumsList: ArrayList<Album>,
    private val onItemClickListener: OnItemClickListener? = null,
    private var db: MusicAppDatabaseHelper? = null
) : Adapter<FragmentAlbumsAdapter.ViewHolder>() {
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val itemText : TextView = itemView.findViewById(R.id.playlistTitle)
        val subText : TextView = itemView.findViewById(R.id.subText)
        val itemImage : ImageButton = itemView.findViewById(R.id.playlistImage)
    }

    override fun getItemCount(): Int {
        return albumsList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.your_library_item, parent, false)
        db = MusicAppDatabaseHelper(context = parent.context)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = albumsList[position]

        holder.subText.text = db?.getArtist(currentItem.artistId)?.name ?: "No artist"
        holder.itemText.text = currentItem.name

        Glide.with(holder.itemImage.context)
            .load(currentItem.image)
            .placeholder(R.drawable.blacker_gradient) // Placeholder while loading
            .error(R.drawable.blacker_gradient) // Error image if URL fails
            .into(holder.itemImage)

        //Implement Listener
        holder.itemView.setOnClickListener {
            onItemClickListener?.setOnItemClickListener(currentItem)
        }

    }

    interface OnItemClickListener {
        fun setOnItemClickListener(item: Album?)
    }

}