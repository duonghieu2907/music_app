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
import com.example.mymusicapp.data.Global
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.models.Album

class FragmentAlbums : Fragment(),
    FragmentAlbumsAdapter.OnItemClickListener{
    private lateinit var adapter : FragmentAlbumsAdapter
    private val items = ArrayList<Album>()
    private lateinit var curUser: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view : View = inflater.inflate(R.layout.fragment_albums, container, false)
        val application = requireActivity().application as Global
        curUser = application.curUserId
        app(view)

        return view
    }

    //Suspend to run background
    private fun app(view: View) {
        //Adapter
        adapter = FragmentAlbumsAdapter(items, this)
        createAlbumsItem()
        updateAdapter(sort("ADDED"))

        //Layout manager
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        //Setup RecyclerView
        val recyclerView : RecyclerView = view.findViewById(R.id.AlbumsList)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        //Sort By
        //Init all sort
        val allSort = ArrayList<String>()
        allSort.addAll(arrayOf("Recently added", "Recently played", "A-Z", "Z-A"))

        //Init setOnClickListener
        val sortBut = view.findViewById<TextView>(R.id.sortButtonAlbum)
        sortBut.setOnClickListener {
            when (sortBut.text.toString()) {
                allSort[0] -> {
                    sortBut.text = allSort[1]
                }
                allSort[1] -> {
                    //set effect for the button
                    sortBut.text = allSort[2]
                    updateAdapter(sort("ASC"))
                }
                allSort[2] -> {
                    sortBut.text = allSort[3]
                    updateAdapter(sort("DESC"))
                }
                allSort[3] -> {
                    sortBut.text = allSort[0]
                    updateAdapter(sort("ADDED"))
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

    private fun addItem(album: Album) {
        items.add(album)
        adapter.notifyItemInserted(items.size - 1)
    }
    private fun createAlbumsItem(){
        val db = MusicAppDatabaseHelper(requireContext())
        updateAdapter(db.getUserFollowedAlbums(curUser))
        db.close()
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
        val sample: ArrayList<Album> = db.sort("album", order, curUser) as? ArrayList<Album>?: ArrayList()
        db.close()
        return sample
    }

    private fun getNewest() : ArrayList<Album> {
        val db = MusicAppDatabaseHelper(requireContext())
        val sample: ArrayList<Album> = db.getNewest("album", curUser) as? ArrayList<Album>?: ArrayList()
        db.close()
        return sample
    }
}
