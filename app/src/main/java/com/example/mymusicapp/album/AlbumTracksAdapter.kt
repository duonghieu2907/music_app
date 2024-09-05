package com.example.mymusicapp.album

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymusicapp.R
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.models.*
class AlbumTracksAdapter(
    private val tracks: List<Track>,
    private val dbHelper: MusicAppDatabaseHelper,
    private val onItemClick: (Track ) -> Unit
) : RecyclerView.Adapter<AlbumTracksAdapter.AlbumTrackViewHolder>() {

    inner class AlbumTrackViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val songTitle: TextView = view.findViewById(R.id.songTitle)
        val menuButton: ImageView = view.findViewById(R.id.menuButton)
        init {
            itemView.setOnClickListener {
                onItemClick(tracks[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumTrackViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_album_song, parent, false)
        return AlbumTrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlbumTrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.songTitle.text = track.name

        holder.menuButton.setOnClickListener { view ->
            showMenu(view, track)
        }
    }

    private fun showMenu(view: View?, track: Track) {
        //jump to Menu Fragment
    }

    override fun getItemCount(): Int = tracks.size
}
