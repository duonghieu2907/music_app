package com.example.mymusicapp.playlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymusicapp.R

data class Song(
    val title: String,
    val artist: String,
    val albumArtUrl: String // URL or local resource path for the album art
)


class SongAdapter(private val songs: List<Song>) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.songTitleTextView)
        val artistTextView: TextView = itemView.findViewById(R.id.songArtistTextView)
        val albumArtImageView: ImageView = itemView.findViewById(R.id.albumArtImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_small_song, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.titleTextView.text = song.title
        holder.artistTextView.text = song.artist
        // Load the album art
        Glide.with(holder.itemView.context).load(song.albumArtUrl).into(holder.albumArtImageView) // Using Glide to load images

    }

    override fun getItemCount(): Int {
        return songs.size
    }
}
