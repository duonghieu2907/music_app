package com.example.mymusicapp.queue

import android.util.Log
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
import com.example.mymusicapp.models.Track
import com.example.mymusicapp.models.TrackQueue

data class QueueSong(val name: String, val artist: String)

class QueueSongAdapter(
                       private val dbHelper: MusicAppDatabaseHelper,
                       private val onTrackSelected: (Track) -> Unit // Updated lambda parameter
) : RecyclerView.Adapter<QueueSongAdapter.SongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_queue_song, parent, false)
        return SongViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val currentSong = TrackQueue.queue[position]
        holder.songNameTextView.text = currentSong.name

        // Fetch Album and Artist details
        val album: Album? = dbHelper.getAlbum(currentSong.albumId)
        val artist: Artist? = dbHelper.getArtist(album?.artistId ?: "")

        holder.artistNameTextView.text = artist?.name ?: "Unknown Artist"




        holder.queueButtonImageView.setOnClickListener {
            // Handle button click action
        }

        holder.queueMenuImageView.setOnClickListener {
            // Handle menu click action
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(position)

            onTrackSelected(currentSong) // Trigger lambda to select the track
            Log.d("QueueSongAdapter", "Track selected: ${currentSong.name}")
        }

//        // Correct the click listener setup for the remove button
//        holder.itemView.findViewById<View>(R.id.removeButton)?.setOnClickListener {
//            Log.d("QueueSongAdapter", "Remove button clicked for track: ${currentSong.name}")
//            onRemoveTrack(currentSong)
//        }
    }

    override fun getItemCount() = TrackQueue.queue.size

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val songNameTextView: TextView = itemView.findViewById(R.id.tvSongName)
        val artistNameTextView: TextView = itemView.findViewById(R.id.tvArtistName)
        val queueButtonImageView: ImageView = itemView.findViewById(R.id.queueButton1)
        val queueMenuImageView: ImageView = itemView.findViewById(R.id.queueMenu1)
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
