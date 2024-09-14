package com.example.mymusicapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mymusicapp.data.Global

class LaunchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = applicationContext as Global
        if (app.curUserId != null) {
            // User is logged in
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            // User is not logged in
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()
    }
}
