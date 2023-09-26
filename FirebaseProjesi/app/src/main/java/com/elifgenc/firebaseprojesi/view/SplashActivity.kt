package com.elifgenc.firebaseprojesi.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.elifgenc.firebaseprojesi.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private  lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            splashActivity()
        },  1000)

    }
    private fun splashActivity(){
        val intent = Intent(this, CurrentActivity::class.java)
        startActivity(intent)
        finish()
    }
}