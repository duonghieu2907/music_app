package com.example.mymusicapp.queue

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicapp.R

data class QueueSong(val name: String, val artist: String)

class QueueSongAdapter(private val songs: List<QueueSong>) : RecyclerView.Adapter<QueueSongAdapter.SongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_queue_song, parent, false)
        return SongViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val currentSong = songs[position]
        holder.songNameTextView.text = currentSong.name
        holder.artistNameTextView.text = currentSong.artist

        // You can add listeners or actions to ImageViews if needed
        holder.queueButtonImageView.setOnClickListener {
            // Handle button click action
        }

        holder.queueMenuImageView.setOnClickListener {
            // Handle menu click action
        }
    }

    override fun getItemCount() = songs.size

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val songNameTextView: TextView = itemView.findViewById(R.id.tvSongName)
        val artistNameTextView: TextView = itemView.findViewById(R.id.tvArtistName)
        val queueButtonImageView: ImageView = itemView.findViewById(R.id.queueButton1) // Reference to queue button
        val queueMenuImageView: ImageView = itemView.findViewById(R.id.queueMenu1) // Reference to queue menu
    }
}
