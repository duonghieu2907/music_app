package com.example.mymusicapp.library

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicapp.R
import com.example.mymusicapp.createnew.CreateNewPlaylist

class FragmentYourLibrary : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_your_library, container, false)
        App(view)

        return view
    }

    private fun App(view: View) {
        //Add new playlist
        val addNewPlaylistSection : View  = view.findViewById(R.id.addNewPlaylistsSection)

        addNewPlaylistSection.setOnClickListener {
            //Sample to test if working
            val intent: Intent = Intent(context, CreateNewPlaylist::class.java)
            startActivity(intent)
        }

        //your liked song
        val yourLikedSongs : View = view.findViewById(R.id.YourLikedSongSection)

        yourLikedSongs.setOnClickListener {
            //Change here
        }

        //Lists
        //Adapter
        val fragmentYourLibraryAdapter = FragmentYourLibraryAdapter(createPlaylistItem())

        //LayoutManager
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        //Set recycleView
        val recyclerView : RecyclerView = view.findViewById(R.id.yourLibraryList)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = fragmentYourLibraryAdapter
    }

    private fun createPlaylistItem(): ArrayList<PlaylistItem> {
        val sample = ArrayList<PlaylistItem>()

        //Sample
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.playlistsample)
        sample.add(PlaylistItem("Conan Gray", bitmap))

        //return
        return sample
    }
}