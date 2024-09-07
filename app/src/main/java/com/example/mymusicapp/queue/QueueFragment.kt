package com.example.mymusicapp.queue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymusicapp.R
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.models.Album
import com.example.mymusicapp.models.Artist
import com.example.mymusicapp.models.Playlist
import com.example.mymusicapp.models.Track
import com.example.mymusicapp.playlist.MenuFragment

class QueueFragment : Fragment() {

    private lateinit var recyclerViewQueue: RecyclerView
    private lateinit var recyclerViewPlaylist: RecyclerView
    private lateinit var queueSongAdapter: QueueSongAdapter
    private lateinit var backButton: ImageView

    private lateinit var dbHelper: MusicAppDatabaseHelper
    private lateinit var track: Track

    private var playlist: Playlist? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_queue, container, false)

        backButton = view.findViewById(R.id.back)

        // Set button click handlers
        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // Receive arguments passed to this fragment (if applicable)
        val imageUrl = arguments?.getString("IMAGE_URL") ?: "https://i.scdn.co/image/ab67616d0000b273356c22ef2466bb450b32e1bb"
        val songNameText = arguments?.getString("SONG_NAME") ?: "Default Song Name"
        val artistNameText = arguments?.getString("ARTIST_NAME") ?: "Default Artist Name"
        val playlistNameText = arguments?.getString("PLAYLIST_NAME") ?: "Default Playlist Name"

        // Update UI components
        val songCover = view.findViewById<ImageView>(R.id.songCover)
        val songName = view.findViewById<TextView>(R.id.songPlaying)
        val songArtist = view.findViewById<TextView>(R.id.artistSongPlaying)
        val playlistName = view.findViewById<TextView>(R.id.playlistName)




        // Get the Track object passed from the previous fragment or activity
        track = arguments?.getParcelable("TRACK") ?: return view
        playlist = arguments?.getParcelable("PLAYLIST")




        dbHelper = MusicAppDatabaseHelper(requireContext())

        // Fetch Album and Artist details
        val album: Album? = dbHelper.getAlbum(track.albumId)  // Ensure getAlbum accepts String type
        val artist: Artist? = dbHelper.getArtist(album?.artistId ?: "")  // Ensure getArtist accepts String type
        // Load album cover image
        Glide.with(this)
            .load(album?.image)
            .placeholder(R.drawable.blacker_gradient) // Placeholder image if no image is available
            .into(songCover)


        songName.text = track.name
        if (artist != null) {
            songArtist.text = artist.name
        }
        playlistName.text = playlist?.name ?: ""

        // Set up RecyclerViews
        val songs = listOf(
            QueueSong("Song 1", "Artist 1"),
            QueueSong("Song 2", "Artist 2"),
            QueueSong("Song 3", "Artist 3"),
            // Add more songs here
        )

        recyclerViewQueue = view.findViewById(R.id.recyclerViewSongsQueue)
        recyclerViewQueue.layoutManager = LinearLayoutManager(requireContext())
        queueSongAdapter = QueueSongAdapter(songs)
        recyclerViewQueue.adapter = queueSongAdapter

        recyclerViewPlaylist = view.findViewById(R.id.recyclerViewSongsPlaylist)
        recyclerViewPlaylist.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewPlaylist.adapter = queueSongAdapter

        return view
    }

    companion object {
        fun newInstance(track: Track, playlist: Playlist?): QueueFragment {
            val fragment = QueueFragment()
            val args = Bundle().apply {
                putParcelable("TRACK", track)
                if (playlist != null) {
                    putParcelable("PLAYLIST", playlist)
                }
            }
            fragment.arguments = args
            return fragment
        }
    }

}
