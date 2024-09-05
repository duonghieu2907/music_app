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
import com.example.mymusicapp.models.Playlist

class PlaylistListAdapter(
    private val playlistListItem : ArrayList<Playlist>,
    private val setItemOnClickListener: FragmentPlaylistItemOnClickListener? = null,
    private var db: MusicAppDatabaseHelper? = null
) : Adapter<PlaylistListAdapter.ViewHolder>() {
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val titleText : TextView = itemView.findViewById(R.id.playlistTitle)
        val subText : TextView = itemView.findViewById(R.id.subText)
        val titleImage : ImageButton = itemView.findViewById(R.id.playlistImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.your_library_item, parent, false)
        db = MusicAppDatabaseHelper(context = parent.context)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playlistListItem.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = playlistListItem[position]
        val artistName = db?.getUser(currentItem.userId)?.name

        if(currentItem.name == "") {
            holder.titleText.text = "Unknown"
        } else holder.titleText.text = currentItem.name

        if(artistName == null) {
            holder.subText.text = "Unknown"
        } else holder.subText.text = artistName

        Glide.with(holder.titleImage.context)
            .load(currentItem.image)
            .placeholder(R.drawable.blacker_gradient) // Placeholder while loading
            .error(R.drawable.blacker_gradient) // Error image if URL fails
            .into(holder.titleImage)

        //Implement interface
        holder.itemView.setOnClickListener {
            setItemOnClickListener?.itemSelectionClickListener(currentItem)
        }

        holder.titleImage.setOnClickListener {
            setItemOnClickListener?.itemSelectionClickListener(currentItem)
        }
    }

    interface FragmentPlaylistItemOnClickListener {
        fun itemSelectionClickListener(item: Playlist?)
    }
}