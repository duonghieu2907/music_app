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
import com.example.mymusicapp.models.Track

class FragmentYourLibraryAdapter(
    private val playlistItem: ArrayList<Track>,
    private val onItemClickListener: OnItemClickListener? = null
) : Adapter<FragmentYourLibraryAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText : TextView = itemView.findViewById(R.id.playlistTitle)
        val subTitleText: TextView = itemView.findViewById(R.id.subText)
        val imagePlaylist : ImageButton = itemView.findViewById(R.id.playlistImage)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.your_library_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playlistItem.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = playlistItem[position]
        val db = MusicAppDatabaseHelper(holder.itemView.context)
        val album = db.getAlbum(currentItem.albumId)
        val artistName = db.getArtist(album!!.artistId)?.name?: "Unknown"

        holder.titleText.text = currentItem.name
        holder.subTitleText.text = artistName

        Glide.with(holder.itemView.context)
            .load(album.image)
            .placeholder(R.drawable.blacker_gradient)
            .error(R.drawable.blacker_gradient)
            .into(holder.imagePlaylist)

        //Implement Listener
        holder.itemView.setOnClickListener {
            onItemClickListener?.setOnItemClickListener(currentItem)
        }
    }

    interface OnItemClickListener {
        fun setOnItemClickListener(item: Track?)
    }
}