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
import com.example.mymusicapp.data.SpotifyData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentAlbums : Fragment(), FragmentAlbumsAdapter.OnItemClickListener {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view : View = inflater.inflate(R.layout.fragment_albums, container, false)

        app(view)

        return view
    }

    private fun app(view: View) {
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

    private fun createAlbumsItem(): ArrayList<AlbumItem> {
        var sample = ArrayList<AlbumItem>()
        val spotifyData = SpotifyData()

        CoroutineScope(Dispatchers.IO).launch {
            spotifyData.buildSearchApi()
            withContext(Dispatchers.Main) {
                sample = spotifyData.findAllPlaylist()!!
            }
        }
        //return
        return sample
    }

    override fun setOnItemClickListener(item: AlbumItem?) {
        //Navigate here
        Toast.makeText(context, "Worked!", Toast.LENGTH_SHORT).show()
    }
}
