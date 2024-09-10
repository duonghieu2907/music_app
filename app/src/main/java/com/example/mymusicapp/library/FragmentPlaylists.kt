package com.example.mymusicapp.library

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
    private lateinit var adapter : PlaylistListAdapter
    private val items = ArrayList<Playlist>()
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

        //Adapter
        items.addAll(createPlaylistItem())
        adapter = PlaylistListAdapter(items, this)

        //LayoutManager
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        //Setup recyclerView
        val recyclerView : RecyclerView = view.findViewById(R.id.PlaylistsList)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        //Sort By
        //Init all sort
        val allSort = ArrayList<String>()
        allSort.add("Recently played")
        allSort.add("A-Z")
        allSort.add("Z-A")

        //Init setOnClickListener
        val sortBut = view.findViewById<TextView>(R.id.sortButtonPlaylist)
        sortBut.setOnClickListener {
            when (sortBut.text.toString()) {
                allSort[0] -> {
                    //set effect for the button
                    sortBut.text = allSort[1]
                    updateAdapter(sort("ASC"))
                }
                allSort[1] -> {
                    sortBut.text = allSort[2]
                    updateAdapter(sort("DESC"))
                }
                allSort[2] -> {
                    sortBut.text = allSort[0]
                }
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun updateAdapter(list : ArrayList<Playlist>) {
        items.clear()
        items.addAll(list)
        adapter.notifyDataSetChanged()
    }

    private fun createPlaylistItem(): ArrayList<Playlist> {
        var sample = ArrayList<Playlist>()
        val db = MusicAppDatabaseHelper(requireContext())
        val allPlaylistId = db.getAllPlaylistId() ?: return sample

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

    private fun sort(order : String): ArrayList<Playlist> {
        val db = MusicAppDatabaseHelper(requireContext())
        val sample: ArrayList<Playlist> = db.sort("playlist", order) as? ArrayList<Playlist>?: ArrayList()
        return sample
    }
}
