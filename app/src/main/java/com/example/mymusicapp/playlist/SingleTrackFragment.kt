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
    private lateinit var lyricsText: TextView
    private lateinit var songEndTime: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_single_song, container, false)

        // Initialize UI components
        songTitle = view.findViewById(R.id.song_title)
        artistName = view.findViewById(R.id.artist_name)
        songCover = view.findViewById(R.id.song_cover)
        lyricsText = view.findViewById(R.id.lyrics_text)
        songEndTime = view.findViewById(R.id.song_end_time)

        // Get the Track object passed from previous fragment or activity
        track = arguments?.getParcelable("TRACK") ?: return view

        dbHelper = MusicAppDatabaseHelper(requireContext())


        // Fetch Album and Artist details
        val album: Album? = dbHelper.getAlbum(track.albumId)
        val artist: Artist? = dbHelper.getArtist(album?.artistId ?: 0)

        // Set the UI components with track details
        songTitle.text = track.name
        artistName.text = artist?.name ?: "Unknown Artist"
        lyricsText.text = "No lyrics available"
        songEndTime.text = track.duration

        // Load album cover image
        Glide.with(this)
            .load(album?.image)
            .placeholder(R.drawable.blacker_gradient) // Placeholder image if no image is available
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
