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
import com.example.mymusicapp.album.AlbumFragment
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.models.Album
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentAlbums : Fragment(),
    FragmentAlbumsAdapter.OnItemClickListener{
    private lateinit var adapter : FragmentAlbumsAdapter
    private val items = ArrayList<Album>()
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
        //Adapter
        items.addAll(createAlbumsItem())
        adapter = FragmentAlbumsAdapter(items, this)


        //Layout manager
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        //Setup RecyclerView
        val recyclerView : RecyclerView = view.findViewById(R.id.AlbumsList)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        //Sort By
        //Init all sort
        val allSort = ArrayList<String>()
        allSort.add("Recently played")
        allSort.add("A-Z")
        allSort.add("Z-A")

        //Init setOnClickListener
        val sortBut = view.findViewById<TextView>(R.id.sortButtonAlbum)
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
    private fun updateAdapter(list : ArrayList<Album>) {
        items.clear()
        items.addAll(list)
        adapter.notifyDataSetChanged()
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
        // Ensure that item is not null
        item?.let {
            // Check if the current fragment is added to the activity before proceeding
            if (isAdded) {
                val albumFragment = AlbumFragment.newInstance(item.albumId) //Transfer id

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, albumFragment)
                    .addToBackStack(null)
                    .commit()

                Toast.makeText(requireContext(), "Worked!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Fragment not attached to activity", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(requireContext(), "Item is null!", Toast.LENGTH_SHORT).show()
        }
    }


    private fun sort(order : String): ArrayList<Album> {
        val db = MusicAppDatabaseHelper(requireContext())
        val sample: ArrayList<Album> = db.sort("album", order) as? ArrayList<Album>?: ArrayList()
        return sample
    }
}
