package com.example.mymusicapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mymusicapp.data.Global
import com.example.mymusicapp.data.MusicAppDatabaseHelper

class LoginActivity : AppCompatActivity() {

    private lateinit var loginButton: Button
    private lateinit var signUpButton: TextView
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var dbHelper: MusicAppDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Check if the user is already logged in
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val loggedInUserId = sharedPref.getString("curUserId", null)

        if (loggedInUserId != null) {
            // User is already logged in, skip login and go to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        // Initialize UI
        loginButton = findViewById(R.id.loginButton)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        signUpButton = findViewById(R.id.signUp)

        //get Email that is from logout or sign up
        emailInput.setText(intent.getStringExtra("Email")?: "")

        dbHelper = MusicAppDatabaseHelper(this)

        loginButton.setOnClickListener {
            handleLogin()
        }

        signUpButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun handleLogin() {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both email and password.", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = dbHelper.getUserId(email, password)
        if (userId != null) {
            // Login successful
            val app = applicationContext as Global
            app.curUserId = userId

            // Save userId in SharedPreferences
            val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("curUserId", userId)
                apply()
            }

            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()

            // Start the main activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // Login failed
            Toast.makeText(this, "Invalid email or password.", Toast.LENGTH_SHORT).show()
        }
    }
}
