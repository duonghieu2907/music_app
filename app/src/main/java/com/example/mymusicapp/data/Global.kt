package com.example.mymusicapp.data

import android.app.Application
import android.content.Context

class Global : Application() {
    //var curUserId: String = "31j3r46xev5snbqwjqbuwbrm6cwu"
    //var curUserId: String? = "3"
    var curUserId: String? = null

    //To access from fragment
    //val app = requireActivity().application as Global
    //println(app.userId)

    //To access from activity
    //val app = applicationContext as Global
    //app.userId = "3"
    //println(app.userId)

    override fun onCreate() {
        super.onCreate()
        // Initialize curUserId from SharedPreferences
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        curUserId = sharedPref.getString("curUserId", null) // null if no user is logged in
    }

    fun saveUserIdToPreferences(userId: String) {
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("curUserId", userId)
            apply() // Save the user ID
        }
    }

    fun getUserIdFromPreferences(): String? {
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPref.getString("curUserId", null) // Retrieve the user ID or null if not logged in
    }


}