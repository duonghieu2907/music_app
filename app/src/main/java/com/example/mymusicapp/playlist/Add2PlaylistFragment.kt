package com.example.mymusicapp.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicapp.R

class Add2PlaylistFragment : Fragment() {

    private lateinit var playlistRecyclerView: RecyclerView
    private lateinit var playlistAdapter: PlaylistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_add_2_playlist, container, false)

        // Initialize RecyclerView
        playlistRecyclerView = view.findViewById(R.id.playlistRecyclerView)
        playlistRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Create an adapter and set it to the RecyclerView
        val playlists = listOf(
            PlaylistItem("https://i.scdn.co/image/ab67616d0000b273356c22ef2466bb450b32e1bb", "Current Favorites", "20 songs"),
            PlaylistItem("https://i.scdn.co/image/ab67616d0000b273356c22ef2466bb450b32e1bb", "3:00am vibes", "18 songs"),
            PlaylistItem("https://i.scdn.co/image/ab67616d0000b273356c22ef2466bb450b32e1bb", "Lofi Loft", "63 songs"),
            PlaylistItem("https://i.scdn.co/image/ab67616d0000b273356c22ef2466bb450b32e1bb", "Rain on My Window", "32 songs"),
            PlaylistItem("https://i.scdn.co/image/ab67616d0000b273356c22ef2466bb450b32e1bb", "Anime OSTs", "20 songs")
        )
        playlistAdapter = PlaylistAdapter(playlists)
        playlistRecyclerView.adapter = playlistAdapter

        return view
    }
}
