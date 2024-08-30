package com.example.mymusicapp.library

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicapp.R

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

        //Your liked playlists
        val yourLikedPlaylists : View = view.findViewById(R.id.YourLikedPlaylist)

        yourLikedPlaylists.setOnClickListener {
            //Navigate here
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

    private fun createPlaylistItem(): ArrayList<PlaylistListItem> {
        val sample = ArrayList<PlaylistListItem>()

        //Sample
        var bitmap = BitmapFactory.decodeResource(resources, R.drawable.playlistsample)
        sample.add(PlaylistListItem("Conan Gray", bitmap))

        bitmap = BitmapFactory.decodeResource(resources, R.drawable.chase_atlantic)
        sample.add(PlaylistListItem("Chase Atlantic", bitmap))

        //return
        return sample
    }

    override fun itemSelectionClickListener(item: PlaylistListItem?) {
        //Navigation here
        Toast.makeText(context, "Worked!", Toast.LENGTH_SHORT).show()
    }
}
