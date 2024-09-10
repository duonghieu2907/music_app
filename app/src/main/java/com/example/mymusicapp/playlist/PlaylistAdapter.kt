package com.example.mymusicapp.playlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymusicapp.R
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.models.Playlist


class PlaylistAdapter(private val playlists: List<Playlist>, private val dbHelper: MusicAppDatabaseHelper // Pass dbHelper as a parameter
                        ) :
    RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    var onPlaylistClick: ((Playlist) -> Unit)? = null

    class PlaylistViewHolder(itemView: View, private val dbHelper: MusicAppDatabaseHelper) : RecyclerView.ViewHolder(itemView) {
        private val icon: ImageView = itemView.findViewById(R.id.icon)
        private val playlistName: TextView = itemView.findViewById(R.id.playlistName)
        private val songCount: TextView = itemView.findViewById(R.id.songCount)

        // Binds data to the views
        fun bind(playlist: Playlist) {
            Glide.with(itemView.context)
                .load(playlist.image)
                .placeholder(R.drawable.blacker_gradient) // Placeholder while loading
                .error(R.drawable.blacker_gradient) // Error image if URL fails
                .into(icon)

            playlistName.text = playlist.name
            // Fetch the song count from the database and display it
            val trackCount = dbHelper.getTracksByPlaylistId(playlist.playlistId).size
            songCount.text = trackCount.toString()
        }
    }

    // Creates new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_playlist, parent, false)
        return PlaylistViewHolder(view, dbHelper)
    }

    // Replaces the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)


        // Set click listener for each playlist item
        holder.itemView.setOnClickListener {
            onPlaylistClick?.invoke(playlist)  // Notify that a playlist was clicked
        }
    }

    // Returns the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int = playlists.size
}
