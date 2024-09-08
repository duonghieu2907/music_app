package com.example.mymusicapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class RedirectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val uri: Uri? = intent.data
        if(uri != null && uri.toString().startsWith(REDIRECT_URI)) {
            val code = uri.getQueryParameter("code")
            if(code != null) {
                val resultIntent = Intent()
                resultIntent.putExtra("AUTH_CODE", code)
                setResult(RESULT_OK, resultIntent)
            }
            else {
                Log.e("RedirectActivity", "Authorization code not found")
                setResult(RESULT_CANCELED)
            }
            finish()
        }
        else {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    companion object {
        private const val REDIRECT_URI = "myapp://callback"
    }
}