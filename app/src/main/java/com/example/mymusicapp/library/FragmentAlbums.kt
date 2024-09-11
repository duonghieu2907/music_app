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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        allSort.add("Recently added")
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
        CoroutineScope(Dispatchers.IO).launch {
            val db = MusicAppDatabaseHelper(requireContext())
            val allAlbumId = db.getAllAlbumId()!!

            //Get the albums from database
            for(i in 0..<allAlbumId.size) {
                addItem(db.getAlbum(allAlbumId[i])!!)
            }
        }
    }

    override fun setOnItemClickListener(item: Album?) {
        // Ensure that item is not null
        item?.let {
            val albumFragment = AlbumFragment.newInstance(item.albumId) //Transfer id

            // Use a bundle to pass data to PlaylistFragment if needed
            /*val bundle = Bundle()
            bundle.putSerializable("playlistItem", it)  // Assuming PlaylistListItem is Serializable
            playlistFragment.arguments = bundle*/

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, albumFragment)  // Replace with your fragment container ID
                .addToBackStack(null)  // Optional: Add this transaction to the back stack
                .commit()

            Toast.makeText(requireContext(), "Worked!", Toast.LENGTH_SHORT).show()
        } ?: run {
            Toast.makeText(requireContext(), "Item is null!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sort(order : String): ArrayList<Album> {
        val db = MusicAppDatabaseHelper(requireContext())
        val sample: ArrayList<Album> = db.sort("album", order, curUser) as? ArrayList<Album>?: ArrayList()
        return sample
    }
}
