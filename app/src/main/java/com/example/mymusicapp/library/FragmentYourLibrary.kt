package com.example.mymusicapp.library

import android.content.Intent
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
import com.example.mymusicapp.createnew.CreateNewPlaylist
import com.example.mymusicapp.playlist.PlaylistFragment

class FragmentYourLibrary : Fragment(), FragmentYourLibraryAdapter.OnItemClickListener {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view : View = inflater.inflate(R.layout.fragment_your_library, container, false)
        app(view)

        return view
    }

    private fun app(view: View) {
        //Add new playlist
        val addNewPlaylistSection : View  = view.findViewById(R.id.addNewPlaylistsSection)

        addNewPlaylistSection.setOnClickListener {
            //Sample to test if working
            val intent = Intent(context, CreateNewPlaylist::class.java)
            startActivity(intent)
        }

        //your liked song
        val yourLikedSongs : View = view.findViewById(R.id.YourLikedSongSection)

        yourLikedSongs.setOnClickListener {
            val playlistFragment = PlaylistFragment.newInstance("userLikedSongs") //Transfer id
            loadFragment(playlistFragment)
        }

        //Lists
        //Adapter
        val fragmentYourLibraryAdapter = FragmentYourLibraryAdapter(createPlaylistItem(), this)

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

    override fun setOnItemClickListener(item: PlaylistItem?) {
        //Navigate here
        Toast.makeText(context, "Worked!", Toast.LENGTH_SHORT).show()
    }
    private fun loadFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}