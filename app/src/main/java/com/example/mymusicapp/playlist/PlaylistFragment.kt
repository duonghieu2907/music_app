package com.example.mymusicapp.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicapp.R
import android.widget.Toast
import com.example.mymusicapp.models.Song

class PlaylistFragment : Fragment() {

    private lateinit var playlistTitle: TextView
    private lateinit var playlistSubtitle: TextView
    private lateinit var recyclerViewSongs: RecyclerView
    private lateinit var songsAdapter: SongsAdapter

    // Dummy song data
    private val songList = listOf(
        Song("grainy days", "moody.", "https://i.scdn.co/image/ab67616d0000b273356c22ef2466bb450b32e1bb", "You never look at the sky...", "2:43"),
        Song("Coffee", "Kainbeats", "https://i1.sndcdn.com/artworks-IBdGI4FyQ2iKPI1e-2jwAgQ-t500x500.jpg", "You never look at the sky...", "2:43"),
        Song("raindrops", "rainyyxx", "https://via.placeholder.com/53x52", "You never look at the sky...", "2:43"),
        Song("Tokyo", "SmYang", "https://via.placeholder.com/53x52", "You never look at the sky...", "2:43"),
        Song("Lullaby", "iamfinenow", "https://via.placeholder.com/53x52", "You never look at the sky...", "2:43"),
        Song("Hazel Eyes", "moody.", "https://via.placeholder.com/53x52", "You never look at the sky...", "2:43")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_single_playlist, container, false)

        // Initialize UI components
        playlistTitle = view.findViewById(R.id.playlistTitle)
        playlistSubtitle = view.findViewById(R.id.playlistSubtitle)
        recyclerViewSongs = view.findViewById(R.id.recyclerViewSongs)

        // Set up RecyclerView
        recyclerViewSongs.layoutManager = LinearLayoutManager(requireContext())
        songsAdapter = SongsAdapter(songList) { song -> openSong(song) }
        recyclerViewSongs.adapter = songsAdapter

        // Set playlist details
        playlistTitle.text = "Lofi Loft"
        playlistSubtitle.text = "soft, chill, dreamy, lo-fi beats"

        return view
    }
    private fun openSong(song: Song) {
        val fragment = SingleSongFragment.newInstance(song)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
        Toast.makeText(requireContext(), song.title, Toast.LENGTH_SHORT).show()
    }
}

