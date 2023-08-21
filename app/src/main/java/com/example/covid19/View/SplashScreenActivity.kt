package com.example.covid19.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.covid19.R

class SplashScreenActivity : AppCompatActivity() {
    private val SPLASH_DURATION: Long = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Handler(mainLooper).postDelayed({
            // Start the main activity
            val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
            startActivity(intent)

            // Close the splash screen activity
            finish()
        }, SPLASH_DURATION)
    }
}