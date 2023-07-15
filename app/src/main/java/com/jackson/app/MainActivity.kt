package com.jackson.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jackson.app.databinding.ActivityMainBinding
import com.jackson.app.extensions.setContentView

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding)
        supportActionBar?.hide()
    }
}