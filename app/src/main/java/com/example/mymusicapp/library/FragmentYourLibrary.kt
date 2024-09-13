package com.example.mymusicapp.library

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicapp.R
import com.example.mymusicapp.createnew.CreateNewPlaylist
import com.example.mymusicapp.data.Global
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.models.Track
import com.example.mymusicapp.playlist.FragmentNewPlaylist
import com.example.mymusicapp.playlist.PlaylistFragment
import com.example.mymusicapp.playlist.SingleTrackFragment

class FragmentYourLibrary : Fragment(), FragmentYourLibraryAdapter.OnItemClickListener {
    companion object {
        private var items = ArrayList<Track>()
        private lateinit var adapter: FragmentYourLibraryAdapter
        private lateinit var curUserId: String
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view : View = inflater.inflate(R.layout.fragment_your_library, container, false)
        val app = requireActivity().application as Global
        curUserId = app.curUserId


        app(view)

        return view
    }

    private fun app(view: View) {
        //Add new playlist
        val addNewPlaylistSection : View  = view.findViewById(R.id.addNewPlaylistsSection)

        addNewPlaylistSection.setOnClickListener {


            openFragment(FragmentNewPlaylist.newInstance())  // Pass the track data when opening Add2PlaylistFragment




        }

        //your liked song
        val yourLikedSongs : View = view.findViewById(R.id.YourLikedSongSection)

        yourLikedSongs.setOnClickListener {
            val playlistFragment = PlaylistFragment.newInstance("userLikedSongs") //Transfer id
            loadFragment(playlistFragment)
        }

        //Lists
        //Adapter
        adapter = FragmentYourLibraryAdapter(items, this)
        updateAdapter(getNewest())

        //LayoutManager
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        //Set recycleView
        val recyclerView : RecyclerView = view.findViewById(R.id.yourLibraryList)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateAdapter(list : ArrayList<Track>) {
        items.clear()
        items.addAll(list)
        adapter.notifyDataSetChanged()
    }

    override fun setOnItemClickListener(item: Track?) {
        val db = MusicAppDatabaseHelper(requireContext())
        val fragment = db.getTrack(item?.trackId?:"")?.let { track ->
            SingleTrackFragment.newInstance(
                track.trackId, null, null)
        }

        if (fragment != null) {
            loadFragment(fragment)
        }

    }
    private fun loadFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun getNewest() : ArrayList<Track> {
        val db = MusicAppDatabaseHelper(requireContext())
        val sample: ArrayList<Track> = db.getNewest("track", curUserId) as? ArrayList<Track>?: ArrayList()
        db.close()
        return sample
    }



    private fun openFragment(fragment: Fragment) {
        val fragmentManager = parentFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment) // Replace with the ID of your container
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}