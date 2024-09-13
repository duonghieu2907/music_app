import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mymusicapp.MainActivity
import com.example.mymusicapp.R
import com.example.mymusicapp.data.Global
import com.example.mymusicapp.data.MusicAppDatabaseHelper

class LoginActivity : AppCompatActivity() {

    private lateinit var loginButton: Button
    private lateinit var forgotPassword: TextView
    private lateinit var arrowBack: ImageView
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var dbHelper: MusicAppDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize UI
        loginButton = findViewById(R.id.loginButton)
        forgotPassword = findViewById(R.id.forgotPassword)
        arrowBack = findViewById(R.id.arrowBack)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)


        dbHelper = MusicAppDatabaseHelper(this)

        loginButton.setOnClickListener {
            handleLogin()
        }

        forgotPassword.setOnClickListener {
            //chua lam
        }

        arrowBack.setOnClickListener {
            // ua co can lam khom?
            //finish()
        }
    }

    private fun handleLogin() {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both email and password.", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if user exists
        val userId = dbHelper.getUserId(email, password)
        if (userId != "-1") {
            // Login successful
            val app = application as Global
            app.curUserId = userId

            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()

            // Navigate to the main activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // Login failed
            Toast.makeText(this, "Invalid email or password.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToForgotPassword() {
        // chua lam
    }
}
