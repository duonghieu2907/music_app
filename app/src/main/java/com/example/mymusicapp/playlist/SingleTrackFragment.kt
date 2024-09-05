package com.example.mymusicapp.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.mymusicapp.R
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.models.Track
import com.example.mymusicapp.models.Album
import com.example.mymusicapp.models.Artist

class SingleTrackFragment : Fragment() {

    private lateinit var dbHelper: MusicAppDatabaseHelper
    private lateinit var track: Track

    private lateinit var songTitle: TextView
    private lateinit var artistName: TextView
    private lateinit var songCover: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_single_song, container, false)

        songTitle = view.findViewById(R.id.song_title)
        artistName = view.findViewById(R.id.artist_name)
        songCover = view.findViewById(R.id.song_cover)

        // Track object from ARG
        track = arguments?.getParcelable("TRACK") ?: return view

        dbHelper = MusicAppDatabaseHelper(requireContext())

        // Fetch Album and Artist details
        val album: Album? = dbHelper.getAlbum(track.albumId)  // Ensure getAlbum accepts String type
        val artist: Artist? = dbHelper.getArtist(album?.artistId ?: "")  // Ensure getArtist accepts String type

        songTitle.text = track.name
        artistName.text = artist?.name ?: "Unknown Artist"

        // album cover image
        Glide.with(this)
            .load(album?.image)
            .placeholder(R.drawable.blacker_gradient)
            .into(songCover)

        return view
    }

    companion object {
        fun newInstance(track: Track): SingleTrackFragment {
            val fragment = SingleTrackFragment()
            val args = Bundle()
            args.putParcelable("TRACK", track)
            fragment.arguments = args
            return fragment
        }
    }
}
