package com.example.mymusicapp.data

import android.app.Application

class Global : Application() {
    var curUserId: String = "31j3r46xev5snbqwjqbuwbrm6cwu"

    //To access from fragment
    //val app = requireActivity().application as Global
    //println(app.userId)

    //To access from activity
    //val app = applicationContext as Global
    //app.userId = "3"
    //println(app.userId)
}