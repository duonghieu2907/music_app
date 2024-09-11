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
                       private val onTrackSelected: (Track?) -> Unit // Updated lambda parameter
) : RecyclerView.Adapter<QueueSongAdapter.SongViewHolder>() {

//    // List to store the selected state of each item
//    private val selectedStates = mutableListOf<Boolean>()


    // Variable to store the currently selected position
    private var selectedPosition: Int = RecyclerView.NO_POSITION

//    init {
//        // Initialize all items to unselected by default
//        for (i in 0 until TrackQueue.queue.size) {
//            selectedStates.add(false)
//        }
//    }

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

//        // Set the button drawable based on selected state
//        val drawableRes = if (selectedStates[position]) {
//            R.drawable.clicked
//        } else {
//            R.drawable.unclicked
//        }
//        holder.queueButtonImageView.setImageResource(drawableRes)

        // Set the button drawable based on whether this item is the selected one
        val drawableRes = if (position == selectedPosition) {
            R.drawable.clicked
        } else {
            R.drawable.unclicked
        }
        holder.queueButtonImageView.setImageResource(drawableRes)




        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(position)

            onTrackSelected(currentSong) // Trigger lambda to select the track
            Log.d("QueueSongAdapter", "Track selected: ${currentSong.name}")

            if (selectedPosition == position) {
                // If the item is already selected, unselect it
                selectedPosition = RecyclerView.NO_POSITION
                onTrackSelected(null)  // No track is selected now
            } else {
                // Otherwise, select this item and unselect the previous one
                val previousPosition = selectedPosition
                selectedPosition = position
                notifyItemChanged(previousPosition)  // Update the previously selected item
                onTrackSelected(currentSong)  // Trigger track selection
            }
            notifyItemChanged(position)  // Update the current item
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
        //val queueMenuImageView: ImageView = itemView.findViewById(R.id.queueMenu1)
        //val removeButtonImageView: ImageView = itemView.findViewById(R.id.removeButton)
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }


    // Method to get the selected track, returns null if no track is selected
    fun getSelectedTrack(): Track? {
        return if (selectedPosition != RecyclerView.NO_POSITION) {
            TrackQueue.queue[selectedPosition]
        } else {
            null
        }
    }

    // Reset the selected position
    fun resetSelection() {
        selectedPosition = RecyclerView.NO_POSITION  // Reset selected position to no selection
    }
}
