package com.example.mymusicapp.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicapp.R

class PlaylistFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var songAdapter: SongAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_single_playlist, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewSongs)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val songs = listOf(
            Song("grainy days", "moody.", "https://via.placeholder.com/53x52"),
            Song("Coffee", "Kainbeats", "https://via.placeholder.com/53x52"),
            Song("raindrops", "rainyyxx", "https://via.placeholder.com/53x52"),
            Song("Tokyo", "SmYang", "https://via.placeholder.com/53x52"),
            Song("Lullaby", "iamfinenow", "https://via.placeholder.com/53x52"),
            Song("Hazel Eyes", "moody.", "https://via.placeholder.com/53x52")
        )

        songAdapter = SongAdapter(songs)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = songAdapter
    }
}
