package com.example.mymusicapp.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicapp.R
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.models.Playlist
import com.example.mymusicapp.playlist.PlaylistFragment

//user's all playlists
class FragmentPlaylists : Fragment(), PlaylistListAdapter.FragmentPlaylistItemOnClickListener {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view : View = inflater.inflate(R.layout.fragment_playlist, container, false)
        app(view)

        return view
    }

    private fun app(view: View) {
        //Implementing here

        //Your liked playlists ?? xoóa đi bạn
        val yourLikedPlaylists : View = view.findViewById(R.id.YourLikedPlaylist)

        yourLikedPlaylists.setOnClickListener {

        }

        //Lists
        //Adapter
        val adapter = PlaylistListAdapter(createPlaylistItem(), this)

        //LayoutManager
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        //Setup recyclerView
        val recyclerView : RecyclerView = view.findViewById(R.id.PlaylistsList)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

    }

    private fun createPlaylistItem(): ArrayList<Playlist> {
        var sample = ArrayList<Playlist>()
        val db = MusicAppDatabaseHelper(requireContext())
        val allPlaylistId = db.getAllPlaylistId()!!

        //Get the playlist from database
        for(i in 0..<allPlaylistId.size) {
            sample.add(db.getPlaylist(allPlaylistId[i])!!)
        }

        //return
        return sample
    }

    override fun itemSelectionClickListener(item: Playlist?) {
        // Ensure that item is not null
        item?.let {
            val playlistFragment = PlaylistFragment.newInstance(item.playlistId) //Transfer id

            loadFragment(playlistFragment)

            Toast.makeText(requireContext(), "Worked!", Toast.LENGTH_SHORT).show()
        } ?: run {
            Toast.makeText(requireContext(), "Item is null!", Toast.LENGTH_SHORT).show()
        }
    }




    private fun loadFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
