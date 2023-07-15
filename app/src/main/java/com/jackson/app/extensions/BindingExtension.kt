package com.jackson.app.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

fun <T: ViewBinding> AppCompatActivity.setContentView(binding: T): T {
    binding.root.let(::setContentView)
    return binding
}