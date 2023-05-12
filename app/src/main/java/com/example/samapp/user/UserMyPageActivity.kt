package com.example.samapp.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.samapp.databinding.ActivityUserMyPageBinding

class UserMyPageActivity : AppCompatActivity() {
    lateinit var binding: ActivityUserMyPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}