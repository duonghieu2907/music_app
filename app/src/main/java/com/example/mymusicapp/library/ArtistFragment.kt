package com.example.mymusicapp.library

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymusicapp.R
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.models.Album
import com.example.mymusicapp.models.Playlist
import com.example.mymusicapp.models.Track
import com.example.mymusicapp.playlist.PlaylistTracksAdapter
import com.example.mymusicapp.playlist.SingleTrackFragment

class ArtistFragment : Fragment(R.layout.fragment_artist) {
    private lateinit var dbHelper: MusicAppDatabaseHelper
    private var artistId: String? = null

    companion object {
        fun newInstance(artistId: String): ArtistFragment {
            val fragment = ArtistFragment()
            val args = Bundle()
            args.putString("artist_id", artistId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_artist, container, false)

        artistId = arguments?.getString("artist_id") ?: ""

        // Initialize DBHelper
        dbHelper = MusicAppDatabaseHelper(requireContext())

        // Fetch artist details using the getArtist function
        val artist = dbHelper.getArtist(artistId!!)

        // Bind the artist data to the UI elements
        artist?.let {
            val artistNameTextView = view.findViewById<TextView>(R.id.artistNameTextView)
            val artistGenreTextView = view.findViewById<TextView>(R.id.artistGenreTextView)
            val artistImageView = view.findViewById<ImageView>(R.id.artistImageView)

            artistNameTextView.text = it.name
            artistGenreTextView.text = it.genre

            // Load artist image using Glide or any image loading library
            Glide.with(this)
                .load(it.image)
                .placeholder(R.drawable.ic_avatar) // Set a default placeholder
                .into(artistImageView)
        }

        // Set up RecyclerView for top 5 tracks and albums
        setupRecyclerView(view, artistId!!)

        return view
        }
    private fun setupRecyclerView(view: View, artistId: String) {
        // Top 5 tracks RecyclerView setup
        val top5Tracks = dbHelper.getTop5TracksByArtist(artistId) // Assume you have this method
        Log.d("ArtistFragment", "Top 5 Tracks: $top5Tracks")  // Log the result
        val trackAdapter = PlaylistTracksAdapter(this, top5Tracks, dbHelper, null) { track -> openTrack(track) }
        val trackRecyclerView = view.findViewById<RecyclerView>(R.id.topSongsRecyclerView)
        trackRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        trackRecyclerView.adapter = trackAdapter

        // Albums RecyclerView setup
        val albums = dbHelper.getAlbumsByArtistId(artistId) // Fetch albums of the artist
        val albumsAdapter = FragmentAlbumsAdapter(ArrayList(albums))

        val albumsRecyclerView = view.findViewById<RecyclerView>(R.id.albumsRecyclerView)
        albumsRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        albumsRecyclerView.adapter = albumsAdapter
    }

    private fun openTrack(track: Track) {
        val fragment = SingleTrackFragment.newInstance(track.trackId, null, null)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
        Toast.makeText(requireContext(), track.name, Toast.LENGTH_SHORT).show()
    }
}

