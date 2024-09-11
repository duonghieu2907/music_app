package com.example.mymusicapp.playlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicapp.R
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.models.Album
import com.example.mymusicapp.models.Artist
import com.example.mymusicapp.models.Playlist
import com.example.mymusicapp.models.Track

class PlaylistSongAdapter(

    private val dbHelper: MusicAppDatabaseHelper,
    private val playlist: Playlist?,
    private val currentTrackIndex: Int, // To filter the remaining songs in the playlist
//    private val onRemoveClickListener: (Track) -> Unit // Add a listener for remove button clicks
) : RecyclerView.Adapter<PlaylistSongAdapter.SongViewHolder>() {

    private var songs: List<Track> = emptyList()

    init {
        playlist?.let {
            songs = dbHelper.getTracksByPlaylistId(it.playlistId)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_queue_song, parent, false)
        return SongViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val actualPosition = currentTrackIndex + 1 + position // Start from the next song after current





        if (actualPosition < songs.size) {
            val currentSong = songs[actualPosition]
            holder.songNameTextView.text = currentSong.name

            // Fetch Album and Artist details
            val album: Album? = dbHelper.getAlbum(currentSong.albumId)
            val artist: Artist? = dbHelper.getArtist(album?.artistId ?: "")

            if (artist != null) {
                holder.artistNameTextView.text = artist.name.toString()
            }

            // You can add listeners or actions to ImageViews if needed
            holder.queueButtonImageView.setOnClickListener {
                // Handle button click action
            }

            holder.queueMenuImageView.setOnClickListener {
                // Handle menu click action
            }

//            holder.removeButtonImageView.setOnClickListener {
//                onRemoveClickListener(currentSong) // Notify the fragment about the track to remove
//            }

            holder.itemView.setOnClickListener {
                onItemClickListener?.onItemClick(position)
            }
        }
    }

    override fun getItemCount() = songs.size - (currentTrackIndex + 1)

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val songNameTextView: TextView = itemView.findViewById(R.id.tvSongName)
        val artistNameTextView: TextView = itemView.findViewById(R.id.tvArtistName)
        val queueButtonImageView: ImageView = itemView.findViewById(R.id.queueButton1) // Reference to queue button
        val queueMenuImageView: ImageView = itemView.findViewById(R.id.queueMenu1) // Reference to queue menu
        //val removeButtonImageView: ImageView = itemView.findViewById(R.id.removeButton)
    }


    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }
}