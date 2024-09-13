package com.example.mymusicapp.bottomnavigation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymusicapp.R
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.models.Album
import com.example.mymusicapp.models.Playlist

class RecentlyPlayedAdapter(
    private val items: List<Any>,
    private val onItemClickListener: ((Any) -> Unit)? = null, // Click listener
    private var db: MusicAppDatabaseHelper? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_ALBUM = 1
        const val VIEW_TYPE_PLAYLIST = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        db = MusicAppDatabaseHelper(context = parent.context)
        return when (viewType) {
            VIEW_TYPE_ALBUM -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)
                AlbumViewHolder(view, db!!)
            }
            VIEW_TYPE_PLAYLIST -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_playlist, parent, false)
                PlaylistViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AlbumViewHolder -> holder.bind(items[position] as Album, onItemClickListener)
            is PlaylistViewHolder -> holder.bind(items[position] as Playlist, onItemClickListener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is Album -> VIEW_TYPE_ALBUM
            is Playlist -> VIEW_TYPE_PLAYLIST
            else -> throw IllegalArgumentException("Unknown type")
        }
    }

    override fun getItemCount(): Int = items.size

    class AlbumViewHolder(itemView: View, private val db: MusicAppDatabaseHelper) : RecyclerView.ViewHolder(itemView) {
        private val albumImageView: ImageView = itemView.findViewById(R.id.albumCoverImageView)
        private val albumNameTextView: TextView = itemView.findViewById(R.id.albumNameTextView)
        private val albumArtistTextView: TextView = itemView.findViewById(R.id.albumReleaseYearTextView)

        fun bind(album: Album, onItemClickListener: ((Any) -> Unit)?) {
            albumNameTextView.text = album.name
            albumArtistTextView.text = db.getArtist(album.artistId)?.name ?: "No artist"

            // Load album image using Glide
            Glide.with(itemView.context)
                .load(album.image)
                .placeholder(R.drawable.blacker_gradient) // Placeholder while loading
                .error(R.drawable.blacker_gradient) // Error image if URL fails
                .into(albumImageView)

            // Handle item click
            itemView.setOnClickListener {
                onItemClickListener?.invoke(album)
            }
        }
    }

    class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val playlistImageView: ImageView = itemView.findViewById(R.id.icon)
        private val playlistNameTextView: TextView = itemView.findViewById(R.id.playlistName)

        fun bind(playlist: Playlist, onItemClickListener: ((Any) -> Unit)?) {
            playlistNameTextView.text = playlist.name

            // Load playlist image using Glide
            Glide.with(itemView.context)
                .load(playlist.image)
                .placeholder(R.drawable.blacker_gradient) // Placeholder while loading
                .error(R.drawable.blacker_gradient) // Error image if URL fails
                .into(playlistImageView)

            // Handle item click
            itemView.setOnClickListener {
                onItemClickListener?.invoke(playlist)
            }
        }
    }
}
