package com.example.mymusicapp.playlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymusicapp.R
import com.example.mymusicapp.models.Song

class SongsAdapter(private var songs: List<Song>, private val onItemClick: (Song) -> Unit) : RecyclerView.Adapter<SongsAdapter.SongViewHolder>() {

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val songTitle: TextView = itemView.findViewById(R.id.songTitleTextView)
        val songArtist: TextView = itemView.findViewById(R.id.songArtistTextView)
        val songImage: ImageView = itemView.findViewById(R.id.albumArtImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_small_song, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.songTitle.text = song.title
        holder.songArtist.text = song.artist


        Glide.with(holder.itemView.context)
            .load(song.imageUrl)
            .override(53,52)
            .placeholder(R.drawable.blacker_gradient)
            .into(holder.songImage)
        
        holder.itemView.setOnClickListener {
            onItemClick(song)
        }
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    fun updateSongs(newSongs: List<Song>) {
        songs = newSongs
        notifyDataSetChanged()
    }
}

