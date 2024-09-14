package com.example.mymusicapp

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.models.User
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.random.Random

class SignUpActivity : AppCompatActivity() {

    private lateinit var db: MusicAppDatabaseHelper
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var userNameInput: EditText
    private lateinit var dateOfBirthInput: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)

        app()
    }

    private fun app() {
        // Initialize UI
        val signUpButton = findViewById<Button>(R.id.signUpButton)
        val arrowBack = findViewById<ImageView>(R.id.arrowBack)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        userNameInput = findViewById(R.id.userNameInput)
        dateOfBirthInput = findViewById(R.id.DOBInput)

        db = MusicAppDatabaseHelper(this)

        //set up date dialog for date of birth
        val dateOfBirthCalendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(this,
            {_, year, month, day_of_month ->
               dateOfBirthCalendar.set(year, month, day_of_month)
                updateDateOfBirthInput(dateOfBirthCalendar)
            },
            dateOfBirthCalendar.get(Calendar.YEAR),
            dateOfBirthCalendar.get(Calendar.MONTH),
            dateOfBirthCalendar.get(Calendar.DAY_OF_MONTH))

        //date of birth
        dateOfBirthInput.setOnClickListener {
            datePickerDialog.show()
        }

        //sign up
        signUpButton.setOnClickListener {
            handleSignUp()
        }

        //back to log in
        arrowBack.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun handleSignUp() {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()
        val userName = userNameInput.text.toString().trim()
        val dateOfBirth = dateOfBirthInput.text.toString().trim()
        val NewUserId = generate16Char()

        if (email.isEmpty() || password.isEmpty() || userName.isEmpty() || dateOfBirth.isEmpty()) {
            Toast.makeText(this, "Please enter email, password, username and date of birth.", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = db.getUserId(email, password)
        if (userId == null) {
            // User does not exists, add successful
            db.addUser(User(NewUserId, userName, email, password, dateOfBirth, ""))

            Toast.makeText(this, "Add successful!", Toast.LENGTH_SHORT).show()

            // Start the main activity
            val intent = Intent(this, LoginActivity::class.java)
            val bundle = Bundle()
            bundle.putString("Email", emailInput.text.toString())
            intent.putExtras(bundle)
            startActivity(intent)
        } else {
            // Login failed
            Toast.makeText(this, "This user is exist.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun generate16Char(): String {
        val random = Random
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        val result = StringBuilder(28)
        for(i in 0 until 27) {
            result.append(chars[random.nextInt(chars.length)])
        }
        return result.toString()
    }


    private fun updateDateOfBirthInput(dateOfBirthCalendar: Calendar?) {
        if(dateOfBirthCalendar == null) {
            dateOfBirthInput.setText("")
            return
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val formattedDate = dateFormat.format(dateOfBirthCalendar.time)
        dateOfBirthInput.setText(formattedDate)

    }

}