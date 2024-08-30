package com.example.mymusicapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class PlaylistItem(
    val iconResId: Int,   // Resource ID for the drawable icon
    val name: String,     // Name of the playlist
    val songCount: String // Number of songs in the playlist
)

class PlaylistAdapter(private val playlists: List<PlaylistItem>) :
    RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    // ViewHolder class that binds the data to the view
    class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val icon: ImageView = itemView.findViewById(R.id.icon)
        private val playlistName: TextView = itemView.findViewById(R.id.playlistName)
        private val songCount: TextView = itemView.findViewById(R.id.songCount)

        // Binds data to the views
        fun bind(playlist: PlaylistItem) {
            icon.setImageResource(playlist.iconResId)
            playlistName.text = playlist.name
            songCount.text = playlist.songCount
        }
    }

    // Creates new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_playlist, parent, false)
        return PlaylistViewHolder(view)
    }

    // Replaces the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)
    }

    // Returns the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int = playlists.size
}
