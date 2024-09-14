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
import com.example.mymusicapp.MainActivity
import com.example.mymusicapp.R
import com.example.mymusicapp.album.AlbumFragment
import com.example.mymusicapp.data.Global
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.models.Album
import com.example.mymusicapp.models.Track
import com.example.mymusicapp.playlist.PlaylistTracksAdapter
import com.example.mymusicapp.playlist.SingleTrackFragment

class ArtistFragment : Fragment(R.layout.fragment_artist),
    FragmentAlbumsAdapter.OnItemClickListener {
    private lateinit var dbHelper: MusicAppDatabaseHelper
    private lateinit var artistId: String
    private lateinit var curUserId: String

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
        val app = requireActivity().application as Global
        curUserId = app.curUserId!!

        // Initialize DBHelper
        dbHelper = MusicAppDatabaseHelper(requireContext())

        // Fetch artist details using the getArtist function
        val artist = dbHelper.getArtist(artistId)

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
        setupRecyclerView(view, artistId)

        // Initialize UI
        val backButton = view.findViewById<ImageView>(R.id.backButton)
        val followButton = view.findViewById<ImageView>(R.id.follow_background)
        val followText = view.findViewById<TextView>(R.id.follow_text)

        //Check the initial state of follow button
        var isFollowed = dbHelper.isArtistFollowed(curUserId, artistId)
        stateOfFollow(isFollowed, followText, followButton)

        //Set up clicking and add to database
        followButton.setOnClickListener {
            if(isFollowed) dbHelper.unfollowArtist(curUserId, artistId)
            else dbHelper.followArtist(curUserId, artistId)
            isFollowed = !isFollowed
            stateOfFollow(isFollowed, followText, followButton)
        }

        // Set up back button
        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        return view
    }
    override fun onResume() {
        super.onResume()
        val activity = requireActivity()
        if (activity is MainActivity) {
            activity.hideBottomNavigation()
        }
    }
    private fun setupRecyclerView(view: View, artistId: String) {
        // Top 5 tracks RecyclerView setup
        val top5Tracks = dbHelper.getTop5TracksByArtist(artistId) // Assume you have this method
        val trackAdapter = PlaylistTracksAdapter(this, top5Tracks, dbHelper, "") { track -> openTrack(track) }
        val trackRecyclerView = view.findViewById<RecyclerView>(R.id.topSongsRecyclerView)
        trackRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        trackRecyclerView.adapter = trackAdapter

        // Albums RecyclerView setup
        val albums = dbHelper.getAlbumsByArtistId(artistId) // Fetch albums of the artist
        val albumsAdapter = FragmentAlbumsAdapter(ArrayList(albums), this)

        val albumsRecyclerView = view.findViewById<RecyclerView>(R.id.albumsRecyclerView)
        albumsRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        albumsRecyclerView.adapter = albumsAdapter
    }

    override fun setOnItemClickListener(item: Album?) {
        // Ensure that item is not null
        item?.let {
            // Check if the current fragment is added to the activity before proceeding
            if (isAdded) {
                val albumFragment = AlbumFragment.newInstance(item.albumId) //Transfer id

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, albumFragment)
                    .addToBackStack(null)
                    .commit()

                Toast.makeText(requireContext(), "Worked!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Fragment not attached to activity", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(requireContext(), "Item is null!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openTrack(track: Track) {
        val fragment = SingleTrackFragment.newInstance(track.trackId, null, null)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
        Toast.makeText(requireContext(), track.name, Toast.LENGTH_SHORT).show()
    }

    private fun stateOfFollow(isFollowed: Boolean, followText: TextView, followButton: ImageView) {
        if (isFollowed) {
            followText.text = "Following"
            followButton.setBackgroundResource(R.drawable.rectangle_item_filter_color)
        }
        else {
            followText.text = "Follow"
            followButton.setBackgroundResource(R.drawable.rectangle_item_filter_nocolor)
        }
    }
}

