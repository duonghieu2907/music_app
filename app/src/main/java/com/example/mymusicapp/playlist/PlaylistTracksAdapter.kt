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
import com.example.mymusicapp.models.Track
import com.example.mymusicapp.models.Album
import com.example.mymusicapp.models.Artist

class PlaylistTracksAdapter(
    private val trackList: List<Track>,
    private val dbHelper: MusicAppDatabaseHelper,
    private val onItemClick: (Track) -> Unit
) : RecyclerView.Adapter<PlaylistTracksAdapter.TrackViewHolder>() {

    inner class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val songTitle: TextView = itemView.findViewById(R.id.songTitleTextView)
        val songArtist: TextView = itemView.findViewById(R.id.songArtistTextView)
        val songImage: ImageView = itemView.findViewById(R.id.albumArtImageView)
        val menuButton: ImageView = itemView.findViewById(R.id.menuButton)

        init {
            itemView.setOnClickListener {
                onItemClick(trackList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_small_song, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = trackList[position]

        val album: Album? = dbHelper.getAlbum(track.albumId)
        val artist: Artist? = dbHelper.getArtist(album?.artistId ?: "")

        holder.songTitle.text = track.name
        holder.songArtist.text = artist?.name ?: "Unknown Artist"

        // Load album image
        Glide.with(holder.itemView.context)
            .load(album?.image)
            .placeholder(R.drawable.blacker_gradient) // Placeholder image
            .into(holder.songImage)

        holder.menuButton.setOnClickListener { view ->
            showMenu(view, track)
        }
    }

    private fun showMenu(view: View?, track: Track) {
        //jump to Menu Fragment
    }

    override fun getItemCount(): Int {
        return trackList.size
    }
}
