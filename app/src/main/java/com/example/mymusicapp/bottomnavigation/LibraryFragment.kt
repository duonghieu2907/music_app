package com.example.mymusicapp.bottomnavigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicapp.R
import com.example.mymusicapp.library.FragmentAlbums
import com.example.mymusicapp.library.FragmentArtists
import com.example.mymusicapp.library.FragmentLibraryFilterAdapter
import com.example.mymusicapp.library.FragmentPlaylists
import com.example.mymusicapp.library.FragmentPodcasts
import com.example.mymusicapp.library.FragmentYourLibrary
import com.example.mymusicapp.library.LibraryFilterItem

class LibraryFragment : Fragment(), FragmentLibraryFilterAdapter.FragmentLibraryFilterSelectionListener {
    private lateinit var drawerLayout: DrawerLayout
    private var filterName: String = ""

    companion object{
        private const val ARG_FILTER_NAME = "filter_name"
        fun newInstance(filterName: String): LibraryFragment {
            val fragment = LibraryFragment()
            val args = Bundle()
            args.putString(LibraryFragment.ARG_FILTER_NAME, filterName)
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_library, container, false)

        //Get argument
        arguments?.let {
            filterName = it.getString(ARG_FILTER_NAME)?: ""
        }


        app(view)

        return view

    }

    private fun app(view: View) {
        // Reference to drawer layout
        drawerLayout = requireActivity().findViewById(R.id.main)

        // Avatar icon
        val avatarIcon = view.findViewById<ImageView>(R.id.avatar_icon)
        avatarIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
      
//        //Implement search icon
//        val searchIcon: ImageButton = view.findViewById(R.id.search_icon)
//        searchIcon.setOnClickListener {
//            //Going to search fragment
//            val intent = Intent(context, BrowseLibrary::class.java)
//            startActivity(intent)
//        }

        //Implement item filter
        //Init adapter
        val itemFilterAdapter = FragmentLibraryFilterAdapter(createItemFilter(), this)

        //Init layoutManager
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        //Setup recycleView
        val recyclerView: RecyclerView = view.findViewById(R.id.libraryFilter)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = itemFilterAdapter


        //Your library Fragment
        val yourLibrarySection : View = view.findViewById(R.id.YourLibrarySection)
        yourLibrarySection.setOnClickListener {
            onSelectionListener(null)
            itemFilterAdapter.resetColors()
        }

        //Set up list
        //Default layout, calling once
        onSelectionListener(LibraryFilterItem(filterName))
        itemFilterAdapter.addColor(filterName)
    }

    //Create the item list
    private fun createItemFilter() : ArrayList<LibraryFilterItem> {
        val items = ArrayList<LibraryFilterItem>()
        items.add(LibraryFilterItem("Playlists"))
        items.add(LibraryFilterItem("Artists"))
        items.add(LibraryFilterItem("Albums"))
        items.add(LibraryFilterItem("Podcasts"))

        return items
    }

    override fun onSelectionListener(item: LibraryFilterItem?) {
        //Change fragment depends on the item clicks
        when(item?.name) {
            "Playlists" -> loadFragment(FragmentPlaylists())
            "Artists" -> loadFragment(FragmentArtists())
            "Albums" -> loadFragment(FragmentAlbums())
            "Podcasts" -> loadFragment(FragmentPodcasts())
            else -> loadFragment(FragmentYourLibrary())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_library_container_list, fragment)
            .commit()
    }
}
