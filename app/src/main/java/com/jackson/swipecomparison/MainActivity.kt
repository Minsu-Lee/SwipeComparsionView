package com.jackson.swipecomparison

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jackson.swipecomparison.databinding.ActivityMainBinding
import com.jackson.swipecomparison.extensions.setContentView

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding)
    }
}