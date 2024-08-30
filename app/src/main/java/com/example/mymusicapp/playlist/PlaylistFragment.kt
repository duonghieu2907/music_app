package com.example.mymusicapp.playlist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicapp.R


// single playlist
class PlaylistFragment : Fragment() {

    private lateinit var songList: List<Song>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_single_playlist, container, false)

        // Initialize the list of songs (this can be fetched from a ViewModel or other data source)
        songList = listOf(
            Song("grainy days", "moody.", "https://via.placeholder.com/345x330", "You never look at the sky...", "2:43"),
            Song("Coffee", "Kainbeats", "https://via.placeholder.com/53x52", "You never look at the sky...", "2:43"),
            Song("raindrops", "rainyyxx", "https://via.placeholder.com/53x52", "You never look at the sky...", "2:43"),
            Song("Tokyo", "SmYang", "https://via.placeholder.com/53x52", "You never look at the sky...", "2:43"),
            Song("Lullaby", "iamfinenow", "https://via.placeholder.com/53x52", "You never look at the sky...", "2:43"),
            Song("Hazel Eyes", "moody.", "https://via.placeholder.com/53x52", "You never look at the sky...", "2:43")
        )

        // Set up RecyclerView and Adapter
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewSongs)
        val adapter = SongAdapter(songList) { song ->
            onSongClick(song)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }

    private fun onSongClick(song: Song) {
        val intent = Intent(requireContext(), SingleSongActivity::class.java)
        intent.putExtra("song", song)  // Put the song object as Parcelable
        startActivity(intent)
    }
}
