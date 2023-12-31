package com.dicoding.thestoryapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.thestoryapp.constant.PREF_TOKEN
import com.dicoding.thestoryapp.ui.auth.LoginActivity
import com.dicoding.thestoryapp.ui.story.ListStoryActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hideActionBar()

        Handler().postDelayed(Runnable
        // Using handler with postDelayed called runnable run method
        {

            val token = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
                .getString(PREF_TOKEN, "")
            if (token != null && token.isNotEmpty()) {
                val i = Intent(this@MainActivity, ListStoryActivity::class.java)
                startActivity(i)
                finish()
            } else {
                val i = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(i)
                finish()
            }

        }, 3 * 1000
        )
    }

    private fun hideActionBar() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}