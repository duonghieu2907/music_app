package com.example.mymusicapp.bottomnavigation

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.graphics.Color
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.mymusicapp.R
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.queue.QueueFragment

class HomeFragment : Fragment() {
    private lateinit var dbHelper: MusicAppDatabaseHelper
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Reference to drawer layout
        drawerLayout = requireActivity().findViewById(R.id.main)

        // Avatar icon
        val avatarIcon = view.findViewById<ImageView>(R.id.avatar_icon)
        avatarIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Handle click for drawer items
        handleDrawerNavigation(drawerLayout)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the database helper
        dbHelper = MusicAppDatabaseHelper(requireContext())

        view.findViewById<LinearLayout>(R.id.mix_item_1).setOnClickListener {
            val boxColor = (it.findViewById<LinearLayout>(R.id.mix_item_1).background as? ColorDrawable)?.color ?: Color.TRANSPARENT
            val underlineColor = (it.findViewById<View>(R.id.underline).background as? ColorDrawable)?.color ?: Color.TRANSPARENT

            openMixPlaylist("Mix 1", boxColor, underlineColor)
        }

        view.findViewById<LinearLayout>(R.id.mix_item_2).setOnClickListener {
            val boxColor = (it.findViewById<LinearLayout>(R.id.mix_item_2).background as? ColorDrawable)?.color ?: Color.TRANSPARENT
            val underlineColor = (it.findViewById<View>(R.id.underline_2).background as? ColorDrawable)?.color ?: Color.TRANSPARENT

            openMixPlaylist("Mix 2", boxColor, underlineColor)
        }

        view.findViewById<LinearLayout>(R.id.mix_item_3).setOnClickListener {
            val boxColor = (it.findViewById<LinearLayout>(R.id.mix_item_3).background as? ColorDrawable)?.color ?: Color.TRANSPARENT
            val underlineColor = (it.findViewById<View>(R.id.underline_3).background as? ColorDrawable)?.color ?: Color.TRANSPARENT

            openMixPlaylist("Mix 3", boxColor, underlineColor)
        }

        view.findViewById<LinearLayout>(R.id.mix_item_4).setOnClickListener {
            val boxColor = (it.findViewById<LinearLayout>(R.id.mix_item_4).background as? ColorDrawable)?.color ?: Color.TRANSPARENT
            val underlineColor = (it.findViewById<View>(R.id.underline_4).background as? ColorDrawable)?.color ?: Color.TRANSPARENT

            openMixPlaylist("Mix 4", boxColor, underlineColor)
        }

        view.findViewById<LinearLayout>(R.id.mix_item_5).setOnClickListener {
            val boxColor = (it.findViewById<LinearLayout>(R.id.mix_item_5).background as? ColorDrawable)?.color ?: Color.TRANSPARENT
            val underlineColor = (it.findViewById<View>(R.id.underline_5).background as? ColorDrawable)?.color ?: Color.TRANSPARENT

            openMixPlaylist("Mix 5", boxColor, underlineColor)
        }

    }

    // Function to open the playlist corresponding to the selected mix
    private fun openMixPlaylist(mixName: String, boxColor: Int, underlineColor: Int) {
        // Retrieve the playlist ID using the mix name and the user ID
        val userId = "1"  // Replace with the actual user ID
        val playlistId = dbHelper.getPlaylistIdByName(mixName, userId)

        // Check if the playlist ID exists
        if (playlistId != null) {
            // Create an instance of MixPlaylistFragment
            val mixPlaylistFragment = MixPlaylistFragment()

            // Prepare the bundle with playlist ID and colors
            val bundle = Bundle().apply {
                putString("playlist_id", playlistId)
                putInt("box_color", boxColor)
                putInt("underline_color", underlineColor)
            }
            mixPlaylistFragment.arguments = bundle

            // Navigate to the MixPlaylistFragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, mixPlaylistFragment)
                .addToBackStack(null)
                .commit()
        } else {
            Toast.makeText(requireContext(), "Playlist not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleDrawerNavigation(view: View) {
        val settingsButton: TextView = view.findViewById(R.id.settings_button)
        val historyButton: TextView = view.findViewById(R.id.history_button)
        val queueButton: TextView = view.findViewById(R.id.queue_button)

        settingsButton.setOnClickListener {
            navigateToSettings()
        }

        historyButton.setOnClickListener {
            navigateToHistory()
        }

        queueButton.setOnClickListener {
            navigateToQueue()
        }
    }

    private fun navigateToSettings() {
        Toast.makeText(context, "This feature is in development", Toast.LENGTH_SHORT).show()
    }

    private fun navigateToHistory() {
        val recentlyPlayedFragment = RecentlyPlayedFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, recentlyPlayedFragment)  // Replace with your fragment container ID
            .addToBackStack(null)  // Optional: Add this transaction to the back stack
            .commit()
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun navigateToQueue() {
        val queueFragment = QueueFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, queueFragment)  // Replace with your fragment container ID
            .addToBackStack(null)  // Optional: Add this transaction to the back stack
            .commit()
        drawerLayout.closeDrawer(GravityCompat.START)
    }
}
