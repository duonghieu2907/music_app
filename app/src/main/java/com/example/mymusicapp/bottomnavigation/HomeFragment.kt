package com.example.mymusicapp.bottomnavigation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.mymusicapp.R
import com.example.mymusicapp.queue.QueueFragment

class HomeFragment : Fragment() {

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
        Log.d("HomeFragment", "Navigating to SettingsFragment.")
//        val settingsFragment = SettingsFragment()
//        parentFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, settingsFragment)  // Replace with your fragment container ID
//            .addToBackStack(null)  // Optional: Add this transaction to the back stack
//            .commit()
//        drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun navigateToHistory() {
        Log.d("HomeFragment", "Navigating to HistoryFragment.")
//        val historyFragment = HistoryFragment()
//        parentFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, historyFragment)  // Replace with your fragment container ID
//            .addToBackStack(null)  // Optional: Add this transaction to the back stack
//            .commit()
//        drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun navigateToQueue() {
        Log.d("HomeFragment", "Navigating to QueueFragment.")
        val queueFragment = QueueFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, queueFragment)  // Replace with your fragment container ID
            .addToBackStack(null)  // Optional: Add this transaction to the back stack
            .commit()
        drawerLayout.closeDrawer(GravityCompat.START)
    }
}
