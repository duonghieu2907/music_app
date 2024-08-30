package com.example.mymusicapp.playlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymusicapp.R

class SongAdapter(private val songs: List<Song>, private val clickListener: (Song) -> Unit) :
    RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val songTitle: TextView = itemView.findViewById(R.id.songTitleTextView)
        val songArtist: TextView = itemView.findViewById(R.id.songArtistTextView)
        val albumArt: ImageView = itemView.findViewById(R.id.albumArtImageView)

        fun bind(song: Song, clickListener: (Song) -> Unit) {
            songTitle.text = song.title
            songArtist.text = song.artist
            itemView.setOnClickListener { clickListener(song) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(songs[position], clickListener)
        val song = songs[position]
        holder.songTitle.text = song.title
        holder.songArtist.text = song.artist
        Glide.with(holder.itemView.context)
            .load(song.imageUrl)
            .into(holder.albumArt)
    }

    override fun getItemCount(): Int = songs.size
}
