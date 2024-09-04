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
import com.example.mymusicapp.models.Album
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentAlbums : Fragment(), FragmentAlbumsAdapter.OnItemClickListener {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view : View = inflater.inflate(R.layout.fragment_albums, container, false)

        //running background, load layout first
        CoroutineScope(Dispatchers.Main).launch {
            app(view)
        }

        return view
    }

    //Suspend to run background
    private suspend fun app(view: View) {
        //Your Liked Albums
        val yourLikedAlbums : View = view.findViewById(R.id.YourLikedAlbums)

        yourLikedAlbums.setOnClickListener {
            //Navigate here
        }

        //Adapter
        val adapter = FragmentAlbumsAdapter(createAlbumsItem(), this)


        //Layout manager
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        //Setup RecyclerView
        val recyclerView : RecyclerView = view.findViewById(R.id.AlbumsList)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

    }

    private suspend fun createAlbumsItem(): ArrayList<Album> {
        var sample = ArrayList<Album>()
        val db = MusicAppDatabaseHelper(requireContext())
        val allAlbumId = db.getAllAlbumId()!!

        //Get the albums from database
        for(i in 0..<allAlbumId.size) {
            sample.add(db.getAlbum(allAlbumId[i])!!)
        }

        //return
        return sample
    }

    override fun setOnItemClickListener(item: Album?) {
        //Navigate here
        Toast.makeText(context, "Worked!", Toast.LENGTH_SHORT).show()
    }
}
